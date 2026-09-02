package com.housing_management.api.modules.master.entity;

import com.housing_management.api.common.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "kategori_ternak")
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter @Builder
public class Kategori extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20, name = "nama_kategori")
    private String namaKategori;

    @Column(nullable = false, length = 20, name = "jenis_satuan")
    private String satuanPopulasi;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20, name = "tipe_pencatatan")
    private TipePencatatan tipePencatatan;
}
