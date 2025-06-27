package com.example.Reyna.controller;

import com.example.Reyna.Service.AuthService;
import com.example.Reyna.dao.RolRepository;
import com.example.Reyna.model.AuthResponse;
import com.example.Reyna.model.LoginRequest;
import com.example.Reyna.model.RegisterRequest;
import com.example.Reyna.model.Rol;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final RolRepository rolRepository;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        // Busca el rol "CLIENTE" y lo asigna al nuevo usuario.
        Rol clienteRol = rolRepository.findById(3L) // Asumiendo que el ID del rol CLIENTE es 3
                .orElseThrow(() -> new RuntimeException("Error: Rol con ID 3 (CLIENTE) no encontrado."));
        request.setRol(clienteRol); // El RegisterRequest ya tiene nombre y apellido del frontend
        request.setEstado(true); // Los clientes se activan por defecto.
        return ResponseEntity.ok(authService.register(request));
    }
}