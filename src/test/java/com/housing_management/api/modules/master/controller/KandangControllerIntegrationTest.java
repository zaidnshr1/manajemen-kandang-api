package com.housing_management.api.modules.master.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.housing_management.api.config.security.JwtTokenProvider;
import com.housing_management.api.modules.auth.entity.Role;
import com.housing_management.api.modules.auth.entity.User;
import com.housing_management.api.modules.auth.repository.UserRepository;
import com.housing_management.api.modules.master.dto.KandangDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class KandangControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private String token;

    @BeforeEach
    void setUp() {
        User user = userRepository.save(User.builder()
                .username("test_owner")
                .email("owner@test.com")
                .password("encoded_pass")
                .role(Role.ROLE_USER)
                .build());

        token = jwtTokenProvider.generateToken(user.getId(), user.getUsername(), user.getRole().name());
    }

    @Test
    @DisplayName("Gagal mengakses endpoint terlindungi tanpa Authorization Header (401 Unauthorized)")
    void getKandang_WithoutToken_ReturnsUnauthorized() throws Exception {
        mockMvc.perform(get("/api/v1/kandang"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Sukses membuat kandang baru dengan Authorization Header Bearer Token")
    void createKandang_WithValidToken_ReturnsCreated() throws Exception {
        KandangDTO.KandangRequest request = new KandangDTO.KandangRequest("KND-TEST-01", "Kandang Test", 25);

        mockMvc.perform(post("/api/v1/kandang")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.kodeKandang").value("KND-TEST-01"))
                .andExpect(jsonPath("$.data.ownerId").exists());
    }
}