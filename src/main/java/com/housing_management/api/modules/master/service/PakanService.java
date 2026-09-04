package com.housing_management.api.modules.master.service;

import com.housing_management.api.common.exception.BusinessLogicException;
import com.housing_management.api.common.exception.ResourceNotFoundException;
import com.housing_management.api.modules.master.dto.PakanDTO;
import com.housing_management.api.modules.master.entity.Pakan;
import com.housing_management.api.modules.master.repository.PakanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PakanService {

    private final PakanRepository pakanRepository;

    private PakanDTO.Response mapToResponse(Pakan entity) {
        return new PakanDTO.Response(
                entity.getId(),
                entity.getNamaPakan(),
                entity.getStok(),
                entity.getSatuan(),
                entity.getHargaPerSatuan()
        );
    }
    @Transactional
    public PakanDTO.Response create(PakanDTO.Request request) {
        Pakan pakan = Pakan.builder()
                .namaPakan(request.namaPakan())
                .stok(request.stok())
                .satuan(request.satuan())
                .hargaPerSatuan(request.hargaPerSatuan())
                .build();

        Pakan saved = pakanRepository.save(pakan);
        return mapToResponse(saved);
    }

    @Transactional
    public PakanDTO.Response restock(Long id, PakanDTO.RestockRequest request) {
        Pakan pakan = pakanRepository.findByIdWithLock(id).orElseThrow(() -> new ResourceNotFoundException("pakan", "id", id));

        pakan.setStok(pakan.getStok().add(request.jumlahTambahan()));
        Pakan updated = pakanRepository.save(pakan);
        return mapToResponse(updated);
    }

    @Transactional(readOnly = true)
    public List<PakanDTO.Response> getAll() {
        return pakanRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional
    public PakanDTO.Response kurangiStokDanAmbilData(Long id, BigDecimal jumlah) {
        Object[] result = pakanRepository.kurangiStokDanKembalikanNilai(id, jumlah)
                .orElseThrow(() -> new BusinessLogicException("Gagal: Pakan tidak ditemukan atau stok tidak mencukupi"));
        Long pakanId = ((Number) result[0]).longValue();
        String namaPakan = (String) result[1];
        BigDecimal stokSisa = (BigDecimal) result[2];
        String satuan = (String) result[3];
        BigDecimal harga = (BigDecimal) result[4];

        return new PakanDTO.Response(
                pakanId,
                namaPakan,
                stokSisa,
                satuan,
                harga
        );

    }
}
