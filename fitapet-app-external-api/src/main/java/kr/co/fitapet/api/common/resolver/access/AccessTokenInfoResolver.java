package kr.co.fitapet.api.common.resolver.access;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.fitapet.api.common.security.jwt.consts.AuthConstants;
import kr.co.fitapet.api.common.security.jwt.JwtProvider;
import kr.co.fitapet.api.common.security.jwt.dto.JwtSubInfo;
import kr.co.fitapet.api.common.security.jwt.exception.AuthErrorCode;
import kr.co.fitapet.api.common.security.jwt.exception.AuthErrorException;
import kr.co.fitapet.domain.domains.member.domain.AccessToken;
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
     */
    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory) {
        final var httpServletRequest = (HttpServletRequest) webRequest.getNativeRequest();

        String accessToken = jwtProvider.resolveToken(httpServletRequest.getHeader(AuthConstants.AUTH_HEADER.getValue()));
        if (!StringUtils.hasText(accessToken)) {
            log.error("Access Token is empty");
            throw new AuthErrorException(AuthErrorCode.EMPTY_ACCESS_TOKEN, "access token is empty");
        }

        JwtSubInfo subs = jwtProvider.getSubInfoFromToken(accessToken);
        LocalDateTime expiryDate = jwtProvider.getExpiryDate(accessToken);
        log.info("access token expiryDate : {}", expiryDate);

        return AccessToken.of(accessToken, subs.id(), expiryDate);
    }
}
