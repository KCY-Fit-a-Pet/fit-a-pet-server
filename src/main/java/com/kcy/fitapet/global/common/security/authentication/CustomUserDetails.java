package com.kcy.fitapet.global.common.security.authentication;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kcy.fitapet.domain.member.domain.Member;
import com.kcy.fitapet.domain.member.type.RoleType;
import com.kcy.fitapet.global.common.security.jwt.dto.JwtUserInfo;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;

@Getter
public final class CustomUserDetails implements UserDetails {
    private Long userId;
    private RoleType role;

    // TODO: UserDetails Deserialize 할 때, 필드가 없으면 에러가 나는데, 이를 해결해야 한다.
    @JsonIgnore
    private boolean enabled;
    @JsonIgnore
    private boolean password;
    @JsonIgnore
    private boolean username;
    @JsonIgnore
    private boolean authorities;
    @JsonIgnore
    private boolean credentialsNonExpired;
    @JsonIgnore
    private boolean accountNonExpired;
    @JsonIgnore
    private boolean accountNonLocked;

    private CustomUserDetails() {}

    @Builder
    private CustomUserDetails(Long userId, RoleType role) {
        this.userId = userId;
        this.role = role;
    }

    public static UserDetails of(Member user) {
        return new CustomUserDetails(user.getId(), user.getRole());
    }

    public JwtUserInfo toJwtUserInfo() {
        return JwtUserInfo.of(userId, role);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.stream(RoleType.values())
                .filter(roleType -> roleType == role)
                .map(roleType -> (GrantedAuthority) roleType::name)
                .toList();
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return userId.toString();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}