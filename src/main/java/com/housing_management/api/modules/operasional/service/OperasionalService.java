package com.housing_management.api.modules.operasional.service;

import com.housing_management.api.common.exception.BusinessLogicException;
import com.housing_management.api.common.exception.ResourceNotFoundException;
import com.housing_management.api.modules.master.dto.KandangDTO;
import com.housing_management.api.modules.master.dto.PakanDTO;
import com.housing_management.api.modules.master.dto.TernakDTO;
import com.housing_management.api.modules.master.entity.Kandang;
import com.housing_management.api.modules.master.entity.Pakan;
import com.housing_management.api.modules.master.entity.StatusTernak;
import com.housing_management.api.modules.master.entity.Ternak;
import com.housing_management.api.modules.master.service.KandangService;
import com.housing_management.api.modules.master.service.PakanService;
import com.housing_management.api.modules.master.service.TernakService;
import com.housing_management.api.modules.operasional.dto.OperasionalDto;
import com.housing_management.api.modules.operasional.entity.JenisKejadian;
import com.housing_management.api.modules.operasional.entity.LogKesehatan;
import com.housing_management.api.modules.operasional.entity.LogPemberianPakan;
import com.housing_management.api.modules.operasional.entity.LogPenimbangan;
import com.housing_management.api.modules.operasional.repository.LogKesehatanRepository;
import com.housing_management.api.modules.operasional.repository.LogPemberianPakanRepository;
import com.housing_management.api.modules.operasional.repository.LogPenimbanganRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OperasionalService {

    private final TernakService ternakService;
    private final PakanService pakanService;
    private final KandangService kandangService;
    private final EntityManager entityManager;
    private final LogKesehatanRepository logKesehatanRepository;
    private final LogPemberianPakanRepository logPemberianPakanRepository;
    private final LogPenimbanganRepository logPenimbanganRepository;

    @Transactional
    public OperasionalDto.PenimbanganResponse catatPenimbangan(OperasionalDto.PenimbanganRequest request) {
        TernakDTO.Response ternak = ternakService.getById(request.ternakId());

        if (ternak.status() != StatusTernak.AKTIF) {
            throw new BusinessLogicException("Hanya ternak aktif yang dapat timbang");
        }

        Ternak ternakProxy = entityManager.getReference(Ternak.class, request.ternakId());
        LogPenimbangan log = LogPenimbangan.builder()
                .ternak(ternakProxy)
                .tanggalTimbang(request.tanggalTimbang())
                .bobot(request.bobot())
                .build();

        logPenimbanganRepository.save(log);

        return new OperasionalDto.PenimbanganResponse(
                log.getId(),
                request.ternakId(),
                ternak.kodeTag(),
                request.tanggalTimbang(),
                request.bobot(),
                log.getCreatedAt()
        );
    }

    @Transactional
    private OperasionalDto.PemberianPakanResponse catatPemberianPakan(OperasionalDto.PemberianPakanRequest request) {
        KandangDTO.Response kandang = kandangService.getById(request.kandangId());
        PakanDTO.Response pakan = pakanService.kurangiStokDanAmbilData(request.pakanId(), request.jumlahPakai());

        Pakan pakanProxy = entityManager.getReference(Pakan.class, request.pakanId());
        Kandang kandangProxy = entityManager.getReference(Kandang.class, request.kandangId());
        LogPemberianPakan log = LogPemberianPakan.builder()
                .pakan(pakanProxy)
                .kandang(kandangProxy)
                .jumlahPakai(request.jumlahPakai())
                .tanggal(request.tanggal())
                .build();

        logPemberianPakanRepository.save(log);
        return new OperasionalDto.PemberianPakanResponse(
                log.getId(),
                kandang.id(),
                kandang.namaKandang(),
                pakan.id(),
                pakan.namaPakan(),
                log.getTanggal(),
                request.jumlahPakai(),
                pakan.satuan(),
                log.getCreatedAt()
        );
    }

    @Transactional
    public OperasionalDto.KesehatanResponse catatKesehatan(OperasionalDto.KesehatanRequest request) {
        TernakDTO.Response ternak = ternakService.kurangiPopulasiDanAmbilData(request.ternakId(), request.jumlahTerdampak());

        Ternak ternakProxy = entityManager.getReference(Ternak.class, request.ternakId());
        LogKesehatan log = LogKesehatan.builder()
                .ternak(ternakProxy)
                .tanggal(request.tanggal())
                .jenisKejadian(request.jenisKejadian())
                .jumlahTerdampak(request.jumlahTerdampak())
                .catatan(request.catatan())
                .biaya(request.biaya())
                .build();

        logKesehatanRepository.save(log);
        return new OperasionalDto.KesehatanResponse(
                log.getId(),
                request.ternakId(),
                ternak.kodeTag(),
                log.getTanggal(),
                request.jenisKejadian(),
                request.jumlahTerdampak(),
                request.catatan(),
                request.biaya(),
                log.getCreatedAt()
        );
    }
}
