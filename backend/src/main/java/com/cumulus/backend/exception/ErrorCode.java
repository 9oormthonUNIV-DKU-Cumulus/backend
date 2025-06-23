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

    // 로그인 인증필터관련 에러
    USER_EMAIL_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 이메일로 등록된 계정을 찾을 수 없습니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "이메일 또는 비밀번호가 일치하지 않습니다." ),
    AUTH_UNKNOWN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "인증 서비스 오류입니다. 관리자에게 문의하세요."),



    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버내부 문제가 발생했습니다." )
    ;

    private final HttpStatus status;
    private final String message;
}
