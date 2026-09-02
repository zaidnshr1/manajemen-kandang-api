package com.housing_management.api.modules.operasional.entity;

import com.housing_management.api.modules.master.entity.Ternak;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "log_kesehatan")
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter @Builder
public class LogKesehatan {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "ternak_id")
    private Ternak ternak;

    
}
