package com.kcy.fitapet.domain.member.service.module;

import com.kcy.fitapet.domain.member.dao.ManagerRepository;
import com.kcy.fitapet.domain.member.dao.MemberRepository;
import com.kcy.fitapet.domain.member.domain.Manager;
import com.kcy.fitapet.domain.member.domain.Member;
import com.kcy.fitapet.domain.member.exception.AccountErrorCode;
import com.kcy.fitapet.global.common.response.exception.GlobalErrorException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberSearchService {
    private final MemberRepository memberRepository;
    private final ManagerRepository managerRepository;

    @Transactional(readOnly = true)
    public Member findById(Long id) {
        return memberRepository.findByIdOrElseThrow(id);
    }

    @Transactional(readOnly = true)
    public Member findByUid(String uid) {
        return memberRepository.findByUid(uid).orElseThrow(
                () -> new GlobalErrorException(AccountErrorCode.NOT_FOUND_MEMBER_ERROR)
        );
    }

    @Transactional(readOnly = true)
    public Member findByPhone(String phone) {
        return memberRepository.findByPhone(phone).orElseThrow(
                () -> new GlobalErrorException(AccountErrorCode.NOT_FOUND_MEMBER_ERROR)
        );
    }

    @Transactional(readOnly = true)
    public List<Manager> findAllManagerByMemberId(Long memberId) {
        return managerRepository.findAllByMember_Id(memberId);
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
    public boolean isManager(Long memberId, Long petId) {
        return managerRepository.existsByMember_IdAndPet_Id(memberId, petId);
    }

    @Transactional(readOnly = true)
    public boolean isManagerAll(Long memberId, List<Long> petIds) {
        if (petIds.isEmpty()) {
            return true;
        }

        for (Long petId : petIds) {
            if (!isManager(memberId, petId)) {
                return false;
            }
        }
        return true;
    }
}
