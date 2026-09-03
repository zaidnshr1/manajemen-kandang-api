package com.housing_management.api.modules.operasional.repository;

import com.housing_management.api.modules.operasional.entity.LogPenimbangan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LogPenimbanganRepository extends JpaRepository<LogPenimbangan, Long> {

    List<LogPenimbangan> findByTernakIdOrderBytanggalTimbangDesc(Long ternakId);

    Optional<LogPenimbangan> findTopByTernakIdOrderByTanggalTimbangDesc(Long ternakId);

    Optional<LogPenimbangan> findTopByTernakIdOrderByTanggalTimbangAsc(Long ternakId);
}
