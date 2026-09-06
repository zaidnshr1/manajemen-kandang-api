package com.housing_management.api.modules.analytics.dto;

import java.math.BigDecimal;

public class AnalyticsDTO {

    public record AgdResponse(
        Long ternakId,
        String kodeTag,
        BigDecimal bobotAwal,
        BigDecimal bobotTerakhir,
        Long totalHari,
        BigDecimal adgKgPerHari
    ) {}

    public record FcrResponse (
            Long kandangId,
            String kodeKandang,
            BigDecimal totalPakanKg,
            BigDecimal totalKenaikanBobotKg,
            BigDecimal fcrScore
    ) {}
}
