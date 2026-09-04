package com.housing_management.api.modules.operasional.dto;

import com.housing_management.api.modules.operasional.entity.JenisKejadian;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

public class OperasionalDto {

    public record PenimbanganRequest(
            @NotNull(message = "Tidak boleh kosong")
            Long ternakId,

            @NotNull(message = "Tidak boleh kosong")
            @PastOrPresent(message = "Efektif hari ini atau lampau")
            LocalDate tanggalTimbang,

            @NotNull(message = "Tidak boleh kosong")
            @Positive(message = "Harus lebih dari 0")
            BigDecimal bobot
    ) {}

    public record PenimbanganResponse(
            Long id,
            Long ternakId,
            String kodeTag,
            LocalDate tanggalTimbang,
            BigDecimal bobot,
            OffsetDateTime createdAt
    ) {}

    public record PemberianPakanRequest(
            @NotNull(message = "Tidak boleh kosong")
            Long kandangId,
            @NotNull(message = "Tidak boleh kosong")
            Long pakanId,
            @NotNull(message = "Tidak boleh kosong")
            @PastOrPresent(message = "Efektif hari ini atau lampau")
            LocalDateTime tanggal,
            @NotNull(message = "Tidak boleh kosong")
            @Positive(message = "Harus lebih dari 0")
            BigDecimal jumlahPakai
    ) {}

    public record PemberianPakanResponse(
            Long id,
            Long kandangId,
            String namaKandang,
            Long pakanId,
            String namaPakan,
            LocalDateTime tanggal,
            BigDecimal jumlahPakai,
            String satuan,
            OffsetDateTime createdAt
    ) {}

    public record KesehatanRequest(
            @NotNull(message = "Tidak boleh kosong")
            Long ternakId,

            @NotNull(message = "Tidak boleh kosong")
            @PastOrPresent(message = "Efektif hari ini atau lampau")
            LocalDate tanggal,

            @NotNull(message = "Tidak boleh kosong")
            JenisKejadian jenisKejadian,

            @NotNull(message = "Tidak boleh kosong")
            @Min(value = 1, message = "Isi minimal 1")
            Integer jumlahTerdampak,

            String catatan,
            BigDecimal biaya
    ) {}

    public record KesehatanResponse(
            Long id,
            Long ternakId,
            String kodeTag,
            LocalDate tanggal,
            JenisKejadian jenisKejadian,
            Integer jumlahTerdampak,
            String catatan,
            BigDecimal biaya,
            OffsetDateTime createdAt
    ) {}
}
