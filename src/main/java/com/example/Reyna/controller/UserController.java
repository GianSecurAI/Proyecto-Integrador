package com.example.Reyna.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.example.Reyna.Service.AuthService;
import com.example.Reyna.Service.UserService;
import com.example.Reyna.model.AuthResponse;
import com.example.Reyna.model.RegisterRequest;
import com.example.Reyna.model.User;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class UserController {

    private final AuthService authService;
    private final UserService userService;
    // Endpoint para que solo el ADMINISTRADOR registre vendedores o admins
    @PostMapping("/register-usuario")
    public ResponseEntity<AuthResponse> registerUsuario(
            @AuthenticationPrincipal User admin,
            @RequestBody RegisterRequest request) { // Este endpoint es para registrar VENDEDOR o ADMIN
        return ResponseEntity.ok(authService.register(request));

    }

    // Endpoint para obtener todos los clientes, con funcionalidad de búsqueda rápida
    @GetMapping("/users/clientes")
    public ResponseEntity<List<User>> getClientes(@RequestParam(required = false) String query) {

        List<User> clientes = userService.searchClients(query); // Use the new searchClients method
        if (clientes.isEmpty()) {
            return ResponseEntity.noContent().build(); 
        }
        return ResponseEntity.ok(clientes);
    }

    // Endpoint para actualizar un usuario
    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        User updatedUser = userService.updateUser(id, userDetails);
        return ResponseEntity.ok(updatedUser);
    }

    // Nuevo endpoint para obtener un usuario por ID
    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    // Endpoint para eliminar un usuario
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        // Considera añadir una verificación si el usuario a eliminar es un ADMIN para evitar auto-eliminación
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}