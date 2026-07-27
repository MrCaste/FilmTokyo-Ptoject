package com.filmtokio.RestController;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.filmtokio.Config.SecurityConfig;
import com.filmtokio.RestDTO.AccessTokenResponse;
import com.filmtokio.Service.JwtRestService;

import lombok.RequiredArgsConstructor;

import java.util.Base64;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequiredArgsConstructor
public class AuthApiController {

    private static final String BASIC_HEADER_PREFIX = "Basic ";
    private static final String USER_PASS_SEPARATOR = ":";
    private static final String CLIENT_CREDENTIALS = "client_credentials";

    private final JwtRestService jwtRestService;
    private final SecurityConfig securityConfig;
    
    @PostMapping(value = "/authenticate", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public AccessTokenResponse authenticate(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
            @RequestParam("grant_type") String grantType) {

        // Extraer credenciales basic
        if (!CLIENT_CREDENTIALS.equals(grantType)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unsuppoerted grant type");
        }
        // Check credenciales
        if (!validUserAndPass(authorization)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }
        // Generar el token jwt
        String token = jwtRestService.generateJwtToken(securityConfig.getApiUser(), securityConfig.getExpiration());
        // Devolver el token jwt
        return new AccessTokenResponse(token, "JWT", securityConfig.getExpiration());
    }

    private String decodeBasicAuthContent(String authHeader) {
        final String base64 = authHeader.substring(BASIC_HEADER_PREFIX.length());
        return new String(Base64.getDecoder().decode(base64));
    }

    //check user y pass son correctos
    private boolean validUserAndPass(String authHeader) {
        final String decode = decodeBasicAuthContent(authHeader);
        final String[] parts = decode.split(USER_PASS_SEPARATOR, 2);

        return parts.length == 2 && parts[0].equals(securityConfig.getApiUser()) && parts[1].equals(securityConfig.getApiPassword());
    }

}
