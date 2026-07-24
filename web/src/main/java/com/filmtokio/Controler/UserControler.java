package com.filmtokio.Controler;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import com.filmtokio.FormData.UserFormData;
import com.filmtokio.Service.Users.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;



@Controller
@Slf4j
@RequiredArgsConstructor
public class UserControler {

    private final UserService userService;

    @GetMapping("/signup")
    public String signUp(Model model) {
        model.addAttribute("userFormData", new UserFormData());
        return "/forms/signup";
    }
    
    @GetMapping("/login")
    public String login() {
        return "/forms/login";
    }

    @PostMapping("/signup")
    public String doSignup(Model model, @Valid @ModelAttribute UserFormData userFormData,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {

        log.info("Received signup request: {}", userFormData);

        if (bindingResult.hasErrors()) {
            log.error("Validation errors: {}", bindingResult.getAllErrors());

            model.addAttribute("errorMessage", bindingResult.getFieldError().getDefaultMessage());

            return "/forms/signup";
        }

        userService.registerUser(userFormData.getUsername(), userFormData.getEmail(), userFormData.getBirthDate(), userFormData.getPassword(), userFormData.getRepeatPassword());

        redirectAttributes.addFlashAttribute("success", "User registered successfully!");

        return "redirect:/login";
    }
    
    

}
