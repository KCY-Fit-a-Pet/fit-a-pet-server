package kr.co.fitapet.api.apis.profile.usecase;

import kr.co.fitapet.api.apis.auth.mapper.SmsRedisMapper;
import kr.co.fitapet.common.annotation.UseCase;
import kr.co.fitapet.common.execption.BaseErrorCode;
import kr.co.fitapet.common.execption.GlobalErrorException;
import kr.co.fitapet.domain.common.redis.sms.type.SmsPrefix;
import kr.co.fitapet.domain.domains.manager.domain.Manager;
import kr.co.fitapet.domain.domains.manager.service.ManagerSearchService;
import kr.co.fitapet.domain.domains.member.domain.Member;
import kr.co.fitapet.domain.domains.member.domain.MemberNickname;
import kr.co.fitapet.domain.domains.member.dto.AccountProfileRes;
import kr.co.fitapet.api.apis.profile.dto.AccountSearchReq;
import kr.co.fitapet.api.apis.profile.dto.ProfilePatchReq;
import kr.co.fitapet.domain.domains.member.dto.MemberInfo;
import kr.co.fitapet.domain.domains.member.dto.UidRes;
import kr.co.fitapet.domain.domains.member.exception.AccountErrorCode;
import kr.co.fitapet.domain.domains.member.service.MemberSearchService;
import kr.co.fitapet.domain.domains.member.type.MemberAttrType;
import kr.co.fitapet.domain.domains.memo.dto.MemoCategoryInfoDto;
import kr.co.fitapet.domain.domains.memo.dto.MemoInfoDto;
import kr.co.fitapet.domain.domains.memo.service.MemoSearchService;
import kr.co.fitapet.domain.domains.notification.type.NotificationType;
import kr.co.fitapet.domain.domains.pet.domain.Pet;
import kr.co.fitapet.domain.domains.schedule.dto.ScheduleInfoDto;
import kr.co.fitapet.domain.domains.schedule.service.ScheduleSearchService;
import kr.co.fitapet.infra.client.sms.snes.exception.SmsErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@UseCase
@RequiredArgsConstructor
@Slf4j
public class MemberAccountUseCase {
    private final MemberSearchService memberSearchService;

    private final ManagerSearchService managerSearchService;

    private final ScheduleSearchService scheduleSearchService;
    private final MemoSearchService memoSearchService;

    private final SmsRedisMapper smsRedisMapper;

    private final PasswordEncoder bCryptPasswordEncoder;

    @Transactional(readOnly = true)
    public AccountProfileRes getProfile(Long userId) {
        Member member = memberSearchService.findById(userId);
        return AccountProfileRes.from(member);
    }

