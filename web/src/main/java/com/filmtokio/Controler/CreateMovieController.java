package com.filmtokio.Controler;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.filmtokio.FormData.MovieFormData;
import com.filmtokio.Service.Artists.ArtistService;
import com.filmtokio.Service.Movies.MovieService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@Slf4j
@RequiredArgsConstructor
public class CreateMovieController {

    public final MovieService movieService;

    public final ArtistService artistService;

    @GetMapping("movies/new")
    public String createMoviesForm(Model model) {


        model.addAttribute("artistList", artistService.getAllArtists());

        return "/forms/createFilm";
    }

    @PostMapping("movies/create")
    public String createUpdateMovie(Model model, @Valid @ModelAttribute MovieFormData createMovieFormData,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes) throws IOException {

        if (bindingResult.hasErrors()) {

            log.error("Validation errors {}", bindingResult.getAllErrors());

            model.addAttribute("errorMessage", bindingResult.getFieldError().getDefaultMessage());
        }

        redirectAttributes.addFlashAttribute("success", "Movie added successfully!");

        movieService.createMovie(createMovieFormData);
        
        return "redirect:/movies/new";
    }
    

}
