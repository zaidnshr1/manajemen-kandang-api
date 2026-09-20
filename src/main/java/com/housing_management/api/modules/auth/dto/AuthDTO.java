package com.housing_management.api.modules.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class AuthDTO {

    public record RegisterRequest (
            @NotBlank(message = "Tidak boleh kosong")
            String username,

            @NotBlank(message = "Tidak boleh kosong")
            @Email(message = "format email salah")
            String email,

            @NotBlank(message = "Tidak boleh kosong")
            String password
    ) {}

    public record LoginRequest (
            @NotBlank(message = "tidak boleh kosong")
            String username,

            @NotBlank(message = "tidak boleh kosong")
            String password
    ) {}

    public record TokenResponse (
            String accessToken,
            String refreshToken,
            String tokenType,
            String username,
            String role
    ) {}

    public record RefreshTokenRequest (
            @NotBlank(message = "tidak boleh kosong")
            String refreshToken
    ) {}
}
