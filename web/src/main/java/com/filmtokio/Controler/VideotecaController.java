package com.filmtokio.Controler;

import com.filmtokio.Service.Artists.ArtistService;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.filmtokio.DTO.MovieCardDTO;
import com.filmtokio.DTO.ReviewDTO;
import com.filmtokio.FormData.MovieFormData;
import com.filmtokio.Service.Movies.MovieService;
import com.filmtokio.Service.Reviews.ReviewService;
import com.filmtokio.Service.Users.UserService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/movies")
public class VideotecaController {

    private final ArtistService artistService;
    public final MovieService movieService;
    public final ReviewService reviewService;
    public final UserService userService;

    @GetMapping("/search")
    public String searchMovies(
            @RequestParam(required = false) String title,
            Model model,
            HttpServletRequest request) {

        List<MovieCardDTO> movies = movieService.search(title);

        model.addAttribute("movieCardList", movies);

        if ("true".equalsIgnoreCase(request.getHeader("HX-Request"))) {
            return "fragments/movie-list :: movieList";
        }

        return "vistas/videoteca";
    }

    @GetMapping("/{id}")
    public String getFilmsDetails(@PathVariable Long id, Model model) {
        
        MovieCardDTO movie = movieService.getMovieById(id)
        .orElseThrow(() -> new EntityNotFoundException("Película no encontrada"));
    
        List<ReviewDTO> reviews = reviewService.getMoviesReviews(id);

        model.addAttribute("movie", movie);
        model.addAttribute("reviews", reviews);

        if (userService.isAuthenticated()) {
            model.addAttribute("userReview", reviewService.getUserReview(id, userService.getAuthenticatedUser().getId()).orElse(null));
        }

        return "vistas/movie";
    }

    @GetMapping("/edit/{id}")
    public String getMethodName(@PathVariable Long id, Model model) {
        
        MovieCardDTO movie = movieService.getMovieById(id).orElseThrow(() -> new EntityNotFoundException("Pelicula no encontrada"));

        model.addAttribute("movie", movie);
        model.addAttribute("artistList", artistService.getAllArtists());

        return "forms/createFilm";
    }

    @PostMapping("/edit/{id}")
    public String updateMovie(@PathVariable Long id,Model model,
                              @Valid @ModelAttribute MovieFormData movieFormData,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes) throws IOException {

        if (bindingResult.hasErrors()) {
            model.addAttribute("errorMessage", bindingResult.getFieldError().getDefaultMessage());
            model.addAttribute("movie", movieFormData);
            return "forms/createFilm";
        }

        movieService.updateMovie(movieFormData);

        redirectAttributes.addFlashAttribute("success", "Movie updated successfully!");

        return "redirect:/movies/edit/" + id;
    }
    
}
