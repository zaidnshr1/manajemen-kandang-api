package com.housing_management.api.modules.operasional.entity;

import com.housing_management.api.common.base.BaseEntity;
import com.housing_management.api.modules.master.entity.Kandang;
import com.housing_management.api.modules.master.entity.Pakan;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "log_pemberian_pakan")
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class LogPemberianPakan extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "pakan_id")
    private Pakan pakan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "kandag_id")
    private Kandang kandang;

    @Column(nullable = false, name = "jumlah_pakai", precision = 10, scale = 2)
    private BigDecimal jumlahPakai;

    @Column(name = "tanggal", nullable = false)
    private LocalDateTime tanggal;
}
