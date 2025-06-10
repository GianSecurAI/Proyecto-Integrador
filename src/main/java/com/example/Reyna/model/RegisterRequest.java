package com.example.Reyna.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    private String nombre_completo;
    private String correo;
    private String contraseña;
    private String telefono;
    private String direccion;
    private String estado;
    private Integer id_rol;
    private Role role;
}