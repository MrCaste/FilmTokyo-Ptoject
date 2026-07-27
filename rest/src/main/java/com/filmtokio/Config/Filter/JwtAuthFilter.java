package com.filmtokio.Config.Filter;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.filmtokio.Service.JwtRestService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final String JWT_HEADER_PREFIX = "Bearer ";

    private final JwtRestService jwtRestService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                    HttpServletResponse response, 
                                    FilterChain filterChain) throws ServletException, IOException {
        
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (!isBearerAuthHeader(authHeader)) {
            unauthorized(response, "Missing or invalid Authorization header");
            return;
        }

        final String token = authHeader.substring(JWT_HEADER_PREFIX.length());
        final String subject = jwtRestService.extractSubject(token);

        final UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(subject, null,
                List.of(new SimpleGrantedAuthority("ROLE_API")));

        SecurityContextHolder.getContext().setAuthentication(auth);

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return request.getRequestURI().startsWith("/authenticate") ||
                request.getRequestURI().startsWith("/v3/api-docs") ||
                request.getRequestURI().startsWith("/swagger-ui") ||
                request.getRequestURI().startsWith("/actuator");
    }

    private void unauthorized(HttpServletResponse response, String errorMessage) throws IOException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setHeader("WWW-Authenticate", "Basic realm=\"API\"");
        response.setContentType("application/json");
        response.getWriter().write("{\"error\" \"%s\"}".formatted(errorMessage));
    }

    private boolean isBearerAuthHeader(String authHeader) {
        return authHeader != null && authHeader.startsWith(JWT_HEADER_PREFIX);
    }

}
