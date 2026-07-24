package com.filmtokio.Service.Users;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.filmtokio.DTO.UserDTO;
import com.filmtokio.Entities.User;
import com.filmtokio.Enum.Role;
import com.filmtokio.Repositorys.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void registerUser(String username, String email, LocalDate birthDate, String password, String confirmPassword) {

        // gestionamos los campos vacíos
        if (username == null || username.isBlank() || email == null || email.isBlank() || birthDate == null || password == null || password.isBlank() || confirmPassword == null || confirmPassword.isBlank()) {
            throw new IllegalArgumentException("All fields are required");
        }

        // checkeamos si el username ya existe
        existsByUsernameOrEmail(username, email);

        // checkeamos si las contraseñas coinciden
        isPasswordsMatch(password, confirmPassword);

        // persistimos en BD y codificamos la contraseña
        userRepository.save(User.builder()
                .username(username)
                .email(email)
                .birthDate(birthDate)
                .password(passwordEncoder.encode(password))
                .role(Role.USER) // Asignamos el rol USER por defecto
                .createdAt(LocalDateTime.now())
                .build());
    }

    public void registerAdmin(String username, String email, LocalDate birthDate, String password, String confirmPassword) {

        // gestionamos los campos vacíos
        if (username == null || username.isBlank() || email == null || email.isBlank() || birthDate == null || password == null || password.isBlank() || confirmPassword == null || confirmPassword.isBlank()) {
            throw new IllegalArgumentException("All fields are required");
        }
        // checkeamos si el username ya existe
        existsByUsernameOrEmail(username, email);

        // checkeamos si las contraseñas coinciden
        isPasswordsMatch(password, confirmPassword);

        // persistimos en BD y codificamos la contraseña
        userRepository.save(User.builder()
                .username(username)
                .email(email)
                .birthDate(birthDate)
                .password(passwordEncoder.encode(password))
                .role(Role.ADMIN) // Asignamos el rol ADMIN por defecto
                .createdAt(LocalDateTime.now())
                .build());
    }

    public void createUser(String username, String email, LocalDate birthDate, Role role, String password, String confirmPassword) {

        // gestionamos los campos vacíos
        if (username == null || username.isBlank() || email == null || email.isBlank() || birthDate == null || role == null || password == null || password.isBlank() || confirmPassword == null || confirmPassword.isBlank()) {
            throw new IllegalArgumentException("All fields are required");
        }

        // checkeamos si el username ya existe
        existsByUsernameOrEmail(username, email);

        // checkeamos si las contraseñas coinciden
        isPasswordsMatch(password, confirmPassword);

        // persistimos en BD y codificamos la contraseña
        userRepository.save(User.builder()
                .username(username)
                .email(email)
                .birthDate(birthDate)
                .password(passwordEncoder.encode(password))
                .role(role)
                .createdAt(LocalDateTime.now())
                .build());
    }

    public User getAuthenticatedUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        return userRepository.findByEmailOrUsername(username).orElseThrow(() -> new UsernameNotFoundException("Usuario autenticado no encontrado"));
    }

    public boolean isAuthenticated() {

        Authentication authentication = SecurityContextHolder
            .getContext()
            .getAuthentication();

        return authentication != null
                && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken);
    }










    public void existsByUsernameOrEmail(String username, String email) {
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already exists");
        }
    }

    public void isPasswordsMatch(String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            throw new IllegalArgumentException("Passwords do not match");
        }
    }

    public UserDTO toUserDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .birthDate(user.getBirthDate())
                .password(user.getPassword())
                .role(user.getRole().name())
                .build();
    }

}
