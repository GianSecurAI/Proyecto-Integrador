package com.example.Reyna.model;

import com.example.Reyna.dao.DetallePedidoRepository;
import com.example.Reyna.dao.PedidoRepository;
import com.example.Reyna.dao.ProductoRepository;
import com.example.Reyna.dao.UserRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class DetallePedidoRepositoryTest {

    @Autowired
    private DetallePedidoRepository detallePedidoRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testGuardarDetallePedido() {
        // Crear y guardar cliente
        User cliente = new User();
        cliente.setNombre("Carlos");
        cliente.setCorreo("carlos@example.com");
        cliente.setContraseña("123"); // No relevante para test
        userRepository.save(cliente);

        // Crear y guardar producto
        Producto producto = new Producto();
        producto.setNombre("Mouse Gamer");
        producto.setPrecio(new BigDecimal("99.99"));
        productoRepository.save(producto);

        // Crear y guardar pedido
        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setFechaPedido(LocalDateTime.now());
        pedido.setEstadoPedido("Pendiente");
        pedido.setTotal(new BigDecimal("199.98"));
        pedidoRepository.save(pedido);

        // Crear detalle pedido
        DetallePedido detalle = new DetallePedido();
        detalle.setPedido(pedido);
        detalle.setProducto(producto);
        detalle.setCantidad(2);
        detalle.setPrecioUnitario(producto.getPrecio());
        detalle.setSubtotal(producto.getPrecio().multiply(BigDecimal.valueOf(2)));

        // Guardar y verificar
        DetallePedido guardado = detallePedidoRepository.save(detalle);
        assertThat(guardado.getIdDetalle()).isNotNull();
        assertThat(guardado.getCantidad()).isEqualTo(2);
        assertThat(guardado.getProducto().getNombre()).isEqualTo("Mouse Gamer");
    }
}
