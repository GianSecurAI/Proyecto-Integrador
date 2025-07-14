package com.example.Reyna.config;

import com.example.Reyna.dao.UserRepository;
import com.example.Reyna.dao.RolRepository; // Asegúrate de tener este repositorio
import com.example.Reyna.model.User;
import com.example.Reyna.model.Rol;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminInitializer {

    @Bean
    public CommandLineRunner initAdmin(UserRepository userRepository, RolRepository rolRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            String adminEmail = "torres123621@gmail.com";
            if (userRepository.findByCorreo(adminEmail).isEmpty()) {
                // Busca el rol ADMINISTRADOR en la base de datos
                 Rol adminRol = rolRepository.findById(1L) // Asumiendo que el ID del rol ADMINISTRADOR es 1
                .orElseThrow(() -> new RuntimeException("Rol con ID 1 (ADMINISTRADOR) no encontrado."));
                User admin = User.builder()
                        .nombre("Administrador") // Usar el campo 'nombre'
                        .apellido("Principal")     // Usar el campo 'apellido'
                        .correo(adminEmail)
                        .contraseña(passwordEncoder.encode("admin123"))
                        .telefono("999999999")
                        .direccion("Administración Central")
                        .estado(true)
                        .rol(adminRol) // Asigna el objeto Role
                        .build();
                userRepository.save(admin);
                System.out.println("Usuario administrador creado por defecto.");
            } else {
                System.out.println("El usuario administrador ya existe.");
            }
        };
    }
}