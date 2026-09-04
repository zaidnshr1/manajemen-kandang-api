package com.housing_management.api.modules.master.repository;

import com.housing_management.api.modules.master.entity.Pakan;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface PakanRepository extends JpaRepository<Pakan, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "SELECT p FROM Pakan p WHERE p.id = :id")
    Optional<Pakan> findByIdWithLock(@Param("id") Long id);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE pakan " +
                    "SET stok = stok - :jumlah " +
                    "WHERE id = :id AND stok >= :jumlah " +
                    "RETURNING id, nama_pakan, stok, satuan, harga_per_satuan", nativeQuery = true)
    Optional<Object[]> kurangiStokDanKembalikanNilai(@Param("id") Long id,
                    @Param("jumlah")BigDecimal jumlah);
}
