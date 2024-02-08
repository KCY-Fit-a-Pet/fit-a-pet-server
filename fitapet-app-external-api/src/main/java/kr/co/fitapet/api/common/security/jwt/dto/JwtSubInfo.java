package kr.co.fitapet.api.common.security.jwt.dto;


import kr.co.fitapet.domain.domains.member.type.RoleType;

import java.math.BigInteger;

public interface JwtSubInfo {
    Long id();
    String oauthId();
    RoleType role();
    String phoneNumber();
}
