package com.example.Reyna.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
    }

    @Test
    void testGenerateAndValidateToken() {
        // Arrange
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("usuario@example.com");

        // Act
        String token = jwtService.getToken(userDetails);

        // Assert
        assertNotNull(token);
        assertEquals("usuario@example.com", jwtService.getUsernameFromToken(token));
        assertTrue(jwtService.isTokenValid(token, userDetails));
    }

    @Test
    void testGetClaimFromToken() {
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("otro@example.com");

        String token = jwtService.getToken(userDetails);
        String extractedSubject = jwtService.getClaim(token, claims -> claims.getSubject());

        assertEquals("otro@example.com", extractedSubject);
    }

    @Test
    void testIsTokenExpiredReturnsFalse() {
        UserDetails user = mock(UserDetails.class);
        when(user.getUsername()).thenReturn("ejemplo@test.com");

        String token = jwtService.getToken(user);
        assertFalse(jwtService.isTokenValid("invalid.token.here", user)); // Test negativo

        // Verificación válida
        assertTrue(jwtService.isTokenValid(token, user));
    }
}
