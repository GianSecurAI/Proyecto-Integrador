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

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
        RegisterRequest request = new RegisterRequest();
        AuthResponse response = new AuthResponse("token123");

        Mockito.when(authService.register(any(RegisterRequest.class))).thenReturn(response);

        mockMvc.perform(post("/admin/register-usuario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token123"));
    }

    @Test
    void testGetClientes_withResults() throws Exception {
        User client = new User();
        client.setId(1L);
        client.setNombre("Juan");

        Mockito.when(userService.searchClients(anyString())).thenReturn(List.of(client));

        mockMvc.perform(get("/admin/users/clientes?query=juan"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Juan"));
    }

    @Test
    void testGetClientes_noContent() throws Exception {
        Mockito.when(userService.searchClients(anyString())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/admin/users/clientes?query=nadie"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testUpdateUser() throws Exception {
        User input = new User();
        input.setNombre("Nuevo");

        User updated = new User();
        updated.setId(1L);
        updated.setNombre("Nuevo");

        Mockito.when(userService.updateUser(eq(1L), any(User.class))).thenReturn(updated);

        mockMvc.perform(put("/admin/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Nuevo"));
    }

    @Test
    void testGetUserById_found() throws Exception {
        User user = new User();
        user.setId(2L);
        user.setNombre("Carlos");

        Mockito.when(userService.getUserById(2L)).thenReturn(user);

        mockMvc.perform(get("/admin/users/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Carlos"));
    }

    @Test
    void testGetUserById_notFound() throws Exception {
        Mockito.when(userService.getUserById(99L)).thenReturn(null);

        mockMvc.perform(get("/admin/users/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteUser() throws Exception {
        Mockito.doNothing().when(userService).deleteUser(1L);

        mockMvc.perform(delete("/admin/users/1"))
                .andExpect(status().isNoContent());
    }
}
