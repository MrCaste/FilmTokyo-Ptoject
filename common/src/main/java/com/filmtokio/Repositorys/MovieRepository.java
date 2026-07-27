package com.filmtokio.Repositorys;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.filmtokio.Entities.Movies;

public interface MovieRepository extends CrudRepository<Movies, Long> {

    List<Movies> findAll();

    List<Movies> findByTitleContainingIgnoreCase(String title);

    @Query("""
    SELECT m
    FROM Movies m
    WHERE m.id NOT IN (
        SELECT e.filmId
        FROM ExportedFilm e
    )
    """)
    List<Movies> findMoviesNotExported();


}
