package com.kcy.fitapet.domain.member.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/members")
@Slf4j
public class MemberApi {
    @GetMapping("/test")
    public ResponseEntity<?> testApi() {
        log.info("test api");
        return ResponseEntity.ok("hello");
    }
}
