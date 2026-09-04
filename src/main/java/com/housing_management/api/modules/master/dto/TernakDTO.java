package com.housing_management.api.modules.master.dto;

import com.housing_management.api.modules.master.entity.StatusTernak;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TernakDTO {

    public record CreateRequest(
            String kodeTag,

            @NotNull(message = "Tidak boleh kosong")
            Long kategoriId,

            @NotNull(message = "Tidak boleh kosong")
            Long kandangId,

            @NotNull(message = "Tidak boleh kosong")
            @PastOrPresent(message = "Efektif hari ini atau lampau")
            LocalDate tanggalMasuk,

            @NotNull(message = "Tidak boleh kosong")
            @Min(value = 1, message = "Isi minimal 1")
            Integer jumlahPopulasi,

            @NotNull(message = "Tidak boleh kosong")
            @Positive(message = "Harus lebih dari 0")
            BigDecimal bobotAwal
    ) {}

    public record Response(
            Long id,
            String kodeTag,
            String namaKategori,
            String tipePencatatan,
            String kodeKandang,
            LocalDate tanggalMasuk,
            Integer jumlahPopulasi,
            BigDecimal bobotAwal,
            StatusTernak status
    ) {}
}
