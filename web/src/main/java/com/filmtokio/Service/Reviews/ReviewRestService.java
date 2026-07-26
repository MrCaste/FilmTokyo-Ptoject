package com.filmtokio.Service.Reviews;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.filmtokio.DTO.ReviewDTO;
import com.filmtokio.FormData.ReviewFormData;
import com.filmtokio.RestDTO.CreateReviewRequest;
import com.filmtokio.RestDTO.RatingAverageResponse;
import com.filmtokio.RestDTO.UpdateReviewRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewRestService {

    private final RestClient restClient;
    

    public List<ReviewDTO> getMovieReviews(Long movieId) {

        return restClient.get()
                .uri("/api/review/films/{id}", movieId)
                .retrieve()
                .body(new ParameterizedTypeReference<List<ReviewDTO>>() {});
    }

    public ReviewDTO getUserReview(Long movieId, Long userId) {

        return restClient.get()
                .uri("/api/review/films/{filmId}/users/{userId}", movieId, userId)
                .retrieve()
                .body(ReviewDTO.class);
    }

    public void createReview(ReviewFormData formData) {

        CreateReviewRequest request = CreateReviewRequest.builder()
                .filmId(formData.getId())
                .rating(formData.getRating())
                .comment(formData.getComment())
                .build();

        restClient.post()
                .uri("/api/ratings")
                .body(request)
                .retrieve()
                .toBodilessEntity();
    }

    public boolean hasReviewed(Long movieId, Long userId) {

        return restClient.get()
                .uri("/api/ratings/films/{filmId}/users/{userId}/exists", movieId, userId)
                .retrieve()
                .body(Boolean.class);
    }

    public void updateReview(Long reviewId, ReviewFormData formData) {

        UpdateReviewRequest request = UpdateReviewRequest.builder()
                .rating(formData.getRating())
                .comment(formData.getComment())
                .build();

        restClient.put()
                .uri("/api/ratings/{reviewId}", reviewId)
                .body(request)
                .retrieve()
                .toBodilessEntity();
    }

    public Double getAverageRating(Long movieId) {

        RatingAverageResponse response = restClient.get()
                .uri("/api/ratings-average/films/{filmId}", movieId)
                .retrieve()
                .body(RatingAverageResponse.class);

        return response.getAverage();
    }
}
