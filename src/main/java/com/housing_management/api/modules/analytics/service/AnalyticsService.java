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
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final TernakService ternakService;
    private final KandangService kandangService;
    private final OperasionalService operasionalService;

    @Transactional(readOnly = true)
    public AnalyticsDTO.AgdResponse calculateADG(Long ternakId) {
        TernakDTO.Response ternak = ternakService.getById(ternakId);
        OperasionalDto.PenimbanganResponse penimbanganTerakhir = operasionalService.getPenimbanganTerakhir(ternakId);

        long totalHari = ChronoUnit.DAYS.between(ternak.tanggalMasuk(), penimbanganTerakhir.tanggalTimbang());
        if (totalHari <= 0) {
            totalHari = 1;
        }

        BigDecimal selisihBobot = penimbanganTerakhir.bobot().subtract(ternak.bobotAwal());
        BigDecimal adg = selisihBobot.divide(BigDecimal.valueOf(totalHari), 2, RoundingMode.HALF_UP);
        return new AnalyticsDTO.AgdResponse(
                ternakId,
                ternak.kodeTag(),
                ternak.bobotAwal(),
                penimbanganTerakhir.bobot(),
                totalHari,
                adg
        );
    }

    @Transactional(readOnly = true)
    public AnalyticsDTO.FcrResponse calculateFCR(Long kandangId, LocalDateTime startDate, LocalDateTime endDate) {
        KandangDTO.Response kandang = kandangService.getById(kandangId);

        BigDecimal totalPakan = operasionalService.getTotalPakanByKandangAndPeriode(kandangId, startDate, endDate);
        BigDecimal totalKenaikanBobot = operasionalService.getTotalKenaikanBobotByKandangAndPeriode(kandangId, startDate, endDate);

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
