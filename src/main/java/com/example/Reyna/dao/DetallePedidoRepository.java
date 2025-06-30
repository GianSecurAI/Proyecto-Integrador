package com.example.Reyna.dao;

import com.example.Reyna.model.DetallePedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Long> {
    // Puedes agregar métodos personalizados si lo necesitas
}
