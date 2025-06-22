package com.cumulus.backend.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // Spring security 인증 & 인가 실패
    NOT_LOGIN_USER(HttpStatus.UNAUTHORIZED, "로그인하지 않은 사용자입니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "접근권한이 없습니다."),



    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버내부 문제가 발생했습니다." );

    private final HttpStatus status;
    private final String message;
}
