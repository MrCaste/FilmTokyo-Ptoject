package com.filmtokio.RestController;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.filmtokio.DTO.ReviewDTO;
import com.filmtokio.RestDTO.CreateReviewRequest;
import com.filmtokio.RestDTO.RatingAverageResponse;
import com.filmtokio.RestDTO.UpdateReviewRequest;
import com.filmtokio.Service.RatingRestService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReviewApiController {

    public final RatingRestService ratingRestService;

    @PostMapping("/ratings")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> createRating(@Valid @RequestBody CreateReviewRequest request) {

        ratingRestService.createRating(request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/review/films/{filmId}/users/{userId}")
    public ResponseEntity<ReviewDTO> getReview(
            @PathVariable Long filmId,
            @PathVariable Long userId) {

        return ResponseEntity.ok(ratingRestService.getRating(filmId, userId));
    }

    @GetMapping("/ratings-average/films/{filmId}")
    public ResponseEntity<RatingAverageResponse> getAverage(
            @PathVariable Long filmId) {

        return ResponseEntity.ok(ratingRestService.getAverage(filmId));
    }

    @GetMapping("/review/films/{filmId}")
    public ResponseEntity<List<ReviewDTO>> getMovieReviews(
            @PathVariable Long filmId) {

        return ResponseEntity.ok(
                ratingRestService.getMovieReviews(filmId));
    }

    @GetMapping("/ratings/films/{filmId}/users/{userId}/exists")
    public Boolean hasReviewed(
            @PathVariable Long filmId,
            @PathVariable Long userId) {

        return ratingRestService.hasReviewed(filmId, userId);
    }

    @PutMapping("/ratings/{reviewId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateReview(
            @PathVariable Long reviewId,
            @RequestBody @Valid UpdateReviewRequest request) {

        ratingRestService.updateReview(reviewId, request);
    }
}