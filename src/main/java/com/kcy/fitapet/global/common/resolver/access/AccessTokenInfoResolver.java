package com.kcy.fitapet.global.common.resolver.access;

import com.kcy.fitapet.global.common.security.jwt.AuthConstants;
import com.kcy.fitapet.global.common.security.jwt.JwtProvider;
import com.kcy.fitapet.global.common.security.jwt.dto.JwtSubInfo;
import com.kcy.fitapet.global.common.security.jwt.exception.AuthErrorCode;
import com.kcy.fitapet.global.common.security.jwt.exception.AuthErrorException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.time.LocalDateTime;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
@Component
public class AccessTokenInfoResolver implements HandlerMethodArgumentResolver {
    private final JwtProvider jwtProvider;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(AccessTokenInfo.class) != null
                && parameter.getParameterType().equals(AccessToken.class);
    }

    /**
     * 요청받은 액세스 토큰 정보를 추출하여 AccessToken 객체에 담아 반환한다.
     * 단, 인증 과정에서 재발급이 일어난 경우에는 헤더에 담긴 재발급된 액세스 토큰을 추출한다.
     * 재발급된 액세스 토큰 추출 시, AccessToken 객체의 isReissued 필드를 true로 설정된다.
     */
    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory) {
        final var httpServletRequest = (HttpServletRequest) webRequest.getNativeRequest();
        final var httpServletResponse = (HttpServletResponse) webRequest.getNativeResponse();
        boolean isReissued = false;

        String reissuedAccessToken = httpServletResponse.getHeader(AuthConstants.REISSUED_ACCESS_TOKEN.getValue());

        String accessToken;
        if (!StringUtils.hasText(reissuedAccessToken)) {
            accessToken = jwtProvider.resolveToken(httpServletRequest.getHeader(AuthConstants.AUTH_HEADER.getValue()));

            if (!StringUtils.hasText(accessToken)) {
                log.error("Access Token is empty");
                throw new AuthErrorException(AuthErrorCode.EMPTY_ACCESS_TOKEN, "access token is empty");
            }
        } else {
            accessToken = reissuedAccessToken;
            isReissued = true;
        }

        JwtSubInfo subs = jwtProvider.getSubInfoFromToken(accessToken);
        LocalDateTime expiryDate = jwtProvider.getExpiryDate(accessToken);
        log.info("access token expiryDate : {}", expiryDate);

        return AccessToken.of(accessToken, subs.id(), expiryDate, isReissued);
    }
}
