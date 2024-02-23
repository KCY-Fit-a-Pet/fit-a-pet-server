package kr.co.fitapet.domain.domains.member.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberAttrType {
    NAME("name"), PASSWORD("password");

    private final String attr;
}
