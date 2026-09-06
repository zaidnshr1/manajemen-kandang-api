package com.housing_management.api.modules.operasional.entity;

import com.housing_management.api.common.base.BaseEntity;
import com.housing_management.api.modules.master.entity.Ternak;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "log_penimbangan",
        indexes = {
            @Index(name = "idx_penimbangan_ternak_tanggal", columnList = "ternak_id, tanggal_timbang DESC")
        })
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter @Builder
public class LogPenimbangan extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ternak_id", nullable = false)
    private Ternak ternak;

    @Column(name = "tanggal_timbang", nullable = false)
    private LocalDate tanggalTimbang;

    @Column(name = "bobot", nullable = false, precision = 8, scale = 2)
    private BigDecimal bobot;
}
