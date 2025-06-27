package com.example.Reyna.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Usuario") // Asegúrate de que el nombre de la tabla coincida con tu DDL
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long id_usuario;

    @Column(name = "nombre", nullable = false)
    private String nombre; // Cambiado de nombre_completo

    @Column(name = "apellido", nullable = false) // Nuevo campo
    private String apellido;

    @Column(name = "correo", nullable = false, unique = true)
    private String correo;

    @Column(name = "contraseña", nullable = false)
    private String contraseña;

    @Column(name = "telefono")
    private String telefono;

    @Column(name = "direccion")
    private String direccion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_rol", nullable = false)
    private Rol rol;

    @Column(name = "estado")
    private boolean estado;

    // Implementación de UserDetails
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Asume que el rol tiene un nombre que puede ser usado como autoridad
        return List.of(new SimpleGrantedAuthority(rol.getNombre()));
    }

    @Override
    public String getPassword() {
        return contraseña;
    }

    @Override
    public String getUsername() {
        return correo;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // O tu lógica de expiración de cuenta
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // O tu lógica de bloqueo de cuenta
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // O tu lógica de expiración de credenciales
    }

    @Override
    public boolean isEnabled() {
        return estado; // Usa el campo 'estado' para determinar si el usuario está habilitado
    }
}