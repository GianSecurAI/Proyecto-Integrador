package com.example.Reyna.Service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.Reyna.security.JwtService;
import com.example.Reyna.model.AuthResponse;
import com.example.Reyna.model.LoginRequest;
import com.example.Reyna.model.RegisterRequest;
import com.example.Reyna.model.Rol;
import com.example.Reyna.model.User;
import com.example.Reyna.dao.UserRepository;
import com.example.Reyna.dao.RolRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RolRepository roleRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getCorreo(), request.getContraseña())
        );
        User user = userRepository.findByCorreo(request.getCorreo()).orElseThrow();
        String token = jwtService.getToken(user);
        return AuthResponse.builder()
            .token(token)
            .data(user)
            .message("Login exitoso")
            .build();
    }

    public AuthResponse register(RegisterRequest request) {
        // Busca el objeto Role usando el id_rol recibido
        Rol rol;
        if (request.getRol() == null) {
            throw new IllegalArgumentException("La solicitud de registro debe incluir información de rol.");
        }

        // Busca el rol por ID. Se asume que el frontend siempre enviará el id_rol.
        if (request.getRol().getId_rol() == null) {
            throw new IllegalArgumentException("La solicitud de registro debe incluir el ID del rol.");
        }
        rol = roleRepository.findById(request.getRol().getId_rol())
            .orElseThrow(() -> new RuntimeException("Rol no encontrado por ID: " + request.getRol().getId_rol()));
        

        User user = User.builder()
            .nombre(request.getNombre()) // Usar el nuevo campo nombre
            .apellido(request.getApellido()) // Usar el nuevo campo apellido
            .contraseña(passwordEncoder.encode(request.getContraseña()))
            .correo(request.getCorreo())
            .telefono(request.getTelefono())
            .direccion(request.getDireccion())
            .estado(request.isEstado())
            .rol(rol)
            .build();

        userRepository.save(user);

        return AuthResponse.builder()
            .token(jwtService.getToken(user))
            .data(user)
            .message("Registro exitoso")
            .build();
    }
}