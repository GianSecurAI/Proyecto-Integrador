package com.example.reyna.service;

import com.example.reyna.auth.AuthResponse;
import com.example.reyna.auth.LoginRequest;
import com.example.reyna.jwt.JwtService;
import com.example.reyna.user.User;
import com.example.reyna.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class AuthServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private JwtService jwtService;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    public AuthServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLogin_ShouldReturnValidToken() {
        LoginRequest request = new LoginRequest("Juan Perez", "1234");
        UserDetails mockUser = new User();

        when(userRepository.findByNombreCompleto("Juan Perez"))
                .thenReturn(Optional.of((User) mockUser));

        when(jwtService.getToken(mockUser)).thenReturn("fake-jwt-token");

        AuthResponse response = authService.login(request);

        verify(authenticationManager).authenticate(
                new UsernamePasswordAuthenticationToken("Juan Perez", "1234")
        );
        assertEquals("fake-jwt-token", response.getToken());
    }
}
