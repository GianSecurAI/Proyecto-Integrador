package com.example.Reyna.model;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    void testUserDetailsMethods() {
        Rol rol = new Rol();
        rol.setId_rol(1L);
        rol.setNombre("ADMINISTRADOR");

        User user = User.builder()
                .id_usuario(1L)
                .nombre("Luis")
                .apellido("Gomez")
                .correo("luis@example.com")
                .contraseña("secreta")
                .telefono("123456789")
                .direccion("Calle 1")
                .estado(true)
                .rol(rol)
                .build();

        assertEquals("luis@example.com", user.getUsername());
        assertEquals("secreta", user.getPassword());
        assertTrue(user.isAccountNonExpired());
        assertTrue(user.isAccountNonLocked());
        assertTrue(user.isCredentialsNonExpired());
        assertTrue(user.isEnabled());

        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        assertEquals(1, authorities.size());
        assertEquals("ADMINISTRADOR", authorities.iterator().next().getAuthority());
    }

    @Test
    void testDisabledUser() {
        User user = new User();
        user.setEstado(false);
        assertFalse(user.isEnabled());
    }
}
