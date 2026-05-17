package com.agroconecta.stock;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.Optional;
import java.util.List;

public interface StockRepository extends JpaRepository<Stock, Long> {

    @EntityGraph(attributePaths = "producto")
    List<Stock> findAllByOrderByIdAsc();

    @EntityGraph(attributePaths = "producto")
    Optional<Stock> findById(Long id);

    Optional<Stock> findByUsuarioIdAndProductoId(Long usuarioId, Long productoId);
}
