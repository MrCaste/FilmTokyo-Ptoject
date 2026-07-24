package com.filmtokio.Repositorys;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.filmtokio.Entities.Movies;

public interface MovieRepository extends CrudRepository<Movies, Long> {

    List<Movies> findAll();

}
