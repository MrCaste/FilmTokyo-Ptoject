package com.filmtokio.Service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.filmtokio.DTO.ReviewDTO;
import com.filmtokio.Entities.Movies;
import com.filmtokio.Entities.Reviews;
import com.filmtokio.Entities.User;
import com.filmtokio.Repositorys.MovieRepository;
import com.filmtokio.Repositorys.ReviewsRepository;
import com.filmtokio.Repositorys.UserRepository;
import com.filmtokio.RestDTO.CreateReviewRequest;
import com.filmtokio.RestDTO.RatingAverageResponse;
import com.filmtokio.RestDTO.UpdateReviewRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RatingRestService {

        public final MovieRepository movieRepository;
        public final UserRepository userRepository;
        public final ReviewsRepository reviewsRepository;

        public void createRating(CreateReviewRequest request) {

                Movies movie = movieRepository.findById(request.getFilmId())
                        .orElseThrow(() ->
                                new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                        "Película inexistente"));

                User user = userRepository.findByEmailOrUsername(request.getEmail())
                        .orElseThrow(() ->
                                new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                        "Usuario inexistente"));

                if (reviewsRepository.existsByMovieAndUser(movie, user)) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Ya existe una valoración para este usuario y película");
                }

                Reviews review = Reviews.builder()
                        .movie(movie)
                        .user(user)
                        .rating(request.getRating())
                        .comment(request.getComment())
                        .createdAt(LocalDateTime.now())
                        .build();

                Reviews saved = reviewsRepository.save(review);

                System.out.println("Review guardada: " + saved.getId());
        }

        public ReviewDTO getRating(Long movieId, Long userId) {

                return reviewsRepository
                        .findByMovieIdAndUserId(movieId, userId)
                        .map(review -> ReviewDTO.builder()
                                .comment(review.getComment())
                                .createdAt(review.getCreatedAt())
                                .username(review.getUser().getUsername())
                                .rating(review.getRating())
                                .build())
                        .orElse(null);
        }

        public RatingAverageResponse getAverage(Long filmId) {

                Double average = reviewsRepository.getAverageRating(filmId);

                if (average == null) {
                        average = 0.0;
                }

                average = Math.ceil(average * 100) / 100;

                Long ratings = reviewsRepository.countByMovieId(filmId);

                return RatingAverageResponse.builder()
                        .average(average)
                        .ratings(ratings)
                        .build();
        }

        public List<ReviewDTO> getMovieReviews(Long filmId) {

                return reviewsRepository.findByMovieId(filmId)
                        .stream()
                        .map(this::toDTO)
                        .toList();
        }



        private ReviewDTO toDTO(Reviews review) {

                return ReviewDTO.builder()
                        .id(review.getId())
                        .username(review.getUser().getUsername())
                        .comment(review.getComment())
                        .rating(review.getRating())
                        .createdAt(review.getCreatedAt())
                        .build();
        }

        public Boolean hasReviewed(Long filmId, Long userId) {
                return reviewsRepository.existsByMovieIdAndUserId(filmId, userId);
        }

        public void updateReview(Long reviewId, UpdateReviewRequest request) {

                Reviews review = reviewsRepository.findById(reviewId)
                        .orElseThrow(() ->
                                new ResponseStatusException(
                                        HttpStatus.NOT_FOUND, "Valoración no encontrada"));

                review.setRating(request.getRating());
                review.setComment(request.getComment());

                reviewsRepository.save(review);
        }
}
