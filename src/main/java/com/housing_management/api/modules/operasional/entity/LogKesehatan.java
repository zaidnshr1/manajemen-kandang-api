package com.housing_management.api.modules.operasional.entity;

import com.housing_management.api.common.base.BaseEntity;
import com.housing_management.api.modules.master.entity.Ternak;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "log_kesehatan")
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter @Builder
public class LogKesehatan extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "ternak_id")
    private Ternak ternak;

    @Column(nullable = false, name = "tanggal")
    private LocalDate tanggal;

    @Enumerated(EnumType.STRING)
    @Column(name = "jenis_kejadian", nullable = false, length = 30)
    private JenisKejadian jenisKejadian;

    @Column(nullable = false, name = "jumlah_terdampak")
    private Integer jumlahTerdampak;

    @Column(name = "catatan", columnDefinition = "TEXT")
    private String catatan;

    @Column(name = "biaya", precision = 12, scale = 2)
    private BigDecimal biaya;
}
