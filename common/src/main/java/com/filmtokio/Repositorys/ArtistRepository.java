package com.filmtokio.Repositorys;

import org.springframework.data.repository.CrudRepository;

import com.filmtokio.Entities.Artists;

import java.util.List;


public interface ArtistRepository  extends CrudRepository<Artists, Long>{

    List<Artists> findAll();

    List<Artists> findByIdIn(List<Long> ids);

}
