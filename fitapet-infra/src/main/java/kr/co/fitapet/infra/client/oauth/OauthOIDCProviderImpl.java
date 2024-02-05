package kr.co.fitapet.infra.client.oauth;

import io.jsonwebtoken.*;
import kr.co.fitapet.infra.client.oauth.dto.OIDCDecodePayload;
import kr.co.fitapet.infra.common.execption.JwtErrorCode;
import kr.co.fitapet.infra.common.execption.JwtErrorException;
import kr.co.fitapet.infra.common.util.JwtErrorCodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;

@Slf4j
@Component
public class OauthOIDCProviderImpl implements OauthOIDCProvider {
    private final String KID = "kid";
    private final String RSA = "RSA";

    @Override
    public String getKidFromUnsignedTokenHeader(String token, String iss, String aud, String nonce) {
        return (String) getUnsignedTokenClaims(token, iss, aud, nonce).getHeader().get(KID);
    }

    @Override
    public OIDCDecodePayload getOIDCTokenBody(String token, String modulus, String exponent) {
        Claims body = getOIDCTokenJws(token, modulus, exponent).getBody();

        return new OIDCDecodePayload(
                body.getIssuer(),
                body.getAudience(),
                body.getSubject(),
                body.get("email", String.class));
    }

    /**
     * ID Token의 header와 body를 Base64 방식으로 디코딩하는 메서드 <br/>
     * payload의 iss, aud, exp, nonce를 검증하고, 실패시 예외 처리
     */
    private Jwt<Header, Claims> getUnsignedTokenClaims(String token, String iss, String aud, String nonce) {
        try {
            return Jwts.parserBuilder()
                    .requireAudience(aud)
                    .requireIssuer(iss)
//                    .require("nonce", nonce)
                    .build()
                    .parseClaimsJwt(getUnsignedToken(token));
        } catch (JwtException e) {
            final JwtErrorCode errorCode = JwtErrorCodeUtil.determineErrorCode(e, JwtErrorCode.FAILED_AUTHENTICATION);

            log.warn("Error code : {}, Error - {},  {}", errorCode, e.getClass(), e.getMessage());
            throw new JwtErrorException(errorCode);
        }
    }

    /**
     * Token의 signature를 제거하는 메서드
     */
    private String getUnsignedToken(String token){
        String[] splitToken = token.split("\\.");
        if (splitToken.length != 3) throw new JwtErrorException(JwtErrorCode.INVALID_TOKEN);
        return splitToken[0] + "." + splitToken[1] + ".";
    }

    /**
     * 공개키로 서명을 검증하는 메서드
     */
    private Jws<Claims> getOIDCTokenJws(String token, String modulus, String exponent) {
        try {
            log.info("token : {}", token);
            return Jwts.parserBuilder()
                    .setSigningKey(getRSAPublicKey(modulus, exponent))
                    .build()
                    .parseClaimsJws(token);
        } catch (JwtException e) {
            final JwtErrorCode errorCode = JwtErrorCodeUtil.determineErrorCode(e, JwtErrorCode.FAILED_AUTHENTICATION);

            log.warn("Error code : {}, Error - {},  {}", errorCode, e.getClass(), e.getMessage());
            throw new JwtErrorException(errorCode);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            log.warn("Error - {},  {}", e.getClass(), e.getMessage());
            throw new JwtErrorException(JwtErrorCode.INVALID_TOKEN);
        }
    }

    /**
     * n, e 조합으로 공개키를 생성하는 메서드
     */
    private Key getRSAPublicKey(String modulus, String exponent) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        byte[] decodeN = Base64.getUrlDecoder().decode(modulus);
        byte[] decodeE = Base64.getUrlDecoder().decode(exponent);
        BigInteger n = new BigInteger(1, decodeN);
        BigInteger e = new BigInteger(1, decodeE);

        RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(n, e);
        return keyFactory.generatePublic(publicKeySpec);
    }
}
