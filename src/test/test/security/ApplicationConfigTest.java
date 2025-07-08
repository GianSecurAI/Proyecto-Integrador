package com.example.Reyna.security;

import com.example.Reyna.dao.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetailsService;

import static org.junit.jupiter.api.Assertions.*;

class ApplicationConfigTest {

    @Test
    void testBeansInitialization() throws Exception {
        // Simulamos UserRepository
        UserRepository mockRepo = Mockito.mock(UserRepository.class);

        // Creamos instancia de la clase config
        ApplicationConfig config = new ApplicationConfig(mockRepo);

        // PasswordEncoder
        PasswordEncoder encoder = config.passwordEncoder();
        assertNotNull(encoder);
        assertTrue(encoder.matches("123456", encoder.encode("123456")));

        // UserDetailsService (devuelve excepción porque el mock no retorna nada)
        UserDetailsService uds = config.userDetailService();
        assertThrows(Exception.class, () -> uds.loadUserByUsername("noexiste@example.com"));

        // AuthenticationProvider
        AuthenticationProvider provider = config.authenticationProvider();
        assertNotNull(provider);

        // AuthenticationManager con mock
        AuthenticationConfiguration authConfig = Mockito.mock(AuthenticationConfiguration.class);
        AuthenticationManager authManager = Mockito.mock(AuthenticationManager.class);
        Mockito.when(authConfig.getAuthenticationManager()).thenReturn(authManager);

        assertNotNull(config.authenticationManager(authConfig));
    }
}
