package kr.co.fitapet.infra.client.oauth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class OIDCPublicKeyResponse {
    List<OIDCPublicKey> keys;

    @Override
    public String toString() {
        return "OIDCPublicKeyResponse{" +
                "keys=" + keys +
                '}';
    }
}
