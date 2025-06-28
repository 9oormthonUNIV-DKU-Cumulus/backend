package com.cumulus.backend.activity.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class ActivityApplicationCreateRequestDto {
    @NotNull( message = "모임 신청자 이름은 반드시 입력해야합니다." )
    private String userName;

    @NotNull( message = "모임 신청자 연락처는 반드시 입력해야합니다.")
    private String phoneNumber;

    @NotNull( message = "모임 신청자 소속은 반드시 입력해야합니다." )
    private String major;

    @NotNull( message = "모임 신청글은 반드시 입력해야합니다." )
    @Size(min=10, max=1000, message = "모임 신청글은 10자이상 ~ 1000자이하로 작성해주세요.")
    private String applicationText;
}
