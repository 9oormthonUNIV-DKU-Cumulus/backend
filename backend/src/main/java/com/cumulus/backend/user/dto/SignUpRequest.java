package com.cumulus.backend.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class SignUpRequest {
    @NotBlank(message = "사용자 이름은 필수입니다.")
    private String userName;

    @NotBlank(message = "사용자 이메일은 필수입니다.")
    @Pattern(
            regexp = "^[A-Za-z0-9._%+-]+@dankook\\.ac\\.kr$",
            message = "단국대학교 이메일(@dankook.ac.kr)만 사용할 수 있습니다."
    )
    private String email;

    @NotBlank(message = "사용자 비밀번호는 필수입니다.")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*?_~-]).{8,}$",
            message = "비밀번호는 8자 이상, 대소문자, 숫자, 특수문자를 포함해야 합니다."
    )
    private String password;

    private String major;

    @Pattern(
            regexp = "^\\d{10,11}$",
            message = "전화번호는 숫자만 입력하며 10~11자리여야 합니다."
    )
    private String phoneNumber;
}
