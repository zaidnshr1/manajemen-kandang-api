package com.housing_management.api.modules.auth.controller;

import com.housing_management.api.common.base.ApiResponse;
import com.housing_management.api.modules.auth.dto.AuthDTO;
import com.housing_management.api.modules.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(@Valid @RequestBody AuthDTO.RegisterRequest request) {
        authService.register(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Registrasi berhasil, silakan login dengan kredensial Anda"));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthDTO.TokenResponse>> login(@Valid @RequestBody AuthDTO.LoginRequest request) {
        AuthDTO.TokenResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success("Login berhasil", response));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<AuthDTO.TokenResponse>> refreshToken(
            @Valid @RequestBody AuthDTO.RefreshTokenRequest request) {
        AuthDTO.TokenResponse response = authService.refreshToken(request);
        return ResponseEntity.ok(ApiResponse.success("Access Token berhasil diperbarui", response));
    }
}
