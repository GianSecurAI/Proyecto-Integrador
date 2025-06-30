package com.example.Reyna.controller;

import com.example.Reyna.dao.PedidoRepository;
import com.example.Reyna.dao.UserRepository;
import com.example.Reyna.dao.ProductoRepository;
import com.example.Reyna.model.Pedido;
import com.example.Reyna.model.DetallePedido;
import com.example.Reyna.model.User;
import com.example.Reyna.model.Producto;
import com.example.Reyna.dto.PedidoRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController 
@RequestMapping("/api/pedidos")
public class PedidoController {
    @Autowired
    private PedidoRepository pedidoRepository;
    @Autowired
    private UserRepository usuarioRepository;
    @Autowired
    private ProductoRepository productoRepository;

    @PostMapping
    public ResponseEntity<?> crearPedido(@RequestBody PedidoRequest request) {
        User cliente = usuarioRepository.findById(request.getId_cliente()).orElseThrow();
        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setFechaPedido(LocalDateTime.now());
        pedido.setEstadoPedido("Pendiente");
        pedido.setTotal(request.getTotal());

        List<DetallePedido> detalles = new ArrayList<>();
        for (PedidoRequest.ProductoDTO p : request.getProductos()) {
            DetallePedido detalle = new DetallePedido();
            detalle.setPedido(pedido);
            Producto producto = productoRepository.findById(p.getId_producto()).orElseThrow();
            detalle.setProducto(producto);
            detalle.setCantidad(p.getCantidad());
            detalle.setPrecioUnitario(p.getPrecioUnitario());
            detalle.setSubtotal(p.getSubtotal());
            detalles.add(detalle);
        }
        pedido.setDetalles(detalles);
        pedidoRepository.save(pedido);
        return ResponseEntity.ok("Pedido registrado correctamente");
    }

}
