package com.example.Reyna.model;

import com.example.Reyna.model.User;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usuario")
public class User implements UserDetails {

    @Id
    @GeneratedValue
    @Column(name = "id_usuario")
    Integer id_usuario;

    @Column(name = "nombre_completo") // Mapeando correctamente con el nombre de columna en la base de datos
    String nombreCompleto; // Usamos el nombre en camelCase para la variable
    @Column(name="correo")
    String correo;
    @Column(name="password")
    String password;
    @Column(name="telefono")
    String telefono;
    @Column(name="direccion")
    String direccion;
    @Column(name="estado")
    String estado;
    @Column(name="id_rol")
    Integer id_rol;
    @Enumerated(EnumType.STRING)
    @Column(name="role")
    Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Integer getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(Integer id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }



    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return this.correo;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Integer getId_rol() {
        return id_rol;
    }

    public void setId_rol(Integer id_rol) {
        this.id_rol = id_rol;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
        // Métodos de validación de rol
    public boolean isAdmin() {
        return (id_rol != null && id_rol == 1) || (role != null && role.name().equalsIgnoreCase("ADMINISTRADOR"));
    }

    public boolean isVendedor() {
        return (id_rol != null && id_rol == 2) || (role != null && role.name().equalsIgnoreCase("VENDEDOR"));
    }

    public boolean isCliente() {
        return (id_rol != null && id_rol == 3) || (role != null && role.name().equalsIgnoreCase("CLIENTE"));
    }

}
