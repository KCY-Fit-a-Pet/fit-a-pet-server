package kr.co.fitapet.domain.domains.member.service;


import kr.co.fitapet.common.annotation.DomainService;
import kr.co.fitapet.domain.domains.member.domain.Member;
import kr.co.fitapet.domain.domains.member.domain.MemberNickname;
import kr.co.fitapet.domain.domains.member.dto.MemberInfo;
import kr.co.fitapet.domain.domains.member.exception.AccountErrorCode;
import kr.co.fitapet.domain.domains.member.exception.AccountErrorException;
import kr.co.fitapet.domain.domains.member.repository.MemberNicknameRepository;
import kr.co.fitapet.domain.domains.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@DomainService
@RequiredArgsConstructor
public class MemberSearchService {
    private final MemberRepository memberRepository;
    private final MemberNicknameRepository memberNicknameRepository;

    @Transactional(readOnly = true)
    public Member findById(Long id) {
        return memberRepository.findByIdOrElseThrow(id);
    }

    @Transactional(readOnly = true)
    public MemberInfo findMemberInfo(Long requesterId, String target) {
        return memberRepository.findMemberInfo(requesterId, target).orElseThrow(
                () -> new AccountErrorException(AccountErrorCode.NOT_FOUND_MEMBER_ERROR)
        );
    }

    @Transactional(readOnly = true)
    public List<MemberInfo> findMemberInfos(List<Long> ids, Long requesterId) {
        return memberRepository.findMemberInfos(ids, requesterId);
    }

    @Transactional(readOnly = true)
    public Member findByUid(String uid) {
        return memberRepository.findByUid(uid).orElseThrow(
                () -> new AccountErrorException(AccountErrorCode.NOT_FOUND_MEMBER_ERROR)
        );
    }

    @Transactional(readOnly = true)
    public Member findByPhone(String phone) {
        return memberRepository.findByPhone(phone).orElseThrow(
                () -> new AccountErrorException(AccountErrorCode.NOT_FOUND_MEMBER_ERROR)
        );
    }

    @Transactional(readOnly = true)
    public List<Long> findMyPetIds(Long memberId) {
        return memberRepository.findMyPetIds(memberId);
    }

    @Transactional(readOnly = true)
    public boolean isExistByUidOrPhone(String uid, String phone) {
        return memberRepository.existsByUidOrPhone(uid, phone);
    }

    @Transactional(readOnly = true)
    public boolean isExistById(Long id) {
        return memberRepository.existsById(id);
    }

    @Transactional(readOnly = true)
    public boolean isExistByPhone(String phone) {
        return memberRepository.existsByPhone(phone);
    }

    @Transactional(readOnly = true)
    public boolean isExistByUid(String uid) {
        return memberRepository.existsByUid(uid);
    }

    @Transactional(readOnly = true)
    public boolean isExistByPhoneAndUid(String phone, String uid) {
        return memberRepository.existsByPhoneAndUid(phone, uid);
    }

    @Transactional(readOnly = true)
    public boolean isExistNicknameByFromAndTo(Long from, Long to) {
        return memberNicknameRepository.existsByFrom_IdAndTo_Id(from, to);
    }

    @Transactional(readOnly = true)
    public MemberNickname findNicknameByFromAndTo(Long from, Long to) {
        return memberNicknameRepository.findByFrom_IdAndTo_Id(from, to).orElseThrow(
                () -> new AccountErrorException(AccountErrorCode.NOT_FOUND_NICKNAME_ERROR)
        );
    }

}
