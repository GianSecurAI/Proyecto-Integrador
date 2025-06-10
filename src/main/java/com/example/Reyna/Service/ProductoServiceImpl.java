package com.example.Reyna.Service;

import com.example.Reyna.model.Producto;
import com.example.Reyna.dao.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductoServiceImpl implements ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Override
    public List<Producto> obtenerTodos() {
        return productoRepository.findAll();
    }
    @Override
    public Producto guardarProducto(Producto producto) {
        return productoRepository.save(producto);
    }
}