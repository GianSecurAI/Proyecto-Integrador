package com.example.Reyna.controller;

import com.example.Reyna.dao.FacturaRepository;
import com.example.Reyna.dao.UserRepository;
import com.example.Reyna.dao.ProductoRepository;
import com.example.Reyna.model.Factura;
import com.example.Reyna.model.User;
import com.example.Reyna.model.Producto;

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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FacturaController.class)
public class FacturaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FacturaRepository facturaRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private ProductoRepository productoRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testBuscarFacturaPorCodigo() throws Exception {
        // Simular factura
        Factura factura = new Factura();
        factura.setId(1L);
        factura.setCodigo("FAC-TEST123");
        factura.setRucCliente("20123456789");

        when(facturaRepository.findByCodigo("FAC-TEST123")).thenReturn(Optional.of(factura));

        mockMvc.perform(get("/api/facturas/buscar/codigo")
                        .param("codigo", "FAC-TEST123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codigo").value("FAC-TEST123"))
                .andExpect(jsonPath("$.rucCliente").value("20123456789"));
    }

    @Test
    void testBuscarFacturaPorCodigoNoEncontrada() throws Exception {
        when(facturaRepository.findByCodigo("FAC-NOEXISTE")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/facturas/buscar/codigo")
                        .param("codigo", "FAC-NOEXISTE"))
                .andExpected(status().isNotFound());
    }

    @Test
    void testGuardarFactura() throws Exception {
        // Simular cliente
        User cliente = new User();
        cliente.setId_usuario(1L);
        cliente.setNombre("Juan");

        // Simular producto
        Producto producto = new Producto();
        producto.setId_producto(10L);
        producto.setPrecio(50.0);

        List<Long> productosIds = List.of(10L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(productoRepository.findAllById(productosIds)).thenReturn(List.of(producto));
        when(userRepository.findAll()).thenReturn(List.of(cliente)); // Simular vendedor

        mockMvc.perform(post("/api/facturas/guardar")
                        .param("clienteId", "1")
                        .param("rucCliente", "20123456789")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productosIds)))
                .andExpect(status().isOk());

        Mockito.verify(facturaRepository).save(any());
    }
}
