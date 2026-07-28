package com.filmtokio.Service.Reviews;

import com.filmtokio.Config.RestClientConfig;

import java.util.Date;
import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

import com.filmtokio.DTO.ReviewDTO;
import com.filmtokio.Entities.User;
import com.filmtokio.FormData.ReviewFormData;
import com.filmtokio.RestDTO.AccessTokenResponse;
import com.filmtokio.RestDTO.CreateReviewRequest;
import com.filmtokio.RestDTO.RatingAverageResponse;
import com.filmtokio.RestDTO.UpdateReviewRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewRestService {

        private String accessToken;
        private Date tokenExpirationDate;

        private final RestClientConfig restClientConfig;
        private final RestClient restClient;
    

        public List<ReviewDTO> getMovieReviews(Long movieId) {

                try {
                        return restClient.get()
                                .uri("/api/review/films/{filmId}", movieId)
                                .headers(header -> header.setBearerAuth(getAccessToken()))
                                .retrieve()
                                .body(new ParameterizedTypeReference<List<ReviewDTO>>() {});

                } catch (ResourceAccessException e) {
                        log.warn("Servicio no disponible");
                        return null;
                }
        }

        public ReviewDTO getUserReview(Long movieId, Long userId) {

                try {
                        return restClient.get()
                                .uri("/api/review/films/{filmId}/users/{userId}", movieId, userId)
                                .headers(header -> header.setBearerAuth(getAccessToken()))
                                .retrieve()
                                .body(ReviewDTO.class);

                } catch (ResourceAccessException e) {
                        log.warn("Servicio no disponible");
                        return null;
                }
        }

        public void createReview(ReviewFormData formData, User user) {
                
                CreateReviewRequest request = CreateReviewRequest.builder()
                        .filmId(formData.getId())
                        .rating(formData.getRating())
                        .userName(user.getUsername())
                        .comment(formData.getComment())
                        .build();

                restClient.post()
                        .uri("/api/ratings")
                        .headers(header -> header.setBearerAuth(getAccessToken()))
                        .body(request)
                        .retrieve()
                        .toBodilessEntity();
        }

        public boolean hasReviewed(Long movieId, Long userId) {

                return restClient.get()
                        .uri("/api/ratings/films/{filmId}/users/{userId}/exists", movieId, userId)
                        .headers(header -> header.setBearerAuth(getAccessToken()))
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
                        .headers(header -> header.setBearerAuth(getAccessToken()))
                        .body(request)
                        .retrieve()
                        .toBodilessEntity();
        }

        public Double getAverageRating(Long movieId) {

                try {
                        RatingAverageResponse response = restClient.get()
                                .uri("/api/ratings-average/films/{filmId}", movieId)
                                .headers(header -> header.setBearerAuth(getAccessToken()))
                                .retrieve()
                                .body(RatingAverageResponse.class);

                        return response.getAverage();

                } catch (ResourceAccessException e) {
                        log.warn("Servicio no disponible");
                        return null;
                }
        }

        private String getAccessToken() {

                final Date now = new Date();

                if(accessToken == null || now.after(tokenExpirationDate)) {
                        final AccessTokenResponse response = restClient.post()
                                .uri("/authenticate")
                                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                .headers(header -> header.setBasicAuth(restClientConfig.getUser(), restClientConfig.getPassword()))
                                .body("grant_type=client_credentials")
                                .retrieve()
                                .body(AccessTokenResponse.class);

                                accessToken = response.getAccessToken();
                                tokenExpirationDate = new Date(now.getTime() + response.getExpiresIn() * 1000);
                }

                log.info("TOKEN = {}", accessToken);

                log.info("Access token expira en {} segundos", (tokenExpirationDate.getTime() - now.getTime()) / 1000);

                return accessToken;
        }
}
