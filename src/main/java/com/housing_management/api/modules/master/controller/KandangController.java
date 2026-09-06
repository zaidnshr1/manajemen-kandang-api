package com.housing_management.api.modules.master.controller;

import com.housing_management.api.common.base.ApiResponse;
import com.housing_management.api.modules.master.dto.KandangDTO;
import com.housing_management.api.modules.master.service.KandangService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/kandang")
@RequiredArgsConstructor
public class KandangController {

    private final KandangService kandangService;

    @PostMapping
    public ResponseEntity<ApiResponse<KandangDTO.Response>> create(@Valid @RequestBody KandangDTO.Request request) {
        KandangDTO.Response response = kandangService.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Berhasil menambahkan kandang baru", response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<KandangDTO.Response>> getById(@PathVariable Long id) {
        KandangDTO.Response response = kandangService.getById(id);
        return ResponseEntity.ok(ApiResponse.success("Data kandang ditemukan", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<KandangDTO.Response>>> getAll() {
        List<KandangDTO.Response> responses =kandangService.getAll();
        return ResponseEntity.ok(ApiResponse.success("Berhasil mengambil seluruh data", responses));
    }

    @GetMapping("/{kodeKandang}")
    public ResponseEntity<ApiResponse<KandangDTO.Response>> getByKodeKandang(@PathVariable String kodeKandang) {
        KandangDTO.Response response = kandangService.getByKodeKandang(kodeKandang);
        return ResponseEntity.ok(ApiResponse.success("Data kandang ditemukan", response));
    }
}
