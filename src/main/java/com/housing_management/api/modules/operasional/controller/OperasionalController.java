package com.housing_management.api.modules.operasional.controller;

import com.housing_management.api.common.base.ApiResponse;
import com.housing_management.api.modules.operasional.dto.OperasionalDto;
import com.housing_management.api.modules.operasional.service.OperasionalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/operasional")
@RequiredArgsConstructor
public class OperasionalController {

    private final OperasionalService operasionalService;

    @PostMapping("/penimbangan")
    public ResponseEntity<ApiResponse<Void>> catatPenimbangan(@Valid @RequestBody OperasionalDto.PenimbanganRequest request) {
        operasionalService.catatPenimbangan(request);
        return ResponseEntity.ok(ApiResponse.success("Log penimbangan berhasil dicatat"));
    }

    @PostMapping("/pemberian-pakan")
    public ResponseEntity<ApiResponse<Void>> catatPemberianPakan(@Valid @RequestBody OperasionalDto.PemberianPakanRequest request) {
        operasionalService.catatPemberianPakan(request);
        return ResponseEntity.ok(ApiResponse.success("Log pemberian pakan berhasil dicatat dan stok terpotong"));
    }

    @PostMapping("/kesehatan")
    public ResponseEntity<ApiResponse<Void>> catatKesehatan(@Valid @RequestBody OperasionalDto.KesehatanRequest request) {
        operasionalService.catatKesehatan(request);
        return ResponseEntity.ok(ApiResponse.success("Log kesehatan berhasil dicatat"));
    }
}
