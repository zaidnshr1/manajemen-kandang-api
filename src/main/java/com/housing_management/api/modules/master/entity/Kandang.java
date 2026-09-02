package com.housing_management.api.modules.master.entity;

import com.housing_management.api.common.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "kandang")
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter @Builder
public class Kandang extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "nama_kandang", length = 100)
    private String namaKandang;

    @Column(nullable = false, name = "kodeKandang", length = 50)
    private String kodeKandang;

    @Column(nullable = false, name = "kapasitas")
    private Integer kapasitas;
}
