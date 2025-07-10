package com.example.Reyna.model;

import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

@Data
@Entity
@Table(name = "Factura")
public class Factura {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_factura")
    private Long id;

    @Column(name = "id_vendedor")
    private Long idVendedor;

    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    private User cliente;

    @Column(name = "fecha_factura", nullable = false, updatable = false)
    private LocalDateTime fechaFactura;

    @Column(name = "total", nullable = false)
    private double total;

    @Column(name = "tipo_comprobante")
    private String tipoComprobante;

    // Campo específico para factura: RUC del cliente
    @Column(name = "ruc_cliente", nullable = false, length = 11)
    private String rucCliente;

    // Campos adicionales para la factura
    @Column(name = "codigo", unique = true)
    private String codigo;

    @Column(name = "subtotal")
    private double subtotal;

    @Column(name = "igv")
    private double igv;

    // Relación con DetalleFactura - Un registro de factura puede tener múltiples detalles
    @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<DetalleFactura> detalles = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        this.fechaFactura = LocalDateTime.now();
        // Tipo de comprobante por defecto
        if (this.tipoComprobante == null) {
            this.tipoComprobante = "FACTURA";
        }
    }

    // Método auxiliar para calcular subtotal e IGV
    public void calcularTotales() {
        this.subtotal = this.total / 1.18; // Asumiendo IGV del 18%
        this.igv = this.total - this.subtotal;
    }
}
