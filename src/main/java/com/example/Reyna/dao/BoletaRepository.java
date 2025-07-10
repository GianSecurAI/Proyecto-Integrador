package com.example.Reyna.dao;

import com.example.Reyna.model.Boleta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoletaRepository extends JpaRepository<Boleta, Long> {
    // JpaRepository ya incluye el método findById para buscar por ID de boleta

    @Query("SELECT b FROM Boleta b WHERE b.codigo = :codigo")
    Optional<Boleta> findByCodigo(@Param("codigo") String codigo);
}
