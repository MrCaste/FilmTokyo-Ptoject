package com.filmtokio.Repositorys;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.filmtokio.Entities.MovieArtist;

public interface MovieArtistRepository extends CrudRepository<MovieArtist, Long> {

    Optional<MovieArtist> findByMovieId(Long movieId);

}
