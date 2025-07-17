package com.example.Reyna.model;

import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

@Data
@Entity
@Table(name = "Venta")
public class Boleta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_venta")
    private Long id;

    @Column(name = "id_vendedor")
    private Long idVendedor;

    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    private User cliente;

    @Column(name = "fecha_venta", nullable = false, updatable = false)
    private LocalDateTime fechaVenta;

    @Column(name = "total", nullable = false)
    private double total;

    @Column(name = "tipo_comprobante")
    private String tipoComprobante;

    // Campos adicionales para la boleta que no están en el esquema original
    @Column(name = "codigo", unique = true)
    private String codigo;

    @Column(name = "subtotal")
    private double subtotal;

    @Column(name = "igv")
    private double igv;

    @Column(name = "metodo_entrega")
    private String metodoEntrega; // "tienda" o "delivery"

    @Column(name = "cargo_delivery")
    private double cargoDelivery; // Cargo adicional por delivery

    // Relación con DetalleVenta - Un registro de boleta puede tener múltiples detalles
    @OneToMany(mappedBy = "boleta", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<DetalleVenta> detalles = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        this.fechaVenta = LocalDateTime.now();
        // Tipo de comprobante por defecto
        if (this.tipoComprobante == null) {
            this.tipoComprobante = "BOLETA";
        }
    }

    // Método auxiliar para calcular subtotal e IGV
    public void calcularTotales() {
        this.subtotal = this.total / 1.18; // Asumiendo IGV del 18%
        this.igv = this.total - this.subtotal;
    }

    // Getters y Setters para los nuevos campos
    public String getMetodoEntrega() {
        return metodoEntrega;
    }

    public void setMetodoEntrega(String metodoEntrega) {
        this.metodoEntrega = metodoEntrega;
    }

    public double getCargoDelivery() {
        return cargoDelivery;
    }

    public void setCargoDelivery(double cargoDelivery) {
        this.cargoDelivery = cargoDelivery;
    }
}
