package com.housing_management.api.modules.master.service;

import com.housing_management.api.common.base.PageResponse;
import com.housing_management.api.common.exception.BusinessLogicException;
import com.housing_management.api.common.exception.InsufficientPopulasiKandangException;
import com.housing_management.api.common.exception.ResourceNotFoundException;
import com.housing_management.api.modules.master.dto.TernakDTO;
import com.housing_management.api.modules.master.entity.Kandang;
import com.housing_management.api.modules.master.entity.Kategori;
import com.housing_management.api.modules.master.entity.StatusTernak;
import com.housing_management.api.modules.master.entity.Ternak;
import com.housing_management.api.modules.master.repository.KandangRepository;
import com.housing_management.api.modules.master.repository.KategoriRepository;
import com.housing_management.api.modules.master.repository.TernakRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;


@Service
@RequiredArgsConstructor
public class TernakService {

    private final TernakRepository ternakRepository;
    private final KategoriRepository kategoriRepository;
    private final KandangRepository kandangRepository;

    private TernakDTO.Response toResponse(Ternak entity) {
        return new TernakDTO.Response(
                entity.getId(),
                entity.getKodeTag(),
                entity.getKategori().getNamaKategori(),
                entity.getKategori().getTipePencatatan().name(),
                entity.getKandang().getKodeKandang(),
                entity.getTanggalMasuk(),
                entity.getJumlahPopulasi(),
                entity.getBobotAwal(),
                entity.getStatus()
        );
    }

    @Transactional
    public TernakDTO.Response create(TernakDTO.CreateRequest request) {

        Kandang kandang = kandangRepository.findById(request.kandangId()).orElseThrow(() -> new ResourceNotFoundException("Kandang", "id", request.kandangId()));
        Integer populasiKandangSaatIni = ternakRepository.countActivePopulasiByKandangId(kandang.getId());
        if(populasiKandangSaatIni == null) {
            populasiKandangSaatIni = 0;
        }
        if(populasiKandangSaatIni + request.jumlahPopulasi() > kandang.getKapasitas()) {
            throw new InsufficientPopulasiKandangException(kandang.getNamaKandang(), kandang.getKapasitas(), populasiKandangSaatIni, request.jumlahPopulasi());
        }

        Kategori kategori = kategoriRepository.findById(request.kategoriId()).orElseThrow(() -> new ResourceNotFoundException("KategoriTernak", "id", request.kategoriId()));

        Ternak ternak = Ternak.builder()
                .kodeTag(request.kodeTag())
                .kategori(kategori)
                .kandang(kandang)
                .tanggalMasuk(request.tanggalMasuk())
                .jumlahPopulasi(request.jumlahPopulasi())
                .bobotAwal(request.bobotAwal())
                .status(StatusTernak.AFKIR)
                .build();

        Ternak saved = ternakRepository.save(ternak);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public TernakDTO.Response getById(Long id) {
        Ternak ternak = ternakRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ternak", "id", id));
        return toResponse(ternak);
    }

    @Transactional(readOnly = true)
    public PageResponse<TernakDTO.Response> getAllActive(Pageable pageable) {
        Page<Ternak> page = ternakRepository.findAllByStatusWithDetails(StatusTernak.AKTIF, pageable);
        Page<TernakDTO.Response> dtoPage = page.map(this::toResponse);
        return PageResponse.from(dtoPage);
    }

    @Transactional
    public TernakDTO.Response kurangiPopulasiDanAmbilData(Long id, Integer jumlah) {
        Object[] result = ternakRepository.kurangiPopulasiDanAmbilData(id, jumlah).orElseThrow(() -> new BusinessLogicException("Gagal: Ternak tidak ditemukan atau jumlah populasi tidak mencukupi"));

        Long ternakId = ((Number) result[0]).longValue();
        String kodeTag = (String) result[1];
        String namaKategori = (String) result[2];
        String tipePencatatan = (String) result[3];
        String kodeKandang = (String) result[4];
        LocalDate tanggalMasuk = result[5] != null ? ((java.sql.Date) result[5]).toLocalDate() : null;
        Integer sisaPopulasi = ((Number) result[6]).intValue();
        BigDecimal bobotAwal = (BigDecimal) result[7];
        StatusTernak status = StatusTernak.valueOf((String) result[8]);

        return new TernakDTO.Response(
                ternakId,
                kodeTag,
                namaKategori,
                tipePencatatan,
                kodeKandang,
                tanggalMasuk,
                sisaPopulasi,
                bobotAwal,
                status
        );
    }
}
