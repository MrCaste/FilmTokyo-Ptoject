package com.filmtokio.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.filmtokio.Service.Users.FilmUserDetailsService;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    public static final String LOGIN_PATH = "/login"; 

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth.requestMatchers("/css/**","/js/**","/images/**").permitAll()
                                                .requestMatchers("/signup", LOGIN_PATH, "/home", "/").permitAll()
                                                .requestMatchers("/admin/**").hasAuthority("ADMIN")
                                                .anyRequest().authenticated())
            .csrf(csrf -> csrf.disable())

            .formLogin(form -> form.loginPage(LOGIN_PATH)
                                   .loginProcessingUrl(LOGIN_PATH)
                                   .defaultSuccessUrl("/home", true)
                                   .failureUrl(LOGIN_PATH + "?error=true"))

            .logout(logout -> logout.logoutUrl("/logout")
                                    .logoutSuccessUrl(LOGIN_PATH + "?logout=true"));

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(FilmUserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {

        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
        
        authProvider.setPasswordEncoder(passwordEncoder);

        return authProvider;
    }
}
