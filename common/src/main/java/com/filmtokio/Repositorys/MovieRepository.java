package com.filmtokio.Repositorys;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.filmtokio.Entities.Movies;

public interface MovieRepository extends CrudRepository<Movies, Long> {

    List<Movies> findAll();

    List<Movies> findByTitleContainingIgnoreCase(String title);

}
