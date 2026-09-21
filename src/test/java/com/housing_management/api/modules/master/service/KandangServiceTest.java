package com.housing_management.api.modules.master.service;

import com.housing_management.api.common.exception.DuplicateResourceException;
import com.housing_management.api.config.security.CustomUserDetails;
import com.housing_management.api.modules.master.dto.KandangDTO;
import com.housing_management.api.modules.master.entity.Kandang;
import com.housing_management.api.modules.master.repository.KandangRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class KandangServiceTest {

    @Mock
    private KandangRepository kandangRepository;

    @InjectMocks
    private KandangService kandangService;

    private Kandang sampleKandang;

    @BeforeEach
    void Setup() {
        sampleKandang = Kandang.builder()
                .id(1L)
                .kodeKandang("KND-SAPI-01")
                .namaKandang("kandang utama")
                .kapasitas(50)
                .build();
        sampleKandang.setOwnerId(100L);
    }

    @Test
    @DisplayName("Sukses membuat kandang baru ketika kode belum terdaftar")
    void createKandang_success() {
        when(kandangRepository.save(any(Kandang.class))).thenAnswer(invocation -> {
            Kandang savedEntity = invocation.getArgument(0);
            savedEntity.setId(1L);
            return savedEntity;
        });

        KandangDTO.KandangRequest request = new KandangDTO.KandangRequest("KND-SAPI-01", "Kandang Utama", 50);
        when(kandangRepository.existsByKodeKandang("KND-SAPI-01")).thenReturn(false);

        KandangDTO.KandangResponse response = kandangService.create(request);

        assertNotNull(response);
        assertEquals("KND-SAPI-01", response.kodeKandang());
        verify(kandangRepository, times(1)).existsByKodeKandang("KND-SAPI-01");
        verify(kandangRepository, times(1)).save(any(Kandang.class));
    }

    @Test
    @DisplayName("Gagal membuat kandang dan melempar DuplicateResourceException jika kode sudah ada")
    void createKandang_DuplicateCode_ThrowsException() {
        KandangDTO.KandangRequest request = new KandangDTO.KandangRequest("KND-SAPI-01", "Kandang Utama", 50);
        when(kandangRepository.existsByKodeKandang("KND-SAPI-01")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> kandangService.create(request));
        verify(kandangRepository, never()).save(any(Kandang.class));
    }

    @Test
    @DisplayName("Sukses mengambil daftar kandang terisolasi milik user yang sedang terautentikasi")
    void getAllMyKandang_Success() {
        CustomUserDetails userDetails = new CustomUserDetails(100L, "peternak_1", "password", List.of(new SimpleGrantedAuthority("ROLE_USER")));
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        try {
            when(kandangRepository.findAllByOwnerId(100L)).thenReturn(List.of(sampleKandang));

            List<KandangDTO.KandangResponse> responses = kandangService.getAllMyKandang();

            assertNotNull(responses);
            assertEquals(1, responses.size());
            assertEquals(100L, responses.get(0).ownerId());
            verify(kandangRepository, times(1)).findAllByOwnerId(100L);
        } finally {
            SecurityContextHolder.clearContext();
        }
    }
}
