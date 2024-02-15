package kr.co.fitapet.domain.common.redis.manager;

public interface ManagerInvitationService {
    /**
     * 매니저 초대 정보를 저장
     * @param to : 초대 받는 사람 아이디
     * @param petId : 초대할 반려동물 고유번호
     * @param from : 초대 하는 사람 아이디
     */
    void save(Long to, Long petId, Long from);

    /**
     * 초대 기록 존재 여부 확인
     * @param to : 초대 받는 사람 아이디
     * @param petId : 초대할 반려동물 고유번호
     * @return 존재하면 true, 아니면 false
     */
    boolean exists(Long to, Long petId);

    /**
     * 초대 정보 삭제
     * @param to : 초대 받는 사람 아이디
     * @param petId : 초대할 반려동물 고유번호
     */
    void delete(Long to, Long petId);
}
