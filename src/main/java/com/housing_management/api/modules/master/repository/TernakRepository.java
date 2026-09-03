package com.housing_management.api.modules.master.repository;

import com.housing_management.api.modules.master.entity.StatusTernak;
import com.housing_management.api.modules.master.entity.Ternak;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TernakRepository extends JpaRepository<Ternak, Long> {

    @Query(value = "SELECT t FROM Ternak t " +
                    "JOIN FETCH t.kategori " +
                    "JOIN FETCH t.kandang " +
                    "WHERE t.id = :id")
    Optional<Ternak> findByIdWithDetails(@Param("id") Long id);

    @Query(value = "SELECT t FROM Ternak t " +
                   "FETCH JOIN t.kategori " +
                   "FETCH JOIN t.kandang " +
                   "WHERE t.status = :status",
           countQuery = "SELECT count(t) FROM Ternak t WHERE t.status = :status")
    Page<Ternak> findAllByStatusWithDetails(@Param("status")StatusTernak status, Pageable pageable);

    @Query(value = "SELECT COALESCE(SUM(t.jumlahPopulasi), 0) FROM Ternak t " +
                    "WHERE t.kandang.id  :kandangId AND t.status = 'AKTIF'")
    Integer countActivePopulasiByKandangId(@Param("kandangId")Long kandangId);
}
