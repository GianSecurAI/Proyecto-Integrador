package com.example.Reyna.controller;

import com.example.Reyna.Service.ProductoService;
import com.example.Reyna.model.Producto;
import com.example.Reyna.model.Rol;
import com.example.Reyna.model.User;
import com.example.Reyna.util.ExcelExporter;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.io.ByteArrayInputStream;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductoController.class)
public class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductoService productoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllProductos() throws Exception {
        Producto p = new Producto();
        p.setNombre("Teclado");
        Mockito.when(productoService.obtenerTodos()).thenReturn(List.of(p));

        mockMvc.perform(get("/api/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Teclado"));
    }

    @Test
    void testCrearProducto() throws Exception {
        Producto producto = new Producto();
        producto.setNombre("Monitor");
        Mockito.when(productoService.guardarProducto(any(Producto.class))).thenReturn(producto);

        mockMvc.perform(post("/api/productos")
                        .principal(() -> "admin@gmail.com") // simular AuthenticationPrincipal
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(producto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Monitor"));
    }

    @Test
    void testActualizarProducto() throws Exception {
        Producto producto = new Producto();
        producto.setNombre("Mouse actualizado");
        Mockito.when(productoService.actualizarProducto(eq(1L), any(Producto.class))).thenReturn(producto);

        mockMvc.perform(put("/api/productos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(producto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Mouse actualizado"));
    }

    @Test
    void testEliminarProducto() throws Exception {
        Mockito.doNothing().when(productoService).eliminarProducto(1L);

        mockMvc.perform(delete("/api/productos/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testExportarProductosExcel() throws Exception {
        Producto p = new Producto();
        p.setNombre("Excel Test");

        Mockito.when(productoService.obtenerTodos()).thenReturn(List.of(p));
        Mockito.mockStatic(ExcelExporter.class).when(() ->
            ExcelExporter.productosToExcel(anyList())
        ).thenReturn(new ByteArrayInputStream(new byte[] {1, 2, 3}));

        mockMvc.perform(get("/api/productos/excel"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "attachment; filename=productos.xlsx"));
    }

    @Test
    void testSoloParaAdmin_Autorizado() throws Exception {
        User admin = new User();
        Rol rol = new Rol();
        rol.setNombre("ADMINISTRADOR");
        admin.setRol(rol);

        mockMvc.perform(get("/api/productos/admin/solo-admin")
                        .requestAttr("user", admin)) // simula @AuthenticationPrincipal
                .andExpect(status().isOk())
                .andExpect(content().string("¡Bienvenido, administrador!"));
    }

    @Test
    void testSoloParaAdmin_NoAutorizado() throws Exception {
        User cliente = new User();
        Rol rol = new Rol();
        rol.setNombre("CLIENTE");
        cliente.setRol(rol);

        mockMvc.perform(get("/api/productos/admin/solo-admin")
                        .requestAttr("user", cliente))
                .andExpect(status().isForbidden())
                .andExpect(content().string("Acceso denegado"));
    }
}
