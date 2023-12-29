package com.kcy.fitapet.global.common.security.jwt.dto;

import com.kcy.fitapet.domain.member.type.RoleType;

public interface JwtSubInfo {
    Long id();
    RoleType role();
    String phoneNumber();
}
