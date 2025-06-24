package com.cumulus.backend.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EmailVerifyRequest {
    @NotBlank(message = "인증용 이메일은 반드시 입력해야합니다.")
    @Pattern(
            regexp = "^[A-Za-z0-9._%+-]+@dankook\\.ac\\.kr$",
            message = "단국대학교 이메일(@dankook.ac.kr)만 사용할 수 있습니다."
    )
    private String email;

    @NotBlank
    private String code;
}
