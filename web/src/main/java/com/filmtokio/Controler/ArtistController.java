package com.filmtokio.Controler;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.filmtokio.FormData.ArtistFormData;
import com.filmtokio.Service.Artists.ArtistService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ArtistController {

    public final ArtistService artistService;

    @GetMapping("/artists/new") 
    public String createArtistForm() {

        return "forms/artist";
    }

    @PostMapping("/artists/new")
    public String createArtist(Model model, @Valid @ModelAttribute ArtistFormData artistFormData,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {

            log.error("Validation errors: {}", bindingResult.getAllErrors());

            model.addAttribute("errorMessage", bindingResult.getFieldError().getDefaultMessage());

            return "forms/artist";
        }

        redirectAttributes.addFlashAttribute("success", "Artist registered successfully!");

        artistService.registerArtist(artistFormData.getName(), artistFormData.getSurname());
            
        return "redirect:/artists/new";
    }
    

}
