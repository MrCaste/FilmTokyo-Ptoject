package com.filmtokio.Processor;


import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.filmtokio.Entities.Movies;
import com.filmtokio.batchDTO.ExportMovie;

@Component
public class MovieProcessor implements ItemProcessor<Movies, ExportMovie> {

    @Override
    public ExportMovie process(Movies movie) {

        String line = movie.getId() + ","
                + movie.getTitle() + ","
                + movie.getReleaseYear();

        return new ExportMovie(movie, line);
    }
}
