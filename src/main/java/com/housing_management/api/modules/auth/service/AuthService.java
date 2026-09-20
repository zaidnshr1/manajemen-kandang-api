package com.housing_management.api.modules.auth.service;

import com.housing_management.api.common.exception.BusinessLogicException;
import com.housing_management.api.common.exception.DuplicateResourceException;
import com.housing_management.api.common.exception.ResourceNotFoundException;
import com.housing_management.api.config.security.JwtTokenProvider;
import com.housing_management.api.modules.auth.dto.AuthDTO;
import com.housing_management.api.modules.auth.entity.RefreshToken;
import com.housing_management.api.modules.auth.entity.Role;
import com.housing_management.api.modules.auth.entity.User;
import com.housing_management.api.modules.auth.repository.RefreshTokenRepository;
import com.housing_management.api.modules.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final RefreshTokenService refreshTokenService;

    @Transactional
    public void register(AuthDTO.RegisterRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new DuplicateResourceException("Username sudah terdaftar");
        }
        if (userRepository.existsByEmail(request.email())) {
            throw new DuplicateResourceException("Email sudah terdaftar");
        }

        User user = User.builder()
                .email(request.email())
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.ROLE_USER)
                .build();

        userRepository.save(user);
    }

    @Transactional
    public AuthDTO.TokenResponse login(AuthDTO.LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new ResourceNotFoundException("username tidak ditemukan"));

        String accessToken = tokenProvider.generateToken(
                user.getId(),
                user.getUsername(),
                user.getRole().name()
        );
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());


        return new AuthDTO.TokenResponse(
                accessToken,
                refreshToken.getToken(),
                "Bearer",
                user.getUsername(),
                user.getRole().name()
        );
    }

    @Transactional
    public AuthDTO.TokenResponse refreshToken(AuthDTO.RefreshTokenRequest request) {
        return refreshTokenRepository.findByToken(request.refreshToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String newAccessToken = tokenProvider.generateToken(user.getId(), user.getUsername(), user.getRole().name());
                    RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(user.getId());

                    return new AuthDTO.TokenResponse(
                            newAccessToken,
                            newRefreshToken.getToken(),
                            "Bearer",
                            user.getUsername(),
                            user.getRole().name()
                    );
                })
                .orElseThrow(() -> new BusinessLogicException("refresh token tidak valid atau tidak ditemukan"));
    }
}
