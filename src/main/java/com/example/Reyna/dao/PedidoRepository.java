package com.example.Reyna.dao;

import com.example.Reyna.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    // Puedes agregar métodos personalizados si lo necesitas
}
