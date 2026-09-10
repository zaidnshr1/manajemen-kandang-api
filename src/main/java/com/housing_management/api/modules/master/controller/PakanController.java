package com.housing_management.api.modules.master.controller;

import com.housing_management.api.common.base.ApiResponse;
import com.housing_management.api.modules.master.dto.PakanDTO;
import com.housing_management.api.modules.master.service.PakanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pakan")
@RequiredArgsConstructor
public class PakanController {

    private final PakanService pakanService;

    @PostMapping
    public ResponseEntity<ApiResponse<PakanDTO.PakanResponse>> create(@Valid @RequestBody PakanDTO.PakanRequest request) {
        PakanDTO.PakanResponse response = pakanService.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Berhasil menambahkan jenis pakan baru", response));
    }

    @PutMapping("/{id}/restock")
    public ResponseEntity<ApiResponse<PakanDTO.PakanResponse>> restock(@PathVariable Long id, @Valid @RequestBody PakanDTO.RestockPakanRequest request) {
        PakanDTO.PakanResponse repsonse = pakanService.restock(id, request);
        return ResponseEntity.ok(ApiResponse.success("Berhasil memperbarui stok pakan", repsonse));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PakanDTO.PakanResponse>>> getAll() {
        List<PakanDTO.PakanResponse> responses = pakanService.getAll();
        return ResponseEntity.ok(ApiResponse.success("Berhasil mengambil data pakan", responses));
    }
}
