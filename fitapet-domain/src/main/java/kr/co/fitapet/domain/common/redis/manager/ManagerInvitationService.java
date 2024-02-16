package kr.co.fitapet.domain.common.redis.manager;

import java.util.AbstractMap;
import java.util.List;

public interface ManagerInvitationService {
    /**
     * 매니저 초대 정보를 저장
     * @param invitedId : 초대 받는 사람 아이디
     * @param petId : 초대할 반려동물 고유번호
     */
    void save(Long invitedId, Long petId);

    /**
     * 초대 정보 조회
     * @param invitedId : 초대 받는 사람 아이디
     * @param petId : 초대할 반려동물 고유번호
     * @return 초대 정보
     */
    public InvitationDto findInvitationInfo(Long invitedId, Long petId);

    /**
     * 반려동물 관리자로 초대한 정보 전체 조회
     * @param petId : 초대할 반려동물 고유번호
     * @return List<InvitationDto> : 초대 정보 리스트
     */
    List<InvitationDto> findAll(Long petId);

    /**
     * 초대 만료 여부 확인
     * @param invitedId : 초대 받는 사람 아이디
     * @param petId : 초대할 반려동물 고유번호
     * @return 존재하면 true, 아니면 false
     */
    boolean expired(Long invitedId, Long petId);

    /**
     * 초대 정보 삭제
     * @param invitedId : 초대 받는 사람 아이디
     * @param petId : 초대할 반려동물 고유번호
     */
    void delete(Long invitedId, Long petId);
}
