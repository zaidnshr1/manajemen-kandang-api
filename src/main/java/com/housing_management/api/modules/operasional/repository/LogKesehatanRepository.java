package com.housing_management.api.modules.operasional.repository;

import com.housing_management.api.modules.operasional.entity.LogKesehatan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogKesehatanRepository extends JpaRepository<LogKesehatan, Long> {
}
