package com.cumulus.backend.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // Spring security 인증 & 인가 실패
    ACCESS_TOKEN_MISSING(HttpStatus.UNAUTHORIZED, "AccessToken이 요청에 포함되어있지 않습니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "접근권한이 없습니다."),

    // 로그인 인증필터관련 에러
    USER_EMAIL_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 이메일로 등록된 계정을 찾을 수 없습니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다." ),
    AUTH_UNKNOWN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "인증 서비스 오류입니다. 관리자에게 문의하세요."),

    // JWT 유효성 검증관련 에러
    EMPTY_TOKEN_ERROR(HttpStatus.BAD_REQUEST, "토큰이 비어있습니다."),
    INVALID_TOKEN_SIGNATURE(HttpStatus.FORBIDDEN, "유효하지 않은 토큰입니다. - jwt 서명검증 실패"),
    MALFORMED_TOKEN_ERROR(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다. - 잘못된 jwt형식"),
    UNSUPPORTED_TOKEN_ERROR(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다. - 지원하지 않는 jwt"),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다. - 만료된 토큰"),
    TOKEN_PARSING_FAILED(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다. - 파싱실패"),

    // refresh token 관련 에러
    REDIS_SAVE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "리프레시 토큰 저장에 실패했습니다."),
    REDIS_DELETE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "리프레스 토큰 삭제에 실패했습니다."),
    REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "Refresh Token이 만료되었거나 유효하지 않습니다. 다시 로그인 해주세요."),
    MISMATCHED_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "저장된 RefreshToken 과 일치하지 않습니다."),

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당유저를 찾을 수 없습니다."),
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 카테고리를 찾을 수 없습니다."),

    // 회원가입 처리 관련 에러
    EMAIL_DUPLICATE(HttpStatus.BAD_REQUEST, "이미 가입된 이메일입니다."),
    EMAIL_VERIFICATION_CODE_INVALID(HttpStatus.BAD_REQUEST, "이메일 인증 코드가 일치하지 않습니다."),
    EMAIL_VERIFICATION_CODE_EXPIRED(HttpStatus.BAD_REQUEST, "인증 코드가 만료되었습니다."),
    EMAIL_NOT_VERIFIED(HttpStatus.BAD_REQUEST, "이메일코드 인증이 완료되지 않았습니다." ),


    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버내부 문제가 발생했습니다." )
    ;

    private final HttpStatus status;
    private final String message;
}
