package com.housing_management.api.modules.master.repository;

import com.housing_management.api.modules.master.entity.Kandang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KandangRepository extends JpaRepository<Kandang, Long> {
    boolean existsByKodeKandang(String kodeKandang);
    Optional<Kandang> findByKodeKandang(String kodeKandang);}
