package com.example.reyna.controller;

import com.example.reyna.demo.DemoController;
import com.example.reyna.demo.RequestResponse;
import com.example.reyna.service.ServiceUser;
import com.example.reyna.user.User;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DemoController.class)
public class DemoControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockBean private ServiceUser serviceUser;
    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testCreateUser() throws Exception {
        User user = new User();
        user.setNombreCompleto("Juan");

        Mockito.when(serviceUser.saveUser(Mockito.any())).thenReturn(user);

        mockMvc.perform(post("/auth/user/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Usuario creado con éxito"))
                .andExpect(jsonPath("$.data.nombreCompleto").value("Juan"));
    }
}

