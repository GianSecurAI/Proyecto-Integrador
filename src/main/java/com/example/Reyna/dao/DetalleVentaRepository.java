package com.example.Reyna.dao;

import com.example.Reyna.model.DetalleVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Long> {
    
    // Buscar detalles por ID de boleta
    List<DetalleVenta> findByBoletaId(Long boletaId);
    
    // Buscar detalles por código de boleta
    List<DetalleVenta> findByBoletaCodigo(String codigo);
}
