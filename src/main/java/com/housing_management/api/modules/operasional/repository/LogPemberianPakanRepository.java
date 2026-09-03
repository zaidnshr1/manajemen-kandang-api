package com.housing_management.api.modules.operasional.repository;

import com.housing_management.api.modules.operasional.entity.LogPemberianPakan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface LogPemberianPakanRepository extends JpaRepository<LogPemberianPakan, Long> {

    @Query(value = "SELECT COALESCE(SUM(lp.jumlahPakai), 0) " +
                    "FROM LogPemberianPakan lp " +
                    "WHERE lp.kandang.id = :kandangId " +
                    "AND lp.tanggal BETWEEN :startDate AND :endDate")
    BigDecimal sumJumlahPakaiByKandangAndTanggalBetween(
            @Param("kandangId") Long kandangId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
}
