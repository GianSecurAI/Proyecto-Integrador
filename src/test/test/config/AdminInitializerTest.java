package com.example.Reyna.config;

import com.example.Reyna.dao.UserRepository;
import com.example.Reyna.dao.RolRepository;
import com.example.Reyna.model.User;
import com.example.Reyna.model.Rol;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AdminInitializerTest {
    private UserRepository userRepository;
    private RolRepository rolRepository;
    private PasswordEncoder passwordEncoder;
    private AdminInitializer adminInitializer;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        rolRepository = mock(RolRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        adminInitializer = new AdminInitializer();
    }

    @Test
    void shouldCreateAdminUserIfNotExists() throws Exception {
        // Arrange
        String adminEmail = "admin@gmail.com";
        when(userRepository.findByCorreo(adminEmail)).thenReturn(Optional.empty());
        Rol adminRol = new Rol();
        adminRol.setId(1L);
        adminRol.setNombre("ADMINISTRADOR");

        when(rolRepository.findById(1L)).thenReturn(Optional.of(adminRol));
        when(passwordEncoder.encode("admin123")).thenReturn("encodedPassword");

        // Act
        adminInitializer.initAdmin(userRepository, rolRepository, passwordEncoder).run();

        // Assert
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        assertEquals("Administrador", savedUser.getNombre());
        assertEquals("Principal", savedUser.getApellido());
        assertEquals(adminEmail, savedUser.getCorreo());
        assertEquals("encodedPassword", savedUser.getContraseña());
        assertEquals(adminRol, savedUser.getRol());
    }

    @Test
    void shouldNotCreateAdminUserIfAlreadyExists() throws Exception {
        // Arrange
        String adminEmail = "admin@gmail.com";
        when(userRepository.findByCorreo(adminEmail)).thenReturn(Optional.of(new User()));

        // Act
        adminInitializer.initAdmin(userRepository, rolRepository, passwordEncoder).run();

        // Assert
        verify(userRepository, never()).save(any());
        verify(rolRepository, never()).findById(anyLong());
    }
    
}
