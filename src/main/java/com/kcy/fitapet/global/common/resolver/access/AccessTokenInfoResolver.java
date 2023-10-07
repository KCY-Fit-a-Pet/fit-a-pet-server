package com.kcy.fitapet.global.common.resolver.access;

import com.kcy.fitapet.global.common.util.jwt.AuthConstants;
import com.kcy.fitapet.global.common.util.jwt.JwtUtil;
import com.kcy.fitapet.global.common.util.jwt.exception.AuthErrorCode;
import com.kcy.fitapet.global.common.util.jwt.exception.AuthErrorException;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
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

import java.util.Date;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
@Component
public class AccessTokenInfoResolver implements HandlerMethodArgumentResolver {
    private final JwtUtil jwtUtil;

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
        String accessToken = jwtUtil.resolveToken(httpServletRequest.getHeader(AuthConstants.AUTH_HEADER.getValue()));

        if (!StringUtils.hasText(accessToken)) {
            log.error("Access Token is empty");
            throw new AuthErrorException(AuthErrorCode.EMPTY_ACCESS_TOKEN, "빈 토큰입니다");
        }

        Long userId = jwtUtil.getUserIdFromToken(accessToken);
        Date expiryDate = jwtUtil.getExpiryDate(accessToken);

        return new AccessToken(accessToken, userId, expiryDate);
    }

}
