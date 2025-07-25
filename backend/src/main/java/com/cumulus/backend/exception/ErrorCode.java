package com.cumulus.backend.exception;

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
    USER_NOT_CLUB_MEMBER(HttpStatus.UNAUTHORIZED, "해당유저는 동아리의 멤버가 아닙니다."),
    USER_NOT_CLUB_LEADER(HttpStatus.UNAUTHORIZED, "해당유저는 동아리의 대표멤버가 아닙니다."),
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 카테고리를 찾을 수 없습니다."),
    CAMPUS_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 캠퍼스는 존재하지 않습니다."),
    ACTIVITY_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 모임을 찾을 수 없습니다."),
    CLUB_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 동아리를 찾을 수 없습니다."),
    CLUB_LEADER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 동아리의 리더정보를 불러올 수 없습니다."),

    // 동아리 처리관련
    DUPLICATE_CLUB_APPLICATION(HttpStatus.BAD_REQUEST, "이미 신청한 동아리를 중복신청할 수 없습니다."),
    ALREADY_CLUB_MEMBER(HttpStatus.BAD_REQUEST, "이미 동아리원일 경우 신청할 수 없습니다."),
    INVALID_APPLICATION_STATUS(HttpStatus.BAD_REQUEST, "동아리 신청상태가 처리가능한 PENDING상태가 아닙니다."),
    NO_PERMISSION_CLUB(HttpStatus.NOT_FOUND, "해당 동아리처리에 대한 권한이 없습니다."),
    NO_PERMISSION_CLUB_APPLICATION(HttpStatus.UNAUTHORIZED,"해당 동아리신청처리에 대한 권한이 없습니다."),

    // 모임 처리관련
    ACTIVTIY_FULL(HttpStatus.BAD_REQUEST, "모임의 모집인원이 가득찼습니다."),
    DUPLICATE_ACTIVITY_APPLICATION(HttpStatus.BAD_REQUEST, "이미 신청한 모임을 중복신청할 수 없습니다."),
    APPLICATION_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 신청내역을 찾을 수 없습니다."),
    NO_PERMISSION_ACTIVITY(HttpStatus.UNAUTHORIZED, "해당 모임처리에 대한 권한이 없습니다."),
    NO_PERMISSION_ACTIVITY_APPLICATION(HttpStatus.UNAUTHORIZED,"해당 모임신청처리에 대한 권한이 없습니다."),

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