    @Transactional(readOnly = true)
    public MemberInfo searchProfile(Long requesterId, String search) {
        return memberSearchService.findMemberInfo(requesterId, search);
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
            validatePassword(member, req.getPrePassword(), req.getNewEncodedPassword(bCryptPasswordEncoder));
            member.updateEncodedPassword(req.getNewEncodedPassword(bCryptPasswordEncoder));
        }
    }

    @Transactional(readOnly = true)
    public UidRes getUidWhenSmsAuthenticated(String phone, String code, SmsPrefix prefix) {
        validatePhone(phone, code, prefix);
        Member member = memberSearchService.findByPhone(phone);
        smsRedisMapper.removeCode(phone, prefix);
        return UidRes.of(member.getUid(), member.getCreatedAt());
    }

    @Transactional
    public void overwritePassword(AccountSearchReq req, String code, SmsPrefix prefix) {
        validatePhone(req.phone(), code, prefix);
        Member member = memberSearchService.findByPhone(req.phone());

        member.updateEncodedPassword(req.getNewEncodedPassword(bCryptPasswordEncoder));
        smsRedisMapper.removeCode(req.phone(), prefix);
    }

    @Transactional
    public void updateNotification(Long requestId, Long userId, NotificationType type) {
        Member member = memberSearchService.findById(userId);
        member.updateNotificationFromType(type);
    }

    @Transactional
    public void updateSomeoneNickname(Long from, Long to, String nickname) {
        Member fromMember = memberSearchService.findById(from);
        Member toMember = memberSearchService.findById(to);

        MemberNickname memberNickname;
        if (memberSearchService.isExistNicknameByFromAndTo(from, to)) {
            log.info("기존 별명 업데이트 from : {}, to : {}, nickname : {}", from, to, nickname);
            memberNickname = memberSearchService.findNicknameByFromAndTo(from, to);
            memberNickname.updateNickname(nickname);
        } else {
            log.info("신규 별명 생성 from : {}, to : {}, nickname : {}", from, to, nickname);
            memberNickname = MemberNickname.of(fromMember, toMember, nickname);
        }
    }

    @Transactional(readOnly = true)
    public ScheduleInfoDto findPetSchedules(Long userId, LocalDateTime date) {
        List<Pet> pets = managerSearchService.findAllByMemberId(userId)
                .stream().map(Manager::getPet).toList();
        List<Long> petIds = pets.stream().map(Pet::getId).toList();

        List<ScheduleInfoDto.ScheduleInfo> scheduleInfo = scheduleSearchService.findSchedulesByCalender(date, petIds);
        return ScheduleInfoDto.of(scheduleInfo);
    }

    @Transactional(readOnly = true)
    public List<MemoCategoryInfoDto.MemoCategoryInfo> findMemoCategories(Long userId) {
        List<Long> petIds = memberSearchService.findMyPetIds(userId);
        log.info("userId: {}, petIds: {}", userId, petIds);

        List<Long> rootMemoCategoryIds = memoSearchService.findRootMemoCategoryIdsByPetIds(petIds);
        List<MemoCategoryInfoDto.MemoCategoryInfo> rootMemoCategories = new ArrayList<>();

        for (Long rootMemoCategoryId : rootMemoCategoryIds) {
            rootMemoCategories.add(memoSearchService.findMemoCategoryWithMemoCount(rootMemoCategoryId));
        }

        return rootMemoCategories;
    }

    @Transactional(readOnly = true)
    public MemoInfoDto.PageResponse findMemos(Long userId, Pageable pageable) {
        List<Long> petIds = memberSearchService.findMyPetIds(userId);
        log.info("userId: {}, petIds: {}", userId, petIds);

        return memoSearchService.findMemosByPetIds(petIds, pageable);
    }

    /**
     * 사용자 입력 닉네임과 기존 닉네임이 일치하는지 확인하고 변경 사항이 없으면 예외 <br/>
     * @param member Member : 사용자 정보
     * @param username String : 사용자 입력 닉네임
     * @throws GlobalErrorException : 닉네임 변경 실패
     */
    private void validateUsername(Member member, String username) {
        if (!StringUtils.hasText(username) || member.getName().equals(username)) {
            BaseErrorCode errorCode = AccountErrorCode.NOT_CHANGE_NAME_ERROR;
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
        BaseErrorCode errorCode = null;
        if (!StringUtils.hasText(newPassword) || prePassword.equals(newPassword)) {
            errorCode = AccountErrorCode.INVALID_PASSWORD_REQUEST;
        } else if (!bCryptPasswordEncoder.matches(prePassword, member.getPassword())) {
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
        if (!smsRedisMapper.isExistsCode(phone, prefix)) {
            BaseErrorCode errorCode = SmsErrorCode.EXPIRED_AUTH_CODE;
            log.warn("인증번호 유효성 검사 실패: {}", errorCode);
            throw new GlobalErrorException(errorCode);
        }

        if (!smsRedisMapper.isCorrectCode(phone, code, prefix)) {
            BaseErrorCode errorCode = SmsErrorCode.INVALID_AUTH_CODE;
            log.warn("인증번호 유효성 검사 실패: {}", errorCode);
            throw new GlobalErrorException(errorCode);
        }
    }
}
