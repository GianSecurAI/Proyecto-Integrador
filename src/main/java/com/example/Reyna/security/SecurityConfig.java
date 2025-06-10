package com.example.Reyna.security;

    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
    import org.springframework.security.config.http.SessionCreationPolicy;
    import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.security.web.SecurityFilterChain;
    import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
    
    import com.example.Reyna.security.JwtAuthenticationFilter;
    
    import lombok.RequiredArgsConstructor;
    
    @Configuration
    @EnableWebSecurity
    @RequiredArgsConstructor
    
    public class SecurityConfig {
    
        private final JwtAuthenticationFilter jwtAuthenticationFilter;
        private final AuthenticationProvider authProvider;
    
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
        {
            return http
                .cors()
                .and()
                .csrf(csrf -> 
                    csrf
                    .disable())
                .authorizeHttpRequests(authRequest ->
                authRequest
                // Público: autenticación y ver productos
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers("/admin/**").hasAuthority("ADMINISTRADOR")
                .requestMatchers(HttpMethod.GET, "/api/productos/**").permitAll()
                // Solo ADMINISTRADOR puede editar/eliminar productos y ver ventas
                .requestMatchers(HttpMethod.PUT, "/api/productos/**").hasAuthority("ADMINISTRADOR")
                .requestMatchers(HttpMethod.DELETE, "/api/productos/**").hasAuthority("ADMINISTRADOR")
                .requestMatchers(HttpMethod.GET, "/api/ventas/**").hasAuthority("ADMINISTRADOR")
                // ADMINISTRADOR y VENDEDOR pueden crear productos y ventas
                .requestMatchers(HttpMethod.POST, "/api/productos/**").hasAnyAuthority("ADMINISTRADOR", "VENDEDOR")
                .requestMatchers(HttpMethod.POST, "/api/ventas/**").hasAnyAuthority("ADMINISTRADOR", "VENDEDOR")
                // CLIENTE solo puede comprar (ajusta el endpoint si es diferente)
                .requestMatchers(HttpMethod.POST, "/api/compras/**").hasAuthority("CLIENTE")
                // Todo lo demás requiere autenticación
                .anyRequest().authenticated()
)
                .sessionManagement(sessionManager->
                    sessionManager 
                      .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
                
                
        }
    
    }
