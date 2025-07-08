package com.example.Reyna.controller;

import com.example.Reyna.Service.UserService;
import com.example.Reyna.model.Rol;
import com.example.Reyna.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    // Simula un Authentication válido
    private Authentication getMockAuth(String email) {
        Authentication auth = Mockito.mock(Authentication.class);
        Mockito.when(auth.isAuthenticated()).thenReturn(true);
        Mockito.when(auth.getName()).thenReturn(email);
        return auth;
    }

    @Test
    void testGetCurrentUser_Authenticated() throws Exception {
        User user = new User();
        user.setId_usuario(1L);
        user.setCorreo("cliente@gmail.com");
        user.setNombre("Cliente");
        user.setContraseña("secreta");

        Mockito.when(userService.findByCorreo("cliente@gmail.com")).thenReturn(Optional.of(user));

        mockMvc.perform(get("/api/usuarios/me")
                        .principal(getMockAuth("cliente@gmail.com")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.correo").value("cliente@gmail.com"))
                .andExpect(jsonPath("$.contraseña").doesNotExist());
    }

    @Test
    void testGetCurrentUser_Unauthorized() throws Exception {
        mockMvc.perform(get("/api/usuarios/me"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testUpdateCurrentUser_Authenticated() throws Exception {
        User current = new User();
        current.setId_usuario(1L);
        current.setCorreo("cliente@gmail.com");
        current.setNombre("Antiguo");

        User input = new User();
        input.setNombre("Nuevo");

        User updated = new User();
        updated.setId_usuario(1L);
        updated.setCorreo("cliente@gmail.com");
        updated.setNombre("Nuevo");

        Mockito.when(userService.findByCorreo("cliente@gmail.com")).thenReturn(Optional.of(current));
        Mockito.when(userService.updateUser(eq(1L), any(User.class))).thenReturn(updated);

        mockMvc.perform(put("/api/usuarios/me")
                        .principal(getMockAuth("cliente@gmail.com"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Nuevo"))
                .andExpect(jsonPath("$.contraseña").doesNotExist());
    }

    @Test
    void testUpdateCurrentUser_Unauthorized() throws Exception {
        User input = new User();
        input.setNombre("Nuevo");

        mockMvc.perform(put("/api/usuarios/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isUnauthorized());
    }
}
