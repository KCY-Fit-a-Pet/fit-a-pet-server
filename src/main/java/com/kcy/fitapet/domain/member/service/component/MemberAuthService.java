package com.kcy.fitapet.domain.member.service.component;

import com.kcy.fitapet.domain.member.domain.Member;
import com.kcy.fitapet.domain.member.dto.SignInReq;
import com.kcy.fitapet.domain.member.dto.SignUpReq;
import com.kcy.fitapet.domain.member.service.module.MemberSaveService;
import com.kcy.fitapet.domain.member.service.module.MemberSearchService;
import com.kcy.fitapet.global.common.response.code.ErrorCode;
import com.kcy.fitapet.global.common.response.exception.GlobalErrorException;
import com.kcy.fitapet.global.common.util.jwt.JwtUtil;
import com.kcy.fitapet.global.common.util.jwt.entity.JwtUserInfo;
import com.kcy.fitapet.global.common.util.redis.forbidden.ForbiddenTokenService;
import com.kcy.fitapet.global.common.util.redis.refresh.RefreshToken;
import com.kcy.fitapet.global.common.util.redis.refresh.RefreshTokenService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.kcy.fitapet.global.common.util.jwt.AuthConstants.ACCESS_TOKEN;
import static com.kcy.fitapet.global.common.util.jwt.AuthConstants.REFRESH_TOKEN;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberAuthService {
    private final RefreshTokenService refreshTokenService;
    private final ForbiddenTokenService forbiddenTokenService;
    private final MemberSearchService memberSearchService;
    private final MemberSaveService memberSaveService;
    private final JwtUtil jwtUtil;

    private final PasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public Map<String, String> register(SignUpReq dto) {
        Member requestMember = dto.toEntity();
        requestMember.encodePassword(bCryptPasswordEncoder);
        log.info("회원가입 요청: {}", requestMember);
        validateMember(requestMember);

        Member registeredMember = memberSaveService.saveMember(requestMember);
        log.info("회원가입 완료: {}", registeredMember);
        JwtUserInfo jwtUserInfo = JwtUserInfo.from(registeredMember);

        return generateToken(jwtUserInfo);
    }

    @Transactional
    public Map<String, String> login(SignInReq dto) {
        Member member = memberSearchService.getMemberByUid(dto.uid());
        if (member.checkPassword(dto.password(), bCryptPasswordEncoder))
            throw new GlobalErrorException(ErrorCode.NOT_MATCH_PASSWORD_ERROR);

        JwtUserInfo jwtUserInfo = JwtUserInfo.from(member);

        return generateToken(jwtUserInfo);
    }

    @Transactional
    public void logout(String authHeader, String requestRefreshToken) {
        String accessToken = jwtUtil.resolveToken(authHeader);
        Long userId = jwtUtil.getUserIdFromToken(accessToken);

        refreshTokenService.logout(requestRefreshToken);
        forbiddenTokenService.register(accessToken, userId);
    }

    @Transactional
    public Map<String, String> refresh(String requestRefreshToken) {
        RefreshToken refreshToken = refreshTokenService.refresh(requestRefreshToken);

        Long memberId = refreshToken.getUserId();
        JwtUserInfo dto = JwtUserInfo.from(memberSearchService.getMemberById(memberId));
        String accessToken = jwtUtil.generateAccessToken(dto);

        return Map.of(ACCESS_TOKEN.getValue(), accessToken, REFRESH_TOKEN.getValue(), refreshToken.getToken());
    }

    private void validateMember(Member member) {
        if (memberSearchService.isExistMemberByUid(member.getUid()))
            throw new GlobalErrorException(ErrorCode.DUPLICATE_NICKNAME_ERROR);

        if (memberSearchService.isExistMemberByEmail(member.getEmail()))
            throw new GlobalErrorException(ErrorCode.DUPLICATE_EMAIL_ERROR);

        if (memberSearchService.isExistMemberByPhone(member.getPhone()))
            throw new GlobalErrorException(ErrorCode.DUPLICATE_PHONE_ERROR);
    }

    private Map<String, String> generateToken(JwtUserInfo jwtUserInfo) {
        String accessToken = jwtUtil.generateAccessToken(jwtUserInfo);
        String refreshToken = refreshTokenService.issueRefreshToken(accessToken);
        log.debug("accessToken : {}, refreshToken : {}", accessToken, refreshToken);

        return Map.of(ACCESS_TOKEN.getValue(), accessToken, REFRESH_TOKEN.getValue(), refreshToken);
    }
}