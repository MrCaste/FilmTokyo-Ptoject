package com.filmtokio.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.filmtokio.Entities.Movies;

public interface MovieBatchRepository extends JpaRepository<Movies, Long>{

    @Query("""
    SELECT m
    FROM Movies m
    WHERE NOT EXISTS (
        SELECT e
        FROM ExportedFilm e
        WHERE e.filmId = m.id
    )
    """)
    Page<Movies> findMoviesNotExported(Pageable pageable);

}
