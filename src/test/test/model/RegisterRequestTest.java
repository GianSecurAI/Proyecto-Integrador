package com.example.Reyna.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RegisterRequestTest {

    @Test
    void testConstructorAndGetters() {
        Rol rol = new Rol();
        rol.setId_rol(1L);
        rol.setNombre("VENDEDOR");

        RegisterRequest request = new RegisterRequest(
                "Juan",
                "Pérez",
                "juan@example.com",
                "clave123",
                "987654321",
                "Av. Siempre Viva 123",
                true,
                rol
        );

        assertEquals("Juan", request.getNombre());
        assertEquals("Pérez", request.getApellido());
        assertEquals("juan@example.com", request.getCorreo());
        assertEquals("clave123", request.getContraseña());
        assertEquals("987654321", request.getTelefono());
        assertEquals("Av. Siempre Viva 123", request.getDireccion());
        assertTrue(request.isEstado());
        assertEquals("VENDEDOR", request.getRol().getNombre());
    }

    @Test
    void testBuilder() {
        Rol rol = new Rol();
        rol.setId_rol(2L);
        rol.setNombre("ADMIN");

        RegisterRequest request = RegisterRequest.builder()
                .nombre("Ana")
                .apellido("García")
                .correo("ana@example.com")
                .contraseña("admin123")
                .telefono("999999999")
                .direccion("Calle Falsa 123")
                .estado(true)
                .rol(rol)
                .build();

        assertEquals("Ana", request.getNombre());
        assertEquals("García", request.getApellido());
        assertEquals("ana@example.com", request.getCorreo());
        assertEquals("admin123", request.getContraseña());
        assertEquals("999999999", request.getTelefono());
        assertEquals("Calle Falsa 123", request.getDireccion());
        assertTrue(request.isEstado());
        assertEquals("ADMIN", request.getRol().getNombre());
    }
}
