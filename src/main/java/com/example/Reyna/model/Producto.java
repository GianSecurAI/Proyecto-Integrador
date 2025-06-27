package com.example.Reyna.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "producto")
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Long id_producto;

    @Column(name = "nombre_producto")
    private String nombre_producto;

    @Column(name = "precio")
    private Double precio;

    @ManyToOne
    @JoinColumn(name = "id_categoria")
    private Categoria categoria;

    @Column(name = "codigo", nullable = false, unique = true)
    private String codigo;

    @Column(name = "stock")
    private int stock;

    @Column(name = "marca")
    private String marca;

    @Column(name = "estado")
    private boolean estado;
    
    @Column(name = "contenido")
    private String contenido;

    @Column(name = "descripcion")
    private String descripcion;
}
