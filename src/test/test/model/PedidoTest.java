package com.example.Reyna.model;

import com.example.Reyna.dao.PedidoRepository;
import com.example.Reyna.dao.UserRepository;
import com.example.Reyna.dao.ProductoRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class PedidoTest {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Test
    void testGuardarPedidoConDetalles() {
        // 1. Crear cliente
        User cliente = new User();
        cliente.setNombre("Luis");
        cliente.setCorreo("luis@correo.com");
        cliente.setContraseña("clave123");
        userRepository.save(cliente);

        // 2. Crear producto
        Producto producto = new Producto();
        producto.setNombre("Laptop");
        producto.setPrecio(new BigDecimal("1500.00"));
        productoRepository.save(producto);

        // 3. Crear pedido
        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setFechaPedido(LocalDateTime.now());
        pedido.setEstadoPedido("Pendiente");
        pedido.setTotal(new BigDecimal("3000.00"));

        // 4. Crear detalle del pedido
        DetallePedido detalle = new DetallePedido();
        detalle.setPedido(pedido); // relación bidireccional
        detalle.setProducto(producto);
        detalle.setCantidad(2);
        detalle.setPrecioUnitario(producto.getPrecio());
        detalle.setSubtotal(producto.getPrecio().multiply(BigDecimal.valueOf(2)));

        // 5. Asignar detalles al pedido
        pedido.setDetalles(List.of(detalle));

        // 6. Guardar pedido (con cascada se guarda también el detalle)
        Pedido guardado = pedidoRepository.save(pedido);

        // 7. Verificar
        assertThat(guardado.getIdPedido()).isNotNull();
        assertThat(guardado.getDetalles()).hasSize(1);
        assertThat(guardado.getDetalles().get(0).getCantidad()).isEqualTo(2);
    }
}
