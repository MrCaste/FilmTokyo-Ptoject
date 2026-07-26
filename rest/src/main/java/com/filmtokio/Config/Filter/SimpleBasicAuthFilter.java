package com.filmtokio.Config.Filter;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SimpleBasicAuthFilter extends OncePerRequestFilter{

    private static final String BASIC_HEADER_PREFIX = "Basic ";
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String USER_PASS_SEPARATOR = ":";
    private final String username;
    private final String password;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        // Comprobar que viene cabecera Authorization
        final String authHeader = request.getHeader(AUTHORIZATION_HEADER);

        if (!isBasicAuthHeader(authHeader)) {

            unauthorized(response, "Missing or invalid Authorization header");
            return;
        }

        if (!validUserAndPass(authHeader)) {

            unauthorized(response, "Invalid user or password");
            return;
        }

        //Inicializar el security context
        final UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(username, password,
                List.of(new SimpleGrantedAuthority("ROLE_API")));

        SecurityContextHolder.getContext().setAuthentication(auth);

        //Si son correctos pasar al siguiente filtro
        filterChain.doFilter(request, response);
    }


    //--------------------------------------------------

    private void unauthorized(HttpServletResponse response, String errorMessage) throws IOException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setHeader("WWW-Authenticate", "Basic realm=\"API\"");
        response.setContentType("application/json");
        response.getWriter().write("{\"error\" \"%s\"}".formatted(errorMessage));
    }

    private String decodeBasicAuthContent(String authHeader) {
        final String base64 = authHeader.substring(BASIC_HEADER_PREFIX.length());
        return new String(Base64.getDecoder().decode(base64));
    }

    //check user y pass son correctos
    private boolean validUserAndPass(String authHeader) {
        final String decode = decodeBasicAuthContent(authHeader);
        final String[] parts = decode.split(USER_PASS_SEPARATOR, 2);

        return parts.length == 2 && parts[0].equals(username) && parts[1].equals(password);
    }

    //check si Authorization viene en la cabecera
    private boolean isBasicAuthHeader(String authHeader) {
        return authHeader != null && authHeader.startsWith(BASIC_HEADER_PREFIX);
    }
}
