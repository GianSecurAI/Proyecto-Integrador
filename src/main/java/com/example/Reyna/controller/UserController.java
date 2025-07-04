package com.example.Reyna.controller;

import com.example.Reyna.model.User;
import com.example.Reyna.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios") // ¡La única ruta correcta para los datos del cliente!
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Endpoint para que un usuario autenticado obtenga SU PROPIA información.
     * GET /api/usuarios/me
     */
    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String userEmail = authentication.getName();
        User usuario = userService.findByCorreo(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + userEmail));

        usuario.setContraseña(null); // ¡Nunca envíes la contraseña al frontend!
        return ResponseEntity.ok(usuario);
    }

    /**
     * Endpoint para que un usuario autenticado actualice SU PROPIA información.
     * PUT /api/usuarios/me
     */
    @PutMapping("/me")
    public ResponseEntity<User> updateCurrentUser(Authentication authentication, @RequestBody User userDetails) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String userEmail = authentication.getName();
        User usuarioActual = userService.findByCorreo(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + userEmail));

        // Usamos el método updateUser de tu servicio, pasándole el ID del usuario actual
        User updatedUsuario = userService.updateUser(usuarioActual.getId_usuario(), userDetails);
        updatedUsuario.setContraseña(null); // Tampoco enviamos la contraseña de vuelta
        return ResponseEntity.ok(updatedUsuario);
    }
}

