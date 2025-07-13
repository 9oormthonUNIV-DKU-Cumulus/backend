package com.cumulus.backend.mypage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserInfoDto {
    private String userName;
    private String email;
    private String major;
    private String phoneNumber;
}
