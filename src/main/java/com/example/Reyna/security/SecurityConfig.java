package com.example.Reyna.security;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authProvider;
    private final MaintenanceModeFilter maintenanceModeFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(authRequest -> authRequest
                        // Endpoints públicos (sin autenticación)
                        .requestMatchers("/", "/index.html", "/static/**", "/manifest.json", "/favicon.ico", "/*.png", "/*.jpg", "/*.jpeg", "/*.gif").permitAll()
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/api/productos/**", "/api/categorias/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/productos/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/categorias/**").permitAll()
                        //Permitir acceso a endpoints de Actuator
                        .requestMatchers("/actuator/**").permitAll()
                        // Endpoints solo para ADMINISTRADOR
                        .requestMatchers(HttpMethod.POST, "/admin/register-usuario").hasAuthority("ADMINISTRADOR")
                        .requestMatchers("/admin/users/**").hasAuthority("ADMINISTRADOR") // Permite todas las operaciones CRUD para usuarios/clientes (incluye /admin/users/register-client si existiera)
                        // Endpoints para ADMINISTRADOR y VENDEDOR
                        .requestMatchers(HttpMethod.POST, "/api/productos").hasAnyAuthority("ADMINISTRADOR", "VENDEDOR") // <-- MODIFICADO: Protege solo la creación
                        .requestMatchers(HttpMethod.PUT, "/api/productos/**").hasAnyAuthority("ADMINISTRADOR", "VENDEDOR") // <-- MODIFICADO: Protege solo la actualización
                        .requestMatchers(HttpMethod.DELETE, "/api/productos/**").hasAnyAuthority("ADMINISTRADOR", "VENDEDOR") // <-- MODIFICADO: Protege solo el borrado
                        // Endpoints para CLIENTES
                        .requestMatchers("/api/pedidos/**").hasAuthority("CLIENTE")
                        // Endpoints para CUALQUIER USUARIO AUTENTICADO (CLIENTE, VENDEDOR, ADMIN)
                        .requestMatchers("/api/usuarios/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/ventas/**").hasAuthority("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.POST, "/api/ventas/**").hasAnyAuthority("ADMINISTRADOR", "VENDEDOR")
                        .anyRequest().authenticated())
                .sessionManagement(sessionManager -> sessionManager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                // Agrega el filtro de mantenimiento antes del filtro de autenticación
                .addFilterBefore(maintenanceModeFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://localhost:8080"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
