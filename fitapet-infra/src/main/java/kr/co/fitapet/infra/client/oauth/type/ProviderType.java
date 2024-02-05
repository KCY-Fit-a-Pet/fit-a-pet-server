package kr.co.fitapet.infra.client.oauth.type;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ProviderType {
    KAKAO("1", "카카오"),
    GOOGLE("2", "구글"),
    NAVER("2", "네이버"),
    APPLE("3", "애플");

    private final String code;
    private final String type;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
