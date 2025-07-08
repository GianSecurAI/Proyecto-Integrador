package com.example.Reyna.controller;

import com.example.Reyna.Service.AuthService;
import com.example.Reyna.Service.UserService;
import com.example.Reyna.model.AuthResponse;
import com.example.Reyna.model.RegisterRequest;
import com.example.Reyna.model.User;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminController.class)
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testRegisterUsuario() throws Exception {
        // Simular petición de registro
        RegisterRequest request = new RegisterRequest();
        request.setCorreo("nuevo@correo.com");
        request.setContraseña("clave123");
        request.setNombre("Nuevo");
        request.setApellido("Usuario");

        // Simular respuesta esperada
        User newUser = new User();
        newUser.setId_usuario(2L);
        newUser.setCorreo("nuevo@correo.com");

        AuthResponse response = AuthResponse.builder()
                .token("abc.def.ghi")
                .message("Usuario registrado con éxito")
                .data(newUser)
                .build();

        // Mock del AuthService
        Mockito.when(authService.register(any(RegisterRequest.class))).thenReturn(response);

        // Ejecutar el test del endpoint
        mockMvc.perform(post("/admin/register-usuario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("abc.def.ghi"))
                .andExpect(jsonPath("$.message").value("Usuario registrado con éxito"))
                .andExpect(jsonPath("$.data.correo").value("nuevo@correo.com"));
    }
}
