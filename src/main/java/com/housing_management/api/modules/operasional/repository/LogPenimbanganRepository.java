package com.housing_management.api.modules.operasional.repository;

import com.housing_management.api.modules.operasional.entity.LogPenimbangan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface LogPenimbanganRepository extends JpaRepository<LogPenimbangan, Long> {

    Optional<LogPenimbangan> findTopByTernakIdOrderByTanggalTimbangDesc(Long ternakId);

    @Query(value = "SELECT COALESCE(SUM(lp.bobot - t.bobotAwal), 0) " +
                    "FROM LogPenimbangan lp JOIN lp.ternak t " +
                    "WHERE t.kandang.id = :kandangId " +
                    "AND lp.tanggalTimbang BETWEEN :startDate AND :endDate " +
                    "AND lp.id IN (" +
                    "SELECT MAX(l2.id) FROM LogPenimbangan l2 " +
                    "WHERE l2.tanggalTimbang BETWEEN :startDate AND :endDate " +
                    "GROUP BY l2.ternak.id" +
                    ")")
    BigDecimal sumKenaikanBobotByKandangAndPeriode(
            @Param("kandangId") Long kandangId,
            @Param("stratDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
            );
}
