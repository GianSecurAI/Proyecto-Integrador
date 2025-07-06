package com.example.Reyna.Service;
import com.example.Reyna.model.Producto;
import java.util.List;
import java.util.Optional;

public interface ProductoService {
    List<Producto> obtenerTodos();
    Optional<Producto> obtenerProductoPorId(Long id);
    Producto guardarProducto(Producto producto);
    Producto actualizarProducto(Long id, Producto producto);

    void eliminarProducto(Long id);
}