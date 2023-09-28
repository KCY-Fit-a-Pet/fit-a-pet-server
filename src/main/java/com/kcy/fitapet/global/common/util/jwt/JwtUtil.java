package com.kcy.fitapet.global.common.util.jwt;

import com.kcy.fitapet.global.common.util.jwt.entity.JwtUserInfo;
import com.kcy.fitapet.global.common.util.jwt.exception.AuthErrorException;

import java.util.Date;

public interface JwtUtil {
    /**
     * 헤더로부터 토큰을 추출하고 유효성을 검사하는 메서드
     * @param authHeader : 메시지 헤더
     * @return String : 토큰
     * @throws AuthErrorException : 토큰이 유효하지 않을 경우
     */
    String resolveToken(String authHeader) throws AuthErrorException;

    /**
     * 사용자 정보 기반으로 액세스 토큰을 생성하는 메서드
     * @param user UserDto : 사용자 정보
     * @return String : 토큰
     */
    String generateAccessToken(JwtUserInfo user);

    /**
     * 사용자 정보 기반으로 리프레시 토큰을 생성하는 메서드
     * @param user UserDto : 사용자 정보
     * @return String : 토큰
     */
    String generateRefreshToken(JwtUserInfo user);

    /**
     * token으로 부터 사용자 정보를 추출하는 메서드
     * @param token String : 토큰
     * @return UserAuthenticateReq : 사용자 정보
     * @throws AuthErrorException : 토큰이 유효하지 않을 경우
     */
    JwtUserInfo getUserInfoFromToken(String token) throws AuthErrorException;

    /**
     * 토큰으로 부터 유저 아이디를 추출하는 메서드
     * @param token String : 토큰
     * @return Long : 유저 아이디
     * @throws AuthErrorException : 토큰이 유효하지 않을 경우
     */
    Long getUserIdFromToken(String token) throws AuthErrorException;

    /**
     * 토큰의 만료일을 추출하는 메서드
     * @param token String : 토큰
     * @return Date : 만료일
     * @throws AuthErrorException : 토큰이 유효하지 않을 경우
     */
    Date getExpiryDate(String token) throws AuthErrorException;
}