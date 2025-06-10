package com.example.reyna.controller;

import com.example.reyna.auth.*;
import com.example.reyna.service.AuthService;
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

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(AuthController.class)
public class AuthControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockBean private AuthService authService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testLoginEndpointReturnsToken() throws Exception {
        LoginRequest loginRequest = new LoginRequest("Juan", "1234");
        AuthResponse response = new AuthResponse("fake-token");

        Mockito.when(authService.login(any())).thenReturn(response);

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("fake-token"));
    }
}
