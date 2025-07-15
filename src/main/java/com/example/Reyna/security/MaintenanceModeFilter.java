package com.example.Reyna.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class MaintenanceModeFilter extends OncePerRequestFilter {

    private final AccessTimeManager accessTimeManager;

    @Autowired
    public MaintenanceModeFilter(AccessTimeManager accessTimeManager) {
        this.accessTimeManager = accessTimeManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        // Permitir siempre las solicitudes a recursos estáticos y endpoint de mantenimiento
        String path = request.getRequestURI();
        if (isStaticResourceRequest(path) || path.equals("/maintenance")) {
            filterChain.doFilter(request, response);
            return;
        }
        
        // Verificar si el acceso está permitido
        if (!accessTimeManager.isAccessAllowed()) {
            response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            response.setContentType("application/json");
            response.getWriter().write("{\"message\":\"Sistema en mantenimiento. Por favor, intente más tarde.\"}");
            return;
        }
        
        filterChain.doFilter(request, response);
    }
    
    private boolean isStaticResourceRequest(String path) {
        return path.contains("/css/") || path.contains("/js/") || 
               path.contains("/images/") || path.contains("/favicon.ico");
    }
}
