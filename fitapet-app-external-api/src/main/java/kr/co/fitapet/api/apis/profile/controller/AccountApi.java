package kr.co.fitapet.api.apis.profile.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import kr.co.fitapet.api.apis.profile.usecase.MemberAccountUseCase;
import kr.co.fitapet.api.common.response.SuccessResponse;
import kr.co.fitapet.api.common.security.authentication.CustomUserDetails;
import kr.co.fitapet.domain.common.redis.sms.type.SmsPrefix;
import kr.co.fitapet.domain.domains.member.dto.AccountProfileRes;
import kr.co.fitapet.api.apis.profile.dto.AccountSearchReq;
import kr.co.fitapet.api.apis.profile.dto.ProfilePatchReq;
import kr.co.fitapet.domain.domains.member.dto.UidRes;
import kr.co.fitapet.domain.domains.member.type.MemberAttrType;
import kr.co.fitapet.domain.domains.notification.type.NotificationType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@Tag(name = "프로필 API")
@RestController
@Slf4j
@RequestMapping("/api/v2/accounts")
@RequiredArgsConstructor
public class AccountApi {
    private final MemberAccountUseCase memberAccountUseCase;

    @Operation(summary = "프로필 조회")
    @Parameter(name = "id", description = "조회할 프로필 ID", in = ParameterIn.PATH, required = true)
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated() and #id == principal.userId")
    public ResponseEntity<?> getProfile(@PathVariable("id") Long id) {
        AccountProfileRes member = memberAccountUseCase.getProfile(id);
        return ResponseEntity.ok(SuccessResponse.from(member));
    }

    @Operation(summary = "프로필 검색")
    @Parameter(name = "search", description = "검색할 닉네임", in = ParameterIn.QUERY, required = true)
    @GetMapping("")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getSearchProfile(@RequestParam("search") @NotBlank String search) {

        return null;
    }

    @Operation(summary = "닉네임 존재 확인")
    @Parameter(name = "uid", description = "확인할 유저 닉네임", in = ParameterIn.QUERY, required = true)
    @GetMapping("/exists")
    @PreAuthorize("isAnonymous()")
    public ResponseEntity<?> getExistsUid(@RequestParam("uid") @NotBlank String uid) {
        boolean exists = memberAccountUseCase.existsUid(uid);
        return ResponseEntity.ok(SuccessResponse.from(Map.of("valid", exists)));
    }

    @Operation(summary = "프로필(비밀번호/이름) 수정")
    @Parameters({
            @Parameter(name = "id", description = "수정할 프로필 ID", in = ParameterIn.PATH, required = true),
            @Parameter(name = "type", description = "수정할 프로필 타입", in = ParameterIn.QUERY, required = true),
    })
    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated() and #id == principal.userId")
    public ResponseEntity<?> putProfile(
            @PathVariable Long id,
            @RequestParam("type") MemberAttrType type,
            @RequestBody @Valid ProfilePatchReq req
    ) {
        log.info("type: {}", type);
        memberAccountUseCase.updateProfile(id, req, type);

        return ResponseEntity.ok(SuccessResponse.noContent());
    }

    @Operation(summary = "ID/PW 찾기")
    @Parameters({
            @Parameter(name = "type", description = "찾을 타입", example = "uid/password", in = ParameterIn.QUERY, required = true),
            @Parameter(name = "code", description = "인증번호", in = ParameterIn.QUERY, required = true),
    })
    @PostMapping("/search")
    @PreAuthorize("isAnonymous()")
    public ResponseEntity<?> postSearchIdOrPassword(
            @RequestParam("type") @NotBlank SmsPrefix type,
            @RequestParam("code") @NotBlank String code,
            @RequestBody @Valid AccountSearchReq req
    ) {
        if (type.equals(SmsPrefix.UID)) {
            UidRes res = memberAccountUseCase.getUidWhenSmsAuthenticated(req.phone(), code, type);
            return ResponseEntity.ok(SuccessResponse.from(res));
        } else {
            memberAccountUseCase.overwritePassword(req, code, type);
            return ResponseEntity.ok(SuccessResponse.noContent());
        }
    }

    @Operation(summary = "알림 on/off")
    @Parameters({
            @Parameter(name = "id", description = "알림 설정할 ID", in = ParameterIn.PATH, required = true),
            @Parameter(name = "type", description = "알림 타입", example = "care or memo or schedule", required = true)
    })
    @GetMapping("/{id}/notify")
    @PreAuthorize("isAuthenticated() and #id == principal.userId")
    public ResponseEntity<?> putNotify(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestParam("type") @NotBlank NotificationType type) {
        memberAccountUseCase.updateNotification(id, user.getUserId(), type);
        return ResponseEntity.ok(SuccessResponse.noContent());
    }

    @Operation(summary = "다른 유저 별명 설정", description = "자기 자신의 별명을 설정하려는 경우 에러 응답을 반환합니다. 별명을 제거하는 경우 null을 입력합니다.")
    @PutMapping("/{member_id}/nickname")
    @PreAuthorize("isAuthenticated() and #memberId != principal.userId")
    public ResponseEntity<?> putNickname(
            @PathVariable("member_id") Long memberId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return null;
    }


    @Operation(summary = "관리 중인 반려동물 날짜별 스케줄 전체 조회")
    @GetMapping("/{user_id}/schedules")
    @PreAuthorize("isAuthenticated() and #userId == principal.userId")
    public ResponseEntity<?> getCalendarSchedules(
            @PathVariable("user_id") Long userId,
            @RequestParam(value = "year") Integer year,
            @RequestParam(value = "month") Integer month,
            @RequestParam(value = "day") Integer day
    ) {
        LocalDateTime date = LocalDate.of(year, month, day).atStartOfDay();
        log.info("date: {}", date);
        return ResponseEntity.ok(SuccessResponse.from("schedules", memberAccountUseCase.findPetSchedules(userId, date).getSchedules()));
    }

    @Operation(summary = "관리 중인 반려동물의 모든 메모 카테고리 조회")
    @Parameter(name = "id", description = "조회할 프로필 ID", in = ParameterIn.PATH, required = true)
    @GetMapping("/{id}/memo-categories")
    @PreAuthorize("isAuthenticated() and #id == principal.userId")
    public ResponseEntity<?> getMemoCategories(@PathVariable("id") Long id) {
        return ResponseEntity.ok(SuccessResponse.from("rootMemoCategories", memberAccountUseCase.getMemoCategories(id)));
    }
}
