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

import com.filmtokio.Config.Filter.SimpleBasicAuthFilter;

import lombok.Getter;

@Configuration
@Getter
@EnableWebSecurity
public class SecurityConfig {

    @Value("${api.client.username}")
    private String apiUser;
    @Value("${api.client.password}")
    private String apiPassword;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        final SimpleBasicAuthFilter filter = new SimpleBasicAuthFilter(apiUser, apiPassword);

        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/**").hasRole("API")
                        .requestMatchers("/actuator/**").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
                .build();

    }

}
