package com.cumulus.backend.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReissueRequest {
    @NotBlank(message = "재발급을 위해 refreshToken은 반드시 필요합니다.")
    private String refreshToken;
}
