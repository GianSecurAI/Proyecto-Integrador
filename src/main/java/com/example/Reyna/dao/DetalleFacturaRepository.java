package com.example.Reyna.dao;

import com.example.Reyna.model.DetalleFactura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetalleFacturaRepository extends JpaRepository<DetalleFactura, Long> {
    
    // Buscar detalles por ID de factura
    List<DetalleFactura> findByFacturaId(Long facturaId);
    
    // Buscar detalles por código de factura
    List<DetalleFactura> findByFacturaCodigo(String codigo);
}
