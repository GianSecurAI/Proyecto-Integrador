package com.example.Reyna.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import jakarta.persistence.*;


@Entity
@Table(name = "Pedido")
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_pedido;

    @ManyToOne
    @JoinColumn(name = "id_cliente")
    private User cliente;

    private LocalDateTime fecha_pedido;
    private String estado_pedido;
    private BigDecimal total;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    private List<DetallePedido> detalles;
    
    public Long getIdPedido() {
        return id_pedido;
    }
    public void setIdPedido(Long id_pedido) {
        this.id_pedido = id_pedido;
    }
    
    public User getCliente() {
        return cliente;
    }
    public void setCliente(User cliente) {
        this.cliente = cliente;
    }
    
    public LocalDateTime getFechaPedido() {
        return fecha_pedido;
    }
    public void setFechaPedido(LocalDateTime fecha_pedido) {
        this.fecha_pedido = fecha_pedido;
    }
    
    public String getEstadoPedido() {
        return estado_pedido;
    }
    public void setEstadoPedido(String estado_pedido) {
        this.estado_pedido = estado_pedido;
    }
    
    public BigDecimal getTotal() {
        return total;
    }
    public void setTotal(BigDecimal total) {
        this.total = total;
    }
    
    public List<DetallePedido> getDetalles() {
        return detalles;
    }
    public void setDetalles(List<DetallePedido> detalles) {
        this.detalles = detalles;
    }

}

