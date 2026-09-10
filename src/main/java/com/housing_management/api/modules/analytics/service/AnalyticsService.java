package com.housing_management.api.modules.analytics.service;

import com.housing_management.api.common.exception.BusinessLogicException;
import com.housing_management.api.modules.analytics.dto.AnalyticsDTO;
import com.housing_management.api.modules.master.dto.KandangDTO;
import com.housing_management.api.modules.master.dto.TernakDTO;
import com.housing_management.api.modules.master.service.KandangService;
import com.housing_management.api.modules.master.service.TernakService;
import com.housing_management.api.modules.operasional.dto.OperasionalDto;
import com.housing_management.api.modules.operasional.service.OperasionalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final TernakService ternakService;
    private final KandangService kandangService;
    private final OperasionalService operasionalService;

    @Transactional(readOnly = true)
    public AnalyticsDTO.AdgResponse calculateADG(Long ternakId) {
        TernakDTO.TernakResponse ternak = ternakService.getById(ternakId);
        OperasionalDto.PenimbanganResponse penimbanganTerakhir = operasionalService.getPenimbanganTerakhir(ternakId);

        long totalHari = ChronoUnit.DAYS.between(ternak.tanggalMasuk(), penimbanganTerakhir.tanggalTimbang());
        if (totalHari <= 0) {
            totalHari = 1;
        }

        BigDecimal selisihBobot = penimbanganTerakhir.bobot().subtract(ternak.bobotAwal());
        BigDecimal adg = selisihBobot.divide(BigDecimal.valueOf(totalHari), 2, RoundingMode.HALF_UP);
        return new AnalyticsDTO.AdgResponse(
                ternakId,
                ternak.kodeTag(),
                ternak.bobotAwal(),
                penimbanganTerakhir.bobot(),
                totalHari,
                adg
        );
    }

    @Transactional(readOnly = true)
    public AnalyticsDTO.FcrResponse calculateFCR(Long kandangId, LocalDate  startDate, LocalDate endDate) {
        KandangDTO.KandangResponse kandang = kandangService.getById(kandangId);

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

        BigDecimal totalPakan = operasionalService.getTotalPakanByKandangAndPeriode(kandangId, startDateTime, endDateTime);
        BigDecimal totalKenaikanBobot = operasionalService.getTotalKenaikanBobotByKandangAndPeriode(kandangId, startDateTime, endDateTime);

        if (totalKenaikanBobot == null || totalKenaikanBobot.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessLogicException("Total kenaikan bobot harus lebih dari 0 untuk menghitung FCR");
        }

        BigDecimal fcrScore = totalPakan.divide(totalKenaikanBobot, 2, RoundingMode.HALF_UP);

        return new AnalyticsDTO.FcrResponse(
                kandangId,
                kandang.kodeKandang(),
                totalPakan,
                totalKenaikanBobot,
                fcrScore
        );
    }


}
