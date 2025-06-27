package com.example.Reyna.Service;

import com.example.Reyna.model.Producto;
import java.util.List;

public interface ProductoService {
    List<Producto> obtenerTodos();

    Producto guardarProducto(Producto producto);
    Producto actualizarProducto(Long id, Producto producto);

    void eliminarProducto(Long id);
}