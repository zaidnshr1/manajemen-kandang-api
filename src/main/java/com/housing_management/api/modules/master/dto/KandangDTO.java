package com.housing_management.api.modules.master.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.OffsetDateTime;

public class KandangDTO {

    public record Request (

        @NotBlank(message = "Tidak boleh kosong")
        @Size(max = 50, message = "Maksimum 50 karakter")
        String kodeKandang,

        @NotBlank(message = "Tidak boleh kosong")
        @Size(max = 100, message = "Maksimum 100 karakter")
        String namaKandang,

        @NotNull(message = "Tidak boleh kosong")
        @Min(value = 1, message = "Minimal 1")
        Integer kapasitas
    ) {}

    public record Response(
            Long id,
            String kodeKandang,
            String namaKandang,
            Integer kapasitas,
            Long ownerId,
            OffsetDateTime createdAt
    ) {}
}
