package com.cumulus.backend.common;

import java.util.List;

public class Constants {
    // 인증필요없는 URL 리스트
    public static List<String> PERMIT_ALL_URLS = List.of(
            "/api/auth/signup",
            "/api/auth/email-code/send",
            "/api/auth/email-code/verify",
            "/api/auth/login",
            "/api/auth/reissue"
    );
}
