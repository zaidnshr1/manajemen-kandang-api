package com.housing_management.api.modules.master.repository;

import com.housing_management.api.modules.master.entity.Kategori;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KategoriRepository extends JpaRepository<Kategori, Long> {
}
