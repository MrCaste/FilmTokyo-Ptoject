package com.filmtokio.Controler;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.filmtokio.FormData.CreateUserFormData;
import com.filmtokio.Service.Users.UserService;

import jakarta.validation.Valid;



@Controller
@Slf4j
@RequiredArgsConstructor
public class CreateUserController {

    public final UserService userService;

    @GetMapping("/users/new")
    public String createUserForm() {

        return "/forms/createUser";
    }

    @PostMapping("/users/new")
    public String CreateUser(Model model, @Valid @ModelAttribute CreateUserFormData createUserFormData,
                                 BindingResult bindingResults,
                                 RedirectAttributes redirectAttributes) {

        if (bindingResults.hasErrors()) {

            log.error("Validation errors {}", bindingResults.getAllErrors());

            redirectAttributes.addFlashAttribute("errorMessage", bindingResults.getFieldError().getDefaultMessage());

            return "redirect:/users/new";
        }

        redirectAttributes.addFlashAttribute("success", "User created successfully!");

        userService.createUser(createUserFormData.getUsername(),
                createUserFormData.getEmail(), createUserFormData.getBirthDate(),createUserFormData.getRole(), createUserFormData.getPassword(), createUserFormData.getRepeatPassword());
        
        return "redirect:/users/new";
    }
    
    

}
