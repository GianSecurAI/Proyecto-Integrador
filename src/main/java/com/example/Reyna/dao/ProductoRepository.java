package com.example.Reyna.dao;

import com.example.Reyna.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    Producto findByCodigo(String codigo);
}