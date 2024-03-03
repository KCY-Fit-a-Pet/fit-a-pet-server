package kr.co.fitapet.api.apis.manager.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.co.fitapet.api.apis.manager.dto.InviteMemberReq;
import kr.co.fitapet.api.apis.manager.usecase.ManagerUseCase;
import kr.co.fitapet.api.common.response.SuccessResponse;
import kr.co.fitapet.api.common.security.authentication.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "매니저 API", description = "반려동물 관리자 및 어드민의 기능을 제공하는 API")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/pets/{pet_id}/managers")
public class MangerApi {
    private final ManagerUseCase managerUseCase;

    @Operation(summary = "매니저 목록 조회")
    @Parameter(name = "pet_id", description = "반려동물 ID", in = ParameterIn.PATH, required = true)
    @GetMapping("")
    @PreAuthorize("isAuthenticated() and @managerAuthorize.isManager(principal.userId, #petId)")
    public ResponseEntity<?> getManagers(@PathVariable("pet_id") Long petId, @AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(SuccessResponse.from("managers", managerUseCase.findManagers(user.getUserId(), petId)));
    }

    @Operation(summary = "반려동물 관리자 초대 리스트 조회")
    @Parameter(name = "pet_id", description = "반려동물 ID", in = ParameterIn.PATH, required = true)
    @GetMapping("/invitations")
    @PreAuthorize("isAuthenticated() and @managerAuthorize.isManager(principal.userId, #petId)")
    public ResponseEntity<?> getInvitedMembers(@PathVariable("pet_id") Long petId, @AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(SuccessResponse.from("invitations", managerUseCase.findInvitedMembers(user.getUserId(), petId)));
    }

    // TODO: 2024-02-17 초대 요청 시 해당 유저에게 PUSH 알림을 전송해야 함
    @Operation(summary = "매니저 초대", description = "요청자와 유저 아이디가 동일한 경우 에러 응답을 반환합니다. 초대 요청에 대한 승인 유효 기간은 1일입니다.")
    @Parameter(name = "pet_id", description = "반려동물 ID", in = ParameterIn.PATH, required = true)
    @PostMapping("/invitations")
    @PreAuthorize("isAuthenticated() and not #req.inviteId().equals(principal.userId) and @managerAuthorize.isManager(principal.userId, #petId)")
    public ResponseEntity<?> inviteManager(@PathVariable("pet_id") Long petId, @RequestBody @Valid InviteMemberReq req, @AuthenticationPrincipal CustomUserDetails user)
    {
        managerUseCase.invite(user.getUserId(), req.inviteId(), petId);
        return ResponseEntity.ok(SuccessResponse.noContent());
    }

    @Operation(summary = "매니저 초대 승인", description = "매니저 초대 요청에 대한 승인을 진행합니다. 1일 이내에 승인하지 않으면 초대 요청이 만료됩니다.")
    @Parameters({
            @Parameter(name = "pet_id", description = "반려동물 ID", in = ParameterIn.PATH, required = true),
            @Parameter(name = "invitation_id", description = "초대 ID", in = ParameterIn.PATH, required = true)
    })
    @PutMapping("/invitations/{invitation_id}")
    @PreAuthorize("isAuthenticated() and @managerAuthorize.isInvitedMember(principal.userId, #petId, #invitationId)")
    public ResponseEntity<?> agreeInvite(@PathVariable("pet_id") Long petId, @PathVariable("invitation_id") Long invitationId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        managerUseCase.agreeInvite(userDetails.getUserId(), petId, invitationId);
        return ResponseEntity.ok(SuccessResponse.noContent());
    }

    @Operation(summary = "매니저 초대 취소/거절", description = "매니저 초대 요청에 대한 취소 또는 거절을 진행합니다.")
    @Parameters({
            @Parameter(name = "pet_id", description = "반려동물 ID", in = ParameterIn.PATH, required = true),
            @Parameter(name = "id", description = "초대를 취소/거부할 유저 ID", in = ParameterIn.QUERY, required = true)
    })
    @DeleteMapping("/invitations/{invitation_id}")
    @PreAuthorize("isAuthenticated() " +
            "and ((@managerAuthorize.isManager(principal.userId, #petId) and @managerAuthorize.isInvitePet(#petId, #invitationId))" +
            "or @managerAuthorize.isInvitedMember(principal.userId, #petId, #invitationId))")
    public ResponseEntity<?> cancelInvite(@PathVariable("pet_id") Long petId, @PathVariable("invitation_id") Long invitationId) {
        managerUseCase.cancelInvite(invitationId);
        return ResponseEntity.ok(SuccessResponse.noContent());
    }

    @Operation(summary = "마스터 위임", description = "마스터 권한을 다른 매니저에게 위임합니다.")
    @Parameters({
            @Parameter(name = "pet_id", description = "반려동물 ID", in = ParameterIn.PATH, required = true),
            @Parameter(name = "manager_id", description = "위임할 매니저 ID", in = ParameterIn.PATH, required = true)
    })
    @PatchMapping("/{manager_id}")
    @PreAuthorize("isAuthenticated() and @managerAuthorize.isMaster(principal.userId, #petId) and @managerAuthorize.isManager(#managerId, #petId)")
    public ResponseEntity<?> delegateMaster(
            @PathVariable("pet_id") Long petId,
            @PathVariable("manager_id") Long managerId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        managerUseCase.delegateMaster(userDetails.getUserId(), managerId, petId);
        return ResponseEntity.ok(SuccessResponse.noContent());
    }

    @Operation(summary = "매니저 탈퇴/추방", description = """
            매니저가 반려동물을 탈퇴하거나 마스터가 추방하는 기능을 제공합니다. <br>
            매니저가 반려동물을 탈퇴하면 해당 반려동물의 매니저 목록에서 제거됩니다. <br>
            요청자와 매니저가 동일하지 않은 경우, 마스터 권한이 있는 경우에만 요청이 성공합니다. <br>
            요청자와 매니저가 동일하지만 요청자가 마스터인 경우 요청이 실패합니다. (마스터가 탈퇴하려면 반드시 다른 관리자에게 위임하거나, 반려동물 삭제 API를 사용해야 합니다.)
            """)
    @Parameters({
            @Parameter(name = "pet_id", description = "반려동물 ID", in = ParameterIn.PATH, required = true),
            @Parameter(name = "manager_id", description = "매니저 ID", in = ParameterIn.PATH, required = true)
    })
    @DeleteMapping("/{manager_id}")
    @PreAuthorize("isAuthenticated() and @managerAuthorize.isManager(#managerId, #petId) " +
            "and ((@managerAuthorize.isMaster(principal.userId, #petId) and not #managerId.equals(principal.userId)) " +
            "or (not @managerAuthorize.isMaster(principal.userId, #petId) and @managerAuthorize.isManager(principal.userId, #petId) and #managerId.equals(principal.userId)))")
    public ResponseEntity<?> deleteManager(@PathVariable("pet_id") Long petId, @PathVariable("manager_id") Long managerId) {
        managerUseCase.expelManager(managerId, petId);
        return ResponseEntity.ok(SuccessResponse.noContent());
    }
}
