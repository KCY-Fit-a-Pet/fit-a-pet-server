package com.kcy.fitapet.global.common.resolver.access;

import com.kcy.fitapet.global.common.util.cookie.CookieUtil;
import com.kcy.fitapet.global.common.util.jwt.AuthConstants;
import com.kcy.fitapet.global.common.util.jwt.JwtUtil;
import com.kcy.fitapet.global.common.util.jwt.exception.AuthErrorCode;
import com.kcy.fitapet.global.common.util.jwt.exception.AuthErrorException;
import jakarta.servlet.http.Cookie;
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
    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(AccessTokenInfo.class) != null
                && parameter.getParameterType().equals(AccessToken.class);
    }

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
            accessToken = jwtUtil.resolveToken(httpServletRequest.getHeader(AuthConstants.AUTH_HEADER.getValue()));

            if (!StringUtils.hasText(accessToken)) {
                log.error("Access Token is empty");
                throw new AuthErrorException(AuthErrorCode.EMPTY_ACCESS_TOKEN, "access token is empty");
            }
        } else {
            accessToken = reissuedAccessToken;
            isReissued = true;
        }

        Long userId = jwtUtil.getUserIdFromToken(accessToken);
        LocalDateTime expiryDate = jwtUtil.getExpiryDate(accessToken);
        log.info("access token expiryDate : {}", expiryDate);

        return AccessToken.of(accessToken, userId, expiryDate, isReissued);
    }
}
