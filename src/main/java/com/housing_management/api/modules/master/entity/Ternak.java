package com.housing_management.api.modules.master.entity;

import com.housing_management.api.common.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "ternak")
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter @Builder
public class Ternak extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "kode_tag", length = 50)
    private String kodeTag;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kategori_id", nullable = false)
    private Kategori kategori;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kandang_id", nullable = false)
    private Kandang kandang;

    @Column(name = "tanggal_masuk", nullable = false)
    private LocalDate tanggalMasuk;

    @Column(name = "jumlah_populasi", nullable = false)
    private Integer jumlahPopulasi;

    @Column(name = "bobot_awal", precision = 8, scale = 2)
    private BigDecimal bobotAwal;

    @Enumerated(EnumType.STRING)
    private StatusTernak status;
}
