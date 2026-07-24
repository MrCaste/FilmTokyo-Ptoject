package com.filmtokio.Service.Reviews;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.filmtokio.DTO.ReviewDTO;
import com.filmtokio.Entities.Movies;
import com.filmtokio.Entities.Reviews;
import com.filmtokio.Entities.User;
import com.filmtokio.FormData.ReviewFormData;
import com.filmtokio.Repositorys.MovieRepository;
import com.filmtokio.Repositorys.ReviewsRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService {

    public final MovieRepository movieRepository;
    public final ReviewsRepository reviewsRepository;

    public void createReview(ReviewFormData reviewFormData, User user) {

        Movies movie = movieRepository.findById(reviewFormData.getId())
                .orElseThrow();

        if(reviewsRepository.existsByMovieAndUser(movie, user)){
        throw new IllegalArgumentException("Ya has valorado esta película.");
        }

        reviewsRepository.save(Reviews.builder()
                .movie(movie)
                .user(user)
                .rating(reviewFormData.getRating())
                .comment(reviewFormData.getComment())
                .createdAt(LocalDateTime.now())
                .build());

    }

    public void updateReview(Long reviewId, ReviewFormData formData) {

        Optional<Reviews> optionalReview = reviewsRepository.findById(reviewId);

        if (optionalReview.isEmpty()) {
            throw new EntityNotFoundException("No existe la review con id " + reviewId);
        }

        Reviews review = optionalReview.get();

        review.setRating(formData.getRating());
        review.setComment(formData.getComment());

        reviewsRepository.save(review);
    }

    public List<ReviewDTO> getMoviesReviews (Long movieId) {

        return reviewsRepository.findByMovieId(movieId)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public Double getAverageRating(Long movieId) {

        Double average = reviewsRepository.getAverageRating(movieId);

        return average == null ? 0.0 : average;
    }

    public Long getReviewsCount(Long movieId) {
        return reviewsRepository.countByMovieId(movieId);
    }

    public boolean hasReviewed(Long movieId, Long userId) {

        return reviewsRepository.existsByMovieIdAndUserId(movieId, userId);
    }

    public Optional<ReviewDTO> getUserReview(Long movieId, Long userId) {

        return reviewsRepository.findByMovieIdAndUserId(movieId, userId).map(this::toDTO);
         
    }



    private ReviewDTO toDTO(Reviews review) {

        return ReviewDTO.builder()
            .id(review.getId())
            .username(review.getUser().getUsername())
            .rating(review.getRating())
            .comment(review.getComment())
            .createdAt(review.getCreatedAt())
            .build();
    }

}
