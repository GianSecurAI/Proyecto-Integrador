package com.example.reyna.service;

import com.example.reyna.user.User;
import com.example.reyna.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ServiceUserTest {

    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;

    @InjectMocks
    private ServiceUser serviceUser;

    public ServiceUserTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLogin_Successful() {
        String correo = "juan@mail.com";
        String rawPassword = "secret";
        User mockUser = new User();
        mockUser.setCorreo(correo);
        mockUser.setPassword("hashed");

        when(userRepository.findByCorreo(correo)).thenReturn(mockUser);
        when(passwordEncoder.matches(rawPassword, "hashed")).thenReturn(true);

        User result = serviceUser.login(correo, rawPassword);
        assertNotNull(result);
        assertEquals(correo, result.getCorreo());
    }

    @Test
    public void testLogin_Failure_WrongPassword() {
        when(userRepository.findByCorreo("x@mail.com")).thenReturn(new User());
        when(passwordEncoder.matches("wrong", "hashed")).thenReturn(false);

        User user = serviceUser.login("x@mail.com", "wrong");
        assertNull(user);
    }
}
