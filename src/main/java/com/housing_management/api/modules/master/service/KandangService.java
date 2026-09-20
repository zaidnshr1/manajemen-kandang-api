package com.housing_management.api.modules.master.service;

import com.housing_management.api.common.exception.DuplicateResourceException;
import com.housing_management.api.common.exception.ResourceNotFoundException;
import com.housing_management.api.common.util.SecurityUtils;
import com.housing_management.api.modules.master.dto.KandangDTO;
import com.housing_management.api.modules.master.entity.Kandang;
import com.housing_management.api.modules.master.repository.KandangRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KandangService {

    private final KandangRepository kandangRepository;

    @Transactional
    public KandangDTO.KandangResponse create(KandangDTO.KandangRequest request) {
        if (kandangRepository.existsByKodeKandang(request.kodeKandang())) {
            throw new DuplicateResourceException("Kode kandang '" + request.kodeKandang() + "' sudah digunakan");
        }

        Kandang kandang = Kandang.builder()
                .kodeKandang(request.kodeKandang())
                .namaKandang(request.namaKandang())
                .kapasitas(request.kapasitas())
                .build();

        Kandang saved = kandangRepository.save(kandang);
        return mapToResponse(saved);
    }

    @Transactional(readOnly = true)
    public KandangDTO.KandangResponse getById(Long id) {
        Kandang kandang = kandangRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Kandang", "id", id));
        return mapToResponse(kandang);
    }

    @Transactional(readOnly = true)
    public List<KandangDTO.KandangResponse> getAll() {
        return kandangRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public KandangDTO.KandangResponse getByKodeKandang(String kodeKandang) {
        Kandang kandang = kandangRepository.findByKodeKandang(kodeKandang).orElseThrow(() -> new ResourceNotFoundException("Kandang", "kode", kodeKandang));
        return mapToResponse(kandang);
    }

    @Transactional(readOnly = true)
    public List<KandangDTO.KandangResponse> getAllMyKandang() {
        Long currentOwnerId = SecurityUtils.getCurrentUserId();

        return kandangRepository.findAllByOwnerId(currentOwnerId).stream()
                .map(this::mapToResponse)
                .toList();
    }

    private KandangDTO.KandangResponse mapToResponse(Kandang entity) {
        return new KandangDTO.KandangResponse(
                entity.getId(),
                entity.getKodeKandang(),
                entity.getNamaKandang(),
                entity.getKapasitas(),
                entity.getOwnerId(),
                entity.getCreatedAt()
        );
    }
}
