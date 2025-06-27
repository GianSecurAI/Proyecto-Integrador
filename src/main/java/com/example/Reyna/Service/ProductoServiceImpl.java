package com.example.Reyna.Service;

import jakarta.persistence.EntityNotFoundException;
import com.example.Reyna.model.Producto;
import com.example.Reyna.dao.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductoServiceImpl implements ProductoService 
{

    @Autowired
    private ProductoRepository productoRepository;

    @Override
    public List<Producto> obtenerTodos() 
    {
        return productoRepository.findAll();
    }
    @Override
    public Producto guardarProducto(Producto producto) {
    Producto existente = productoRepository.findByCodigo(producto.getCodigo());
    if (existente != null) {
        existente.setStock(existente.getStock() + producto.getStock());
        // Puedes actualizar otros campos si lo deseas
        return productoRepository.save(existente);
    } else {
        return productoRepository.save(producto);
    }
    }
    @Override
    public Producto actualizarProducto(Long id, Producto productoDetalles) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con id: " + id));

        producto.setNombre_producto(productoDetalles.getNombre_producto());
        producto.setPrecio(productoDetalles.getPrecio());
        producto.setCategoria(productoDetalles.getCategoria());
        producto.setMarca(productoDetalles.getMarca());
        producto.setStock(productoDetalles.getStock());
        producto.setContenido(productoDetalles.getContenido());
        producto.setDescripcion(productoDetalles.getDescripcion());

        return productoRepository.save(producto);
    }

    @Override
    public void eliminarProducto(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new EntityNotFoundException("Producto no encontrado con id: " + id);
        }
        productoRepository.deleteById(id);
    }
}

    