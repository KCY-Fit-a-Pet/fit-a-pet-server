package com.kcy.fitapet.global.common.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kcy.fitapet.global.common.util.exception.JwtErrorCodeUtil;
import com.kcy.fitapet.global.common.util.jwt.exception.AuthErrorCode;
import com.kcy.fitapet.global.common.util.jwt.exception.AuthErrorException;
import com.kcy.fitapet.global.common.util.jwt.exception.AuthErrorResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
public class JwtExceptionFilter extends OncePerRequestFilter {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            AuthErrorException exception = JwtErrorCodeUtil.determineAuthErrorException(e);
            log.warn("Exception caught in JwtExceptionFilter: {}", exception.getMessage());

            sendAuthError(response, exception);
        }
    }

    private void sendAuthError(HttpServletResponse response, AuthErrorException e) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(e.getErrorCode().getHttpStatus().value());

        AuthErrorResponse errorResponse = new AuthErrorResponse(e.getErrorCode().name(), e.getErrorCode().getMessage());
        objectMapper.writeValue(response.getWriter(), errorResponse);
    }
}