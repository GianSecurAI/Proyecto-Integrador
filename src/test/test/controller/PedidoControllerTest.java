package com.example.Reyna.controller;

import com.example.Reyna.dao.PedidoRepository;
import com.example.Reyna.dao.UserRepository;
import com.example.Reyna.dao.ProductoRepository;
import com.example.Reyna.model.User;
import com.example.Reyna.model.Producto;
import com.example.Reyna.dto.PedidoRequest;
import com.example.Reyna.dto.PedidoRequest.ProductoDTO;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PedidoController.class)
public class PedidoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PedidoRepository pedidoRepository;

    @MockBean
    private UserRepository usuarioRepository;

    @MockBean
    private ProductoRepository productoRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCrearPedido() throws Exception {
        // Simular cliente
        User cliente = new User();
        cliente.setId(1L);

        // Simular producto
        Producto producto = new Producto();
        producto.setId(10L);

        // Construir DTO de producto
        ProductoDTO productoDTO = new ProductoDTO();
        productoDTO.setId_producto(10L);
        productoDTO.setCantidad(2);
        productoDTO.setPrecioUnitario(15.0);
        productoDTO.setSubtotal(30.0);

        PedidoRequest request = new PedidoRequest();
        request.setId_cliente(1L);
        request.setTotal(30.0);
        request.setProductos(List.of(productoDTO));

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(productoRepository.findById(10L)).thenReturn(Optional.of(producto));

        mockMvc.perform(post("/api/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Pedido registrado correctamente"));

        Mockito.verify(pedidoRepository).save(any());
    }
}
