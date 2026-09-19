package com.housing_management.api.modules.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AuthDTO {

    public record RegisterRequest (
            @NotBlank(message = "Tidak boleh kosong")
            String username,

            @NotBlank(message = "Tidak boleh kosong")
            @Email(message = "format email salah")
            String email,

            @NotNull(message = "Tidak boleh kosong")
            @Min(value = 6, message = "Minimal 6 karakter")
            String password
    ) {}

    public record LoginRequest (
            @NotBlank(message = "tidak boleh kosong")
            String email,

            @NotBlank(message = "tidak boleh kosong")
            String password
    ) {}

    public record TokenResponse (
            String token,
            String tokenType,
            String username,
            String role
    ) {}
}
