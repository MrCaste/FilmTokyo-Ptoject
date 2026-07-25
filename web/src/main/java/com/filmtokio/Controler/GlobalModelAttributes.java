package com.filmtokio.Controler;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalModelAttributes {

    @ModelAttribute
    public void addAttributes(HttpServletRequest request, Model model) {

        model.addAttribute(
                "showSearch",
                request.getRequestURI().startsWith("/movies/search")
        );
    }
}
