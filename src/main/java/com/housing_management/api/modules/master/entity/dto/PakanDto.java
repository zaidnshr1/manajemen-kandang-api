package com.housing_management.api.modules.master.entity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public class PakanDto {

    public record Request (

            @NotBlank(message = "Tidak boleh kosong")
            String namaPakan,

            @NotNull(message = "Tidak boleh kosong")
            @PositiveOrZero(message = "Stok tidak bisa minus")
            BigDecimal stok,

            @NotBlank(message = "Tidak boleh kosong")
            String satuan,

            @NotNull(message = "TIdak boleh kosong")
            @Positive(message = "Harus lebih dari 0")
            BigDecimal hargaPerSatuan
    ) {}

    public record RestockRequest(
            @NotNull(message = "Tidak boleh kosong")
            @Positive(message = "Harus lebih dari 0")
            BigDecimal jumlahTambahan
    ) {}

    public record Response(
            Long id,
            String namaPakan,
            BigDecimal stok,
            String satuan,
            BigDecimal hargaPerSatuan
    ) {}
}
