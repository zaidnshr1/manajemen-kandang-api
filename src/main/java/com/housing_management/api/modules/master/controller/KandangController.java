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
    public ResponseEntity<ApiResponse<KandangDTO.KandangResponse>> create(@Valid @RequestBody KandangDTO.KandangRequest request) {
        KandangDTO.KandangResponse response = kandangService.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Berhasil menambahkan kandang baru", response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<KandangDTO.KandangResponse>> getById(@PathVariable Long id) {
        KandangDTO.KandangResponse response = kandangService.getById(id);
        return ResponseEntity.ok(ApiResponse.success("Data kandang ditemukan", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<KandangDTO.KandangResponse>>> getAll() {
        List<KandangDTO.KandangResponse> responses =kandangService.getAll();
        return ResponseEntity.ok(ApiResponse.success("Berhasil mengambil seluruh data", responses));
    }

    @GetMapping("/kode/{kodeKandang}")
    public ResponseEntity<ApiResponse<KandangDTO.KandangResponse>> getByKodeKandang(@PathVariable String kodeKandang) {
        KandangDTO.KandangResponse response = kandangService.getByKodeKandang(kodeKandang);
        return ResponseEntity.ok(ApiResponse.success("Data kandang ditemukan", response));
    }

    @GetMapping("/myKandang")
    public ResponseEntity<ApiResponse<List<KandangDTO.KandangResponse>>> getAllMyKandang() {
        List<KandangDTO.KandangResponse> responses =kandangService.getAllMyKandang();
        return ResponseEntity.ok(ApiResponse.success("Berhasil mengambil seluruh data", responses));
    }
}
