package com.housing_management.api.modules.operasional.entity;

import com.housing_management.api.modules.master.entity.Pakan;
import com.housing_management.api.modules.master.entity.Ternak;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "log_pemberian_pakan")
public class LogPemberianPakan {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "pakan_id")
    private Pakan pakan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "ternak_id")
    private Ternak ternak;

    @Column(nullable = false, name = "jumlah_pakai", precision = 10, scale = 2)
    private BigDecimal jumlahPakai;

    @Column(name = "tanggal", nullable = false)
    private LocalDateTime tanggal;
}
