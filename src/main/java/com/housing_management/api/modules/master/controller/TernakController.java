package com.housing_management.api.modules.master.controller;

import com.housing_management.api.common.base.ApiResponse;
import com.housing_management.api.common.base.PageResponse;
import com.housing_management.api.modules.master.dto.TernakDTO;
import com.housing_management.api.modules.master.service.TernakService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ternak")
@RequiredArgsConstructor
public class TernakController {

    private final TernakService ternakService;

    @PostMapping
    public ResponseEntity<ApiResponse<TernakDTO.Response>> create(@Valid @RequestBody TernakDTO.CreateRequest request) {
        TernakDTO.Response response = ternakService.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Berhasil registrasi ternak baru", response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TernakDTO.Response>> getById(@PathVariable Long id) {
        TernakDTO.Response response = ternakService.getById(id);
        return ResponseEntity.ok(ApiResponse.success("Data ternak ditemukan", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<TernakDTO.Response>>> getAllActive(
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        PageResponse<TernakDTO.Response> responses = ternakService.getAllActive(pageable);
        return ResponseEntity.ok(ApiResponse.success("Berhasil mengambil daftar ternak aktif", responses));
    }
}
