package com.kcy.fitapet.domain.member.service.component;

import com.kcy.fitapet.domain.member.domain.Member;
import com.kcy.fitapet.domain.member.dto.account.AccountProfileRes;
import com.kcy.fitapet.domain.member.dto.account.AccountSearchReq;
import com.kcy.fitapet.domain.member.dto.account.ProfilePatchReq;
import com.kcy.fitapet.domain.member.dto.account.UidRes;
import com.kcy.fitapet.domain.member.dto.mapping.MemberUidMapping;
import com.kcy.fitapet.domain.member.exception.AccountErrorCode;
import com.kcy.fitapet.domain.member.exception.SmsErrorCode;
import com.kcy.fitapet.domain.member.service.module.MemberSearchService;
import com.kcy.fitapet.global.common.response.code.StatusCode;
import com.kcy.fitapet.global.common.response.exception.GlobalErrorException;
import com.kcy.fitapet.global.common.util.redis.sms.SmsCertificationService;
import com.kcy.fitapet.global.common.util.redis.sms.SmsPrefix;
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
    private final SmsCertificationService smsCertificationService;

    private final PasswordEncoder bCryptPasswordEncoder;

    @Transactional(readOnly = true)
    public AccountProfileRes getProfile(Long userId) {
        Member member = memberSearchService.findById(userId);
        return AccountProfileRes.from(member);
    }

    @Transactional
    public void updateName(Long userId, ProfilePatchReq req) {
        Member member = memberSearchService.findById(userId);

        validateUsername(member, req.getName());

        member.updateName(req.getName());
    }

    @Transactional
    public void updatePassword(Long userId, ProfilePatchReq req) {
        Member member = memberSearchService.findById(userId);

        validatePassword(member, req.getPrePassword(), req.getNewPassword());

        member.updatePassword(req.getNewPassword(), bCryptPasswordEncoder);
    }

    @Transactional(readOnly = true)
    public UidRes getUidWhenSmsAuthenticated(String phone, SmsPrefix prefix) {
        validatePhone(phone, prefix);
        MemberUidMapping res = memberSearchService.findUidAndCreatedAtByPhone(phone);
        return UidRes.of(res.getUid(), res.getCreatedAt());
    }

    @Transactional
    public void overwritePassword(AccountSearchReq req, SmsPrefix prefix) {
        validatePhone(req.phone(), prefix);
        Member member = memberSearchService.findByPhone(req.phone());

        if (!StringUtils.hasText(req.getNewPassword())) {
            StatusCode errorCode = AccountErrorCode.INVALID_PASSWORD_REQUEST;
            log.warn("비밀번호 변경 실패: {}", errorCode);
            throw new GlobalErrorException(errorCode);
        }
        member.updatePassword(req.getNewPassword(), bCryptPasswordEncoder);
    }

    @Transactional
    public void updateNotification(Long userId, String type) {
        Member member = memberSearchService.findById(userId);

        if (type.equalsIgnoreCase("care")) {
            member.updateEmailNotification();
        } else if (type.equalsIgnoreCase("memo")) {
            member.updateSmsNotification();
        } else if (type.equalsIgnoreCase("schedule")) {
            member.updateScheduleNotification();
        } else {
            StatusCode errorCode = AccountErrorCode.INVALID_NOTIFICATION_TYPE_ERROR;
            log.warn("알림 설정 변경 실패: {}", errorCode);
            throw new GlobalErrorException(errorCode);
        }
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

    private void validatePhone(String phone, SmsPrefix prefix) {
        if (!smsCertificationService.existsCertificationNumber(phone, prefix)) {
            StatusCode errorCode = SmsErrorCode.EXPIRED_AUTH_CODE;
            log.warn("인증번호 유효성 검사 실패: {}", errorCode);
            throw new GlobalErrorException(errorCode);
        }
    }
}
