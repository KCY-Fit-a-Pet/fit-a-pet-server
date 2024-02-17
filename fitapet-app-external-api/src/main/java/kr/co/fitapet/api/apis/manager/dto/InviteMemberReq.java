package kr.co.fitapet.api.apis.manager.dto;

import jakarta.validation.constraints.NotNull;

public record InviteMemberReq(
        @NotNull
        Long inviteId
) {
}
