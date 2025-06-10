package com.example.Reyna.Service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.Reyna.security.JwtService;
import com.example.Reyna.model.AuthResponse;
import com.example.Reyna.model.LoginRequest;
import com.example.Reyna.model.RegisterRequest;
import com.example.Reyna.model.Role;
import com.example.Reyna.model.User;
import com.example.Reyna.dao.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthResponse login(LoginRequest request) {
        // Autentica el usuario usando Spring Security (verifica nickname y contraseña)
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getCorreo(), request.getContraseña()));
        User user = userRepository.findByCorreo(request.getCorreo()).orElseThrow();
        String token=jwtService.getToken(user);
        return AuthResponse.builder()
            .token(token)
            .build();
    }

    public AuthResponse register(RegisterRequest request) {
        User user = User.builder()
            .nombreCompleto(request.getNombre_completo())
            .password(passwordEncoder.encode( request.getContraseña()))
            .correo(request.getCorreo())
            .telefono(request.getTelefono())
            .direccion(request.getDireccion())
            .estado(request.getEstado())
            .role(request.getRole())
            .build();

        userRepository.save(user);

        return AuthResponse.builder()
            .token(jwtService.getToken(user))
            .build();
        
    }

}