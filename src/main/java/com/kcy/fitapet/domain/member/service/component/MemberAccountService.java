package com.kcy.fitapet.domain.member.service.component;

import com.kcy.fitapet.domain.member.domain.Member;
import com.kcy.fitapet.domain.member.dto.account.AccountProfileRes;
import com.kcy.fitapet.domain.member.dto.account.AccountSearchReq;
import com.kcy.fitapet.domain.member.dto.account.ProfilePatchReq;
import com.kcy.fitapet.domain.member.dto.account.UidRes;
import com.kcy.fitapet.domain.member.exception.AccountErrorCode;
import com.kcy.fitapet.domain.member.exception.SmsErrorCode;
import com.kcy.fitapet.domain.member.service.module.MemberSearchService;
import com.kcy.fitapet.domain.member.type.MemberAttrType;
import com.kcy.fitapet.domain.notification.type.NotificationType;
import com.kcy.fitapet.global.common.redis.sms.SmsRedisHelper;
import com.kcy.fitapet.global.common.redis.sms.type.SmsPrefix;
import com.kcy.fitapet.global.common.response.code.StatusCode;
import com.kcy.fitapet.global.common.response.exception.GlobalErrorException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberAccountService {
    private final MemberSearchService memberSearchService;
    private final SmsRedisHelper smsRedisHelper;

    private final PasswordEncoder bCryptPasswordEncoder;

    @Transactional(readOnly = true)
    public AccountProfileRes getProfile(Long userId) {
        Member member = memberSearchService.findById(userId);
        return AccountProfileRes.from(member);
    }

    @Transactional(readOnly = true)
    public boolean existsUid(String uid) {
        return memberSearchService.isExistByUid(uid);
    }

    @Transactional
    public void updateProfile(Long userId, ProfilePatchReq req, MemberAttrType type) {
        Member member = memberSearchService.findById(userId);

        if (type == MemberAttrType.NAME) {
            validateUsername(member, req.getName());
            member.updateName(req.getName());
        } else if (type == MemberAttrType.PASSWORD) {
            validatePassword(member, req.getPrePassword(), req.getNewPassword());
            member.updatePassword(req.getNewPassword(), bCryptPasswordEncoder);
        }
    }

    @Transactional(readOnly = true)
    public UidRes getUidWhenSmsAuthenticated(String phone, String code, SmsPrefix prefix) {
        log.info("phone: {}, code: {}, prefix: {}", phone, code, prefix);
        validatePhone(phone, code, prefix);
        log.info("isValid");
        Member member = memberSearchService.findByPhone(phone);
        smsRedisHelper.removeCode(phone, prefix);
        return UidRes.of(member.getUid(), member.getCreatedAt());
    }

    @Transactional
    public void overwritePassword(AccountSearchReq req, String code, SmsPrefix prefix) {
        validatePhone(req.phone(), code, prefix);
        Member member = memberSearchService.findByPhone(req.phone());

        if (!StringUtils.hasText(req.newPassword())) {
            StatusCode errorCode = AccountErrorCode.INVALID_PASSWORD_REQUEST;
            log.warn("비밀번호 변경 실패: {}", errorCode);
            throw new GlobalErrorException(errorCode);
        }
        member.updatePassword(req.newPassword(), bCryptPasswordEncoder);
        smsRedisHelper.removeCode(req.phone(), prefix);
    }

    @Transactional
    public void updateNotification(Long requestId, Long userId, NotificationType type) {
        Member member = memberSearchService.findById(userId);
        member.updateNotificationFromType(type);
    }

    /**
     * 사용자 입력 닉네임과 기존 닉네임이 일치하는지 확인하고 변경 사항이 없으면 예외 <br/>
     * @param member Member : 사용자 정보
     * @param username String : 사용자 입력 닉네임
     * @throws GlobalErrorException : 닉네임 변경 실패
     */
    private void validateUsername(Member member, String username) {
        if (!StringUtils.hasText(username) || member.getName().equals(username)) {
            StatusCode errorCode = AccountErrorCode.NOT_CHANGE_NAME_ERROR;
            log.warn("닉네임 변경 실패: {}", errorCode);
            throw new GlobalErrorException(errorCode);
        }
    }

    /**
     * 비밀번호 변경 유효성 검사 <br/>
     * validation check #1: 사용자 입력 기존 비밀번호와 새 비밀번호가 일치하는지 확인하고 변경 사항이 없으면 예외 <br/>
     * validation check #2: 기존 비밀번호와 일치하는지 확인하고 일치하지 않으면 예외 <br/>
     * @param member Member : 사용자 정보
     * @param prePassword String : 기존 비밀번호
     * @param newPassword String : 새 비밀번호
     * @throws GlobalErrorException : 비밀번호 변경 실패
     */
    private void validatePassword(Member member, String prePassword, String newPassword) {
        StatusCode errorCode = null;
        if (!StringUtils.hasText(newPassword) || prePassword.equals(newPassword)) {
            errorCode = AccountErrorCode.INVALID_PASSWORD_REQUEST;
        } else if (!member.checkPassword(prePassword, bCryptPasswordEncoder)) {
            errorCode = AccountErrorCode.NOT_MATCH_PASSWORD_ERROR;
        }

        if (errorCode != null) {
            log.warn("비밀번호 변경 실패: {}", errorCode);
            throw new GlobalErrorException(errorCode);
        }
    }

    /**
     * in-memory에 해당 전화번호에 대한 정보가 저장되어 있는 지 확인하고, <br/>
     * {"phone:"인증번호"} 형태의 인증번호가 일치함을 확인
     * @param phone : 전화번호
     * @param code : 인증번호
     * @param prefix : 인증번호 타입
     */
    private void validatePhone(String phone, String code, SmsPrefix prefix) {
        if (!smsRedisHelper.isExistsCode(phone, prefix)) {
            StatusCode errorCode = SmsErrorCode.EXPIRED_AUTH_CODE;
            log.warn("인증번호 유효성 검사 실패: {}", errorCode);
            throw new GlobalErrorException(errorCode);
        }

        if (!smsRedisHelper.isCorrectCode(phone, code, prefix)) {
            StatusCode errorCode = SmsErrorCode.INVALID_AUTH_CODE;
            log.warn("인증번호 유효성 검사 실패: {}", errorCode);
            throw new GlobalErrorException(errorCode);
        }
    }
}
