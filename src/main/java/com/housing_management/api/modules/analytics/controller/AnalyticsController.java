package com.housing_management.api.modules.analytics.controller;

import com.housing_management.api.common.base.ApiResponse;
import com.housing_management.api.modules.analytics.dto.AnalyticsDTO;
import com.housing_management.api.modules.analytics.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping("/adg/{ternakId}")
    public ResponseEntity<ApiResponse<AnalyticsDTO.AgdResponse>> getADG(@PathVariable Long ternakId) {
        AnalyticsDTO.AgdResponse response = analyticsService.calculateADG(ternakId);
        return ResponseEntity.ok(ApiResponse.success("Kalkulasi ADG berhasil", response));
    }

    @GetMapping("/fcr")
    public ResponseEntity<ApiResponse<AnalyticsDTO.FcrResponse>> getFCR(
            @RequestParam Long kandangId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        AnalyticsDTO.FcrResponse response = analyticsService.calculateFCR(kandangId, startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success("Kalkulasi FCR berhasil", response));
    }
}