package kr.co.fitapet.api.apis.auth.usecase;

import kr.co.fitapet.api.apis.auth.dto.SignInReq;
import kr.co.fitapet.api.apis.auth.dto.SignUpReq;
import kr.co.fitapet.api.apis.auth.dto.SmsReq;
import kr.co.fitapet.api.apis.auth.dto.SmsRes;
import kr.co.fitapet.api.apis.auth.mapper.JwtMapper;
import kr.co.fitapet.api.apis.auth.mapper.SmsRedisMapper;
import kr.co.fitapet.api.common.security.jwt.consts.JwtType;
import kr.co.fitapet.api.common.security.jwt.dto.Jwt;
import kr.co.fitapet.api.common.security.jwt.dto.JwtSubInfo;
import kr.co.fitapet.api.common.security.jwt.dto.JwtUserInfo;
import kr.co.fitapet.api.common.security.jwt.dto.SmsAuthInfo;
import kr.co.fitapet.api.common.security.jwt.exception.AuthErrorCode;
import kr.co.fitapet.api.common.security.jwt.exception.AuthErrorException;
import kr.co.fitapet.common.annotation.UseCase;
import kr.co.fitapet.common.execption.BaseErrorCode;
import kr.co.fitapet.common.execption.GlobalErrorException;
import kr.co.fitapet.domain.common.redis.sms.type.SmsPrefix;
import kr.co.fitapet.domain.domains.member.domain.AccessToken;
import kr.co.fitapet.domain.domains.member.domain.Member;
import kr.co.fitapet.domain.domains.member.exception.AccountErrorCode;
import kr.co.fitapet.domain.domains.member.service.MemberSaveService;
import kr.co.fitapet.domain.domains.member.service.MemberSearchService;
import kr.co.fitapet.infra.client.sms.snes.SmsProvider;
import kr.co.fitapet.infra.client.sms.snes.dto.SnesDto;
import kr.co.fitapet.infra.client.sms.snes.exception.SmsErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@UseCase
@RequiredArgsConstructor
public class MemberAuthUseCase {
    private final MemberSearchService memberSearchService;
    private final MemberSaveService memberSaveService;

    private final SmsRedisMapper smsRedisMapper;
    private final SmsProvider smsProvider;

    private final JwtMapper jwtMapper;

    private final PasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public Pair<Long, Jwt> register(String requestSmsAccessToken, SignUpReq dto) {
        String smsToken = jwtMapper.resolveToken(requestSmsAccessToken, JwtType.SMS_AUTH_TOKEN);

        if (jwtMapper.isForbidden(smsToken))
            throw new AuthErrorException(AuthErrorCode.FORBIDDEN_ACCESS_TOKEN, "허용되지 않은 토큰입니다.");

        JwtSubInfo jwtSubInfo = jwtMapper.getSubInfoFromToken(smsToken, JwtType.SMS_AUTH_TOKEN);
        smsRedisMapper.removeCode(jwtSubInfo.phoneNumber(), SmsPrefix.REGISTER);

        Member requestMember = dto.toEntity(jwtSubInfo.phoneNumber(), bCryptPasswordEncoder);
        validateMember(requestMember);

        Member registeredMember = memberSaveService.saveMember(requestMember);

        jwtMapper.ban(smsToken, JwtType.SMS_AUTH_TOKEN);
        return Pair.of(registeredMember.getId(), jwtMapper.login(JwtUserInfo.from(registeredMember)));
    }

    @Transactional
    public Pair<Long, Jwt> login(SignInReq dto) {
        Member member = memberSearchService.findByUid(dto.uid());
        if (!bCryptPasswordEncoder.matches(dto.password(), member.getPassword()))
            throw new GlobalErrorException(AccountErrorCode.NOT_MATCH_PASSWORD_ERROR);

        return Pair.of(member.getId(), jwtMapper.login(JwtUserInfo.from(member)));
    }

    public void logout(AccessToken requestAccessToken, String requestRefreshToken) {
        jwtMapper.logout(requestAccessToken, requestRefreshToken);
    }

    public Jwt refresh(String requestRefreshToken) {
        return jwtMapper.refresh(requestRefreshToken);
    }

