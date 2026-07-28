package com.filmtokio.Processor;


import java.time.LocalDateTime;

import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.filmtokio.Entities.Movies;
import com.filmtokio.batchDTO.ExportMovie;

@Component
public class MovieProcessor implements ItemProcessor<Movies, ExportMovie> {

    @Override
    public ExportMovie process(Movies movie) {

        LocalDateTime exportedAt = LocalDateTime.now();

        String line = movie.getId() + ","
                + movie.getTitle() + ","
                + movie.getReleaseYear() + ","
                + exportedAt;

        return new ExportMovie(movie, line, exportedAt);
    }
}
