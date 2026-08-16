package com.agroconecta.stock;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Lock;

import jakarta.persistence.LockModeType;

import java.util.Optional;
import java.util.List;

public interface StockRepository extends JpaRepository<Stock, Long> {

    @EntityGraph(attributePaths = "producto")
    List<Stock> findAllByOrderByIdAsc();

    @EntityGraph(attributePaths = "producto")
    Optional<Stock> findById(Long id);

    Optional<Stock> findByUsuarioIdAndProductoId(Long usuarioId, Long productoId);

    boolean existsByProductoId(Long productoId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Stock> findByProductoIdOrderByIdAsc(Long productoId);
}
