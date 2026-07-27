package com.filmtokio.Repository;

import org.springframework.data.repository.CrudRepository;

import com.filmtokio.Entity.ExportedFilm;

public interface ExportFilmRepository extends CrudRepository<ExportedFilm, Long>{

}