    @Transactional
    public SmsRes sendCode(SmsReq dto, SmsPrefix prefix) {
        validateForSms(prefix, dto);
        SnesDto.SensInfo smsInfo = smsProvider.sendCodeByPhoneNumber(SnesDto.Request.of(dto.to()));

        smsRedisMapper.saveSmsAuthToken(dto.to(), smsInfo.code(), prefix);
        LocalDateTime expireTime = smsRedisMapper.getExpiredTime(dto.to(), prefix);
        log.info("인증번호 만료 시간: {}", expireTime);
        return SmsRes.of(dto.to(), smsInfo.requestTime(), expireTime);
    }

    @Transactional
    public Jwt checkCodeForRegister(SmsReq smsReq, String requestCode) {
        if (!smsRedisMapper.isCorrectCode(smsReq.to(), requestCode, SmsPrefix.REGISTER)) {
            log.warn("인증번호 불일치 -> 사용자 입력 인증 번호 : {}", requestCode);
            throw new GlobalErrorException(SmsErrorCode.INVALID_AUTH_CODE);
        }
        smsRedisMapper.removeCode(smsReq.to(), SmsPrefix.REGISTER);

        if (memberSearchService.isExistByPhone(smsReq.to())) {
            Member member = memberSearchService.findByPhone(smsReq.to());
            if (member.getIsOauth().equals(Boolean.TRUE)) {
                member.updateOauthToOriginAccount();
                return jwtMapper.login(JwtUserInfo.from(member));
            }
        }

        return Jwt.of(jwtMapper.generateToken(SmsAuthInfo.of(1L, smsReq.to()), JwtType.SMS_AUTH_TOKEN), null);
    }

    @Transactional(readOnly = true)
    public void checkCodeForSearch(SmsReq req, String code, SmsPrefix prefix) {
        if (!smsRedisMapper.isExistsCode(req.to(), prefix)) {
            BaseErrorCode errorCode = SmsErrorCode.EXPIRED_AUTH_CODE;
            log.warn("인증번호 유효성 검사 실패: {}", errorCode.getExplainError());
            throw new GlobalErrorException(errorCode);
        }

        if (!smsRedisMapper.isCorrectCode(req.to(), code, prefix)) {
            BaseErrorCode errorCode = SmsErrorCode.INVALID_AUTH_CODE;
            log.warn("인증번호 유효성 검사 실패: {}", errorCode.getExplainError());
            throw new GlobalErrorException(errorCode);
        }
    }

    private void validateForSms(SmsPrefix prefix, SmsReq req) {
        boolean isExistPhone = memberSearchService.isExistByPhone(req.to());
        if (prefix.equals(SmsPrefix.REGISTER) && isExistPhone) {
            if (!memberSearchService.findByPhone(req.to()).getIsOauth()) {
                log.warn("중복된 전화번호로 인한 회원가입 요청 실패: {}", req.to());
                throw new GlobalErrorException(AccountErrorCode.DUPLICATE_PHONE_ERROR);
            }
        } else if (prefix.equals(SmsPrefix.UID) && !isExistPhone) {
            log.warn("DB에 존재하지 않는 전화번호로 인한 SMS 인증 요청 실패: {}", req.to());
            throw new GlobalErrorException(AccountErrorCode.NOT_FOUND_PHONE_ERROR);
        } else if (prefix.equals(SmsPrefix.PASSWORD)) {
            if (!isExistPhone) {
                log.warn("DB에 존재하지 않는 전화번호로 인한 SMS 인증 요청 실패: {}", req.to());
                throw new GlobalErrorException(AccountErrorCode.NOT_FOUND_PHONE_ERROR);
            } else if (!memberSearchService.isExistByPhoneAndUid(req.to(), req.uid())) {
                log.warn("등록된 유저 전화번호와 다른 전화번호 입력: {}", req.to());
                throw new GlobalErrorException(AccountErrorCode.MISSMATCH_PHONE_AND_UID_ERROR);
            }
        }
    }

    private void validateMember(Member member) {
        if (memberSearchService.isExistByUidOrPhone(member.getUid(), member.getPhone()))
            throw new GlobalErrorException(AccountErrorCode.DUPLICATE_USER_INFO_ERROR);
    }
}
