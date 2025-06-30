package com.example.Reyna.model;

import com.example.Reyna.model.Rol;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    private String nombre; // Cambiado de nombre_completo a nombre
    private String apellido; // Añadido el campo apellido
    private String correo;
    private String contraseña;
    private String telefono;
    private String direccion;
    private boolean estado;
    private Rol rol;

}