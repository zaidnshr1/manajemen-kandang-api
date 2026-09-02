package com.housing_management.api.modules.master.entity;

import com.housing_management.api.common.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "pakan")
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter @Builder
public class Pakan extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "nama_pakan", length = 100)
    private String namaPakan;

    @Column(nullable = false, name = "satuan", length = 25)
    private String satuan;

    @Column(nullable = false, name = "stok", precision = 10, scale = 2)
    private BigDecimal stok;

    @Column(nullable = false, name = "harga_persatuan", precision = 12, scale = 12)
    private BigDecimal hargaPerSatuan;
}
