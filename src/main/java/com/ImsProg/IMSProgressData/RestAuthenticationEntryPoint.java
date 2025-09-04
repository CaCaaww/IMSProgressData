package com.ImsProg.IMSProgressData;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {                    
        
        String path = request.getServletPath();

        if (path.equals("/login") || path.equals("/error")) {
        // Let Spring handle it
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        return;
    }
        String acceptHeader = request.getHeader("Accept");

        if (acceptHeader != null && acceptHeader.contains("application/json")) {
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\": \"Unauthorized\"}");
        } else {
            // Let Spring Security handle it normally (e.g., redirect to login page)
            response.sendRedirect("/login");
        }
    }
}