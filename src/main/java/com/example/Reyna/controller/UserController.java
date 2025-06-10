package com.example.Reyna.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.example.Reyna.Service.AuthService;
import com.example.Reyna.model.AuthResponse;
import com.example.Reyna.model.RegisterRequest;
import com.example.Reyna.model.User;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class UserController {

    private final AuthService authService;

    // Endpoint para que solo el ADMINISTRADOR registre vendedores o admins
    @PostMapping("/register-usuario")
    public ResponseEntity<AuthResponse> registerUsuario(
            @AuthenticationPrincipal User admin,
            @RequestBody RegisterRequest request) {
        if (admin == null || !admin.isAdmin()) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(authService.register(request));
    }
}