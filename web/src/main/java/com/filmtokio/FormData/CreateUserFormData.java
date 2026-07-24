package com.filmtokio.FormData;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.filmtokio.Enum.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CreateUserFormData {

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    private String username;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    private String email;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate birthDate;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 50, message = "Password must be between 8 and 50 characters")
    private String password;
    
    @NotBlank(message = "Repeat password is required")
    private String repeatPassword;

    @NotNull(message = "A role is required")
    private Role role;

}
