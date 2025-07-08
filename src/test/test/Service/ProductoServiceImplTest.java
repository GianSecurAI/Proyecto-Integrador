package com.example.Reyna.Service;

import com.example.Reyna.dao.ProductoRepository;
import com.example.Reyna.model.Categoria;
import com.example.Reyna.model.Producto;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductoServiceImplTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoServiceImpl productoService;

    private Producto producto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        producto = new Producto();
        producto.setId_producto(1L);
        producto.setNombre_producto("Coca Cola");
        producto.setCodigo("P001");
        producto.setPrecio(3.5);
        producto.setStock(10);
        producto.setMarca("Coca Cola");
        producto.setDescripcion("Bebida gaseosa");
        producto.setContenido("500ml");
        producto.setEstado(true);

        Categoria categoria = new Categoria();
        categoria.setId_categoria(1L);
        categoria.setNombre_categoria("Bebidas");

        producto.setCategoria(categoria);
    }

    @Test
    void testObtenerTodos() {
        when(productoRepository.findAll()).thenReturn(Arrays.asList(producto));

        List<Producto> productos = productoService.obtenerTodos();

        assertEquals(1, productos.size());
        verify(productoRepository, times(1)).findAll();
    }

    @Test
    void testGuardarProducto_Nuevo() {
        when(productoRepository.findByCodigo("P001")).thenReturn(null);
        when(productoRepository.save(producto)).thenReturn(producto);

        Producto guardado = productoService.guardarProducto(producto);

        assertNotNull(guardado);
        assertEquals("Coca Cola", guardado.getNombre_producto());
        verify(productoRepository).save(producto);
    }

    @Test
    void testGuardarProducto_ExistenteSumaStock() {
        Producto existente = new Producto();
        existente.setId_producto(1L);
        existente.setCodigo("P001");
        existente.setStock(5);

        when(productoRepository.findByCodigo("P001")).thenReturn(existente);
        when(productoRepository.save(any(Producto.class))).thenReturn(existente);

        producto.setStock(10); // nuevo producto

        Producto actualizado = productoService.guardarProducto(producto);

        assertEquals(15, actualizado.getStock());
        verify(productoRepository).save(existente);
    }

    @Test
    void testActualizarProducto_Existe() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);

        Producto detalles = new Producto();
        detalles.setNombre_producto("Pepsi");
        detalles.setPrecio(3.0);
        detalles.setCategoria(producto.getCategoria());
        detalles.setMarca("PepsiCo");
        detalles.setStock(20);
        detalles.setContenido("600ml");
        detalles.setDescripcion("Otra bebida");

        Producto actualizado = productoService.actualizarProducto(1L, detalles);

        assertEquals("Pepsi", actualizado.getNombre_producto());
        assertEquals(20, actualizado.getStock());
    }

    @Test
    void testActualizarProducto_NoExiste() {
        when(productoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            productoService.actualizarProducto(1L, producto);
        });
    }

    @Test
    void testEliminarProducto_Existe() {
        when(productoRepository.existsById(1L)).thenReturn(true);

        productoService.eliminarProducto(1L);

        verify(productoRepository).deleteById(1L);
    }

    @Test
    void testEliminarProducto_NoExiste() {
        when(productoRepository.existsById(1L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> {
            productoService.eliminarProducto(1L);
        });
    }
}
