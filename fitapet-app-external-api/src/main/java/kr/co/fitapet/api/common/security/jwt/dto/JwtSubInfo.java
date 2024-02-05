package kr.co.fitapet.api.common.security.jwt.dto;

import com.kcy.fitapet.domain.member.type.RoleType;

import java.math.BigInteger;

public interface JwtSubInfo {
    Long id();
    String oauthId();
    RoleType role();
    String phoneNumber();
}
