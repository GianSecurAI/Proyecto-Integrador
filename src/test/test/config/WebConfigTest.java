package com.example.Reyna.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
public class WebConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testCorsConfigurationAllowsAllowedOrigins() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.options("/api/test")  // cualquier endpoint de prueba
                .header("Origin", "http://localhost:3000")
                .header("Access-Control-Request-Method", "GET"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:3000"))
                .andExpect(header().string("Access-Control-Allow-Credentials", "true"));
    }

    @Test
    void testCorsConfigurationBlocksUnknownOrigin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.options("/api/test")
                .header("Origin", "http://malicioso.com")
                .header("Access-Control-Request-Method", "GET"))
                .andExpect(status().isForbidden()); 
    }
}
