package com.example.Reyna.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "Venta")
public class Boleta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_venta")
    private Long id;

    @Lob
    @Column(name = "pdf_boleta")
    private byte[] pdf;

    @Column(name = "fecha_venta", nullable = false, updatable = false)
    private LocalDateTime fechaVenta;

    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    private User cliente;

    @OneToMany
    @JoinColumn(name = "id_producto")
    private List<Producto> productos;

    @Column(name = "codigo", nullable = false, unique = true)
    private String codigo;

    @Column(name = "subtotal", nullable = false)
    private double subtotal;

    @Column(name = "igv", nullable = false)
    private double igv;

    @Column(name = "total", nullable = false)
    private double total;

    @PrePersist
    protected void onCreate() {
        this.fechaVenta = LocalDateTime.now();
    }

    // Getters y setters
    public User getCliente() {
        return cliente;
    }

    public void setCliente(User cliente) {
        this.cliente = cliente;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getIgv() {
        return igv;
    }

    public void setIgv(double igv) {
        this.igv = igv;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
