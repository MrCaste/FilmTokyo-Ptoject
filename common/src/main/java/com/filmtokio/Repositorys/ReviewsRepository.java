package com.filmtokio.Repositorys;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.filmtokio.Entities.Movies;
import com.filmtokio.Entities.Reviews;
import com.filmtokio.Entities.User;

public interface ReviewsRepository extends CrudRepository<Reviews, Long> {

    boolean existsByMovieAndUser(Movies movie, User user);

    List<Reviews> findByMovieId(Long movieId);

    @Query("""
    SELECT AVG(r.rating)
    FROM Reviews r
    WHERE r.movie.id = :movieId
    """)
    Double getAverageRating(Long movieId);

    Long countByMovieId(Long movieId);

    Optional<Reviews> findByMovieIdAndUserId(Long movieId, Long userId);

    boolean existsByMovieIdAndUserId(Long movieId, Long userId);
}
