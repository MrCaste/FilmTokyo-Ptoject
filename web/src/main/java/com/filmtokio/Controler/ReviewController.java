package com.filmtokio.Controler;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.filmtokio.FormData.ReviewFormData;
import com.filmtokio.Service.Reviews.ReviewService;
import com.filmtokio.Service.Users.UserService;

import jakarta.validation.Valid;


@Controller
@Slf4j
@RequiredArgsConstructor
public class ReviewController {

    public final ReviewService reviewService;
    public final UserService userService;

    @PostMapping("/movies/{id}/review")
    public String postMethodName(Model model, @PathVariable Long id, @Valid @ModelAttribute ReviewFormData reviewFormData,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {

            log.error("Validation errors {}", bindingResult.getAllErrors());

            model.addAttribute("errors", bindingResult.getFieldError().getDefaultMessage());

            return "redirect:/movies/" + id;
        }
        
        if (reviewService.hasReviewed(id, userService.getAuthenticatedUser().getId())) {
            reviewService.updateReview(reviewFormData.getId(), userService.getAuthenticatedUser(), reviewFormData);
            redirectAttributes.addFlashAttribute("success", "Review updated successfully!");
        } else {
            reviewService.createReview(reviewFormData, userService.getAuthenticatedUser());
            redirectAttributes.addFlashAttribute("success", "Review created successfully!");
        }
        
        return "redirect:/movies/" + id;
    }
    

}
