package com.filmtokio.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.filmtokio.Config.Filter.JwtAuthFilter;
import com.filmtokio.Service.JwtRestService;

import lombok.Getter;

@Configuration
@Getter
@EnableWebSecurity
public class SecurityConfig {

    @Value("${api.client.username}")
    private String apiUser;
    @Value("${api.client.password}")
    private String apiPassword;
    @Value("${app.security.jwt.secret}")
    private String secret;
    @Value("${app.security.jwt.expiration}")
    private Long expiration;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtRestService jwtRestService) throws Exception {

        final JwtAuthFilter filter = new JwtAuthFilter(jwtRestService);

        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/**").hasRole("API")
                        .requestMatchers("/actuator/**","/authenticate","/v3/api-docs/**","/swagger-ui/**").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
                .build();

    }

}
