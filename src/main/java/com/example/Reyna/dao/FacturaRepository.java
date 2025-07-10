package com.example.Reyna.dao;

import com.example.Reyna.model.Factura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, Long> {
    // JpaRepository ya incluye el método findById para buscar por ID de factura

    @Query("SELECT f FROM Factura f WHERE f.codigo = :codigo")
    Optional<Factura> findByCodigo(@Param("codigo") String codigo);
}
