package com.example.Reyna.Service;

import com.example.Reyna.model.Producto;
import java.util.List;

public interface ProductoService {
    List<Producto> obtenerTodos();

    Producto guardarProducto(Producto producto);
}