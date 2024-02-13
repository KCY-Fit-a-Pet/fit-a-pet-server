package kr.co.fitapet.api.apis.manager.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.fitapet.api.apis.manager.usecase.ManagerUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "매니저 API", description = "반려동물 관리자 및 어드민의 기능을 제공하는 API")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/pets/{pet_id}/managers")
public class MangerApi {
    private final ManagerUseCase managerUseCase;

    @GetMapping("")
    @PreAuthorize("isAuthenticated() and hasp")
    public ResponseEntity<?> getManagers(

    ) {

    }
}
