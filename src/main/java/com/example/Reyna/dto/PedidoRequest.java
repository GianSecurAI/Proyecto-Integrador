package com.example.Reyna.dto;

import java.math.BigDecimal;
import java.util.List;

public class PedidoRequest {
    private Long id_cliente;
    private List<ProductoDTO> productos;
    private BigDecimal total;

    // getters y setters
    public Long getId_cliente() {
        return id_cliente;
    }
    public void setId_cliente(Long id_cliente) {
        this.id_cliente = id_cliente;
    }
    public List<ProductoDTO> getProductos() {
        return productos;
    }
    public void setProductos(List<ProductoDTO> productos) {
        this.productos = productos;
    }
    public BigDecimal getTotal() {
        return total;
    }
    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public static class ProductoDTO {
        private Long id_producto;
        private Integer cantidad;
        private BigDecimal precioUnitario;
        private BigDecimal subtotal;
        // getters y setters
        public Long getId_producto() {
            return id_producto;
        }
        public void setId_producto(Long id_producto) {
            this.id_producto = id_producto;
        }
        public Integer getCantidad() {
            return cantidad;
        }
        public void setCantidad(Integer cantidad) {
            this.cantidad = cantidad;
        }
        public BigDecimal getPrecioUnitario() {
            return precioUnitario;
        }
        public void setPrecio_unitario(BigDecimal precioUnitario) {
            this.precioUnitario = precioUnitario;
        }
        public BigDecimal getSubtotal() {
            return subtotal;
        }
        public void setSubtotal(BigDecimal subtotal) {
            this.subtotal = subtotal;
        }

    }
}
