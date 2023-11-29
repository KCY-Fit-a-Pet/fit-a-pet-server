package com.kcy.fitapet.domain.member.service.component;

import com.kcy.fitapet.domain.member.domain.Member;
import com.kcy.fitapet.domain.member.dto.profile.MemberProfileRes;
import com.kcy.fitapet.domain.member.dto.profile.ProfilePatchReq;
import com.kcy.fitapet.domain.member.exception.ProfileErrorCode;
import com.kcy.fitapet.domain.member.service.module.MemberSearchService;
import com.kcy.fitapet.global.common.response.code.ErrorCode;
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
public class MemberProfileService {
    private final MemberSearchService memberSearchService;
    private final PasswordEncoder bCryptPasswordEncoder;

    @Transactional(readOnly = true)
    public MemberProfileRes getProfile(Long userId) {
        Member member = memberSearchService.findById(userId);
        return MemberProfileRes.from(member);
    }

    @Transactional
    public void updateName(Long userId, ProfilePatchReq req) {
        Member member = memberSearchService.findById(userId);

        isValidateNickname(member, req.getName());

        member.updateName(req.getName());
    }

    @Transactional
    public void updatePassword(Long userId, ProfilePatchReq req) {
        Member member = memberSearchService.findById(userId);

        isValidatePassword(member, req.getPrePassword(), req.getNewPassword());

        member.updatePassword(req.getNewPassword(), bCryptPasswordEncoder);
    }

    @Transactional
    public void updateNotification(Long userId, String type) {
        Member member = memberSearchService.findById(userId);

        if (type.equals("care")) {
            member.updateEmailNotification();
        } else if (type.equals("memo")) {
            member.updateSmsNotification();
        } else if (type.equals("schedule")) {
            member.updateScheduleNotification();
        } else {
            StatusCode errorCode = ProfileErrorCode.INVALID_NOTIFICATION_TYPE_ERROR;
            log.warn("알림 설정 변경 실패: {}", errorCode);
            throw new GlobalErrorException(errorCode);
        }
    }

    /**
     * 사용자 입력 닉네임과 기존 닉네임이 일치하는지 확인하고 변경 사항이 없으면 예외 <br/>
     * @param member Member : 사용자 정보
     * @param nickname String : 사용자 입력 닉네임
     * @throws GlobalErrorException : 닉네임 변경 실패
     */
    private void isValidateNickname(Member member, String nickname) {
        if (!StringUtils.hasText(nickname) || member.getName().equals(nickname)) {
            StatusCode errorCode = ProfileErrorCode.NOT_CHANGE_NAME_ERROR;
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
    private void isValidatePassword(Member member, String prePassword, String newPassword) {
        StatusCode errorCode = null;
        if (prePassword.equals(newPassword)) {
            errorCode = ProfileErrorCode.NOT_CHANGE_PASSWORD_ERROR;
        } else if (!member.checkPassword(prePassword, bCryptPasswordEncoder)) { // TODO: 빈 문자열이 들어올 때 검사
            errorCode = ProfileErrorCode.NOT_MATCH_PASSWORD_ERROR;
        }

        if (errorCode != null) {
            log.warn("비밀번호 변경 실패: {}", errorCode);
            throw new GlobalErrorException(errorCode);
        }
    }
}
