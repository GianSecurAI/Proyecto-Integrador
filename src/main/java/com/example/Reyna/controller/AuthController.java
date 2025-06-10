package com.example.Reyna.controller;

// Importaciones necesarias para manejar respuestas HTTP y anotaciones de Spring
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Reyna.Service.AuthService;
import com.example.Reyna.model.AuthResponse;
import com.example.Reyna.model.LoginRequest;
import com.example.Reyna.model.RegisterRequest;

import lombok.RequiredArgsConstructor;

// Anotación que indica que esta clase es un controlador REST de Spring
@RestController
// Define la ruta base para todas las solicitudes que maneja este controlador
@RequestMapping("/auth")
// Genera un constructor con los atributos requeridos (final), útil para inyección de dependencias
@RequiredArgsConstructor

public class AuthController {
    // Servicio que maneja la lógica de autenticación y registro
    private final AuthService authService;
    

    // Recibe los datos de login en el cuerpo de la solicitud y los pasa al servicio
    @PostMapping(value = "login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request)
    {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping(value = "register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request)
    {
        return ResponseEntity.ok(authService.register(request));
    }

}