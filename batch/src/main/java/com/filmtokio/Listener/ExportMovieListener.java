package com.filmtokio.Listener;

import org.springframework.batch.core.listener.ItemWriteListener;
import org.springframework.batch.core.scope.context.JobSynchronizationManager;
import org.springframework.batch.infrastructure.item.Chunk;
import org.springframework.stereotype.Component;

import com.filmtokio.Entity.ExportedFilm;
import com.filmtokio.Repository.ExportFilmRepository;
import com.filmtokio.batchDTO.ExportMovie;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ExportMovieListener implements ItemWriteListener<ExportMovie> {

    private final ExportFilmRepository exportFilmRepository;

    @Override
    public void afterWrite(Chunk<? extends ExportMovie> chunk) {

        Long jobId = JobSynchronizationManager.getContext()
                .getJobExecution()
                .getId();

        for (ExportMovie movie : chunk.getItems()) {

            ExportedFilm exported = new ExportedFilm();

            exported.setJobId(jobId);
            exported.setFilmId(movie.getMovie().getId());
            exported.setExportedAt(movie.getExportedAt()); // <-- usa el objeto del chunk

            exportFilmRepository.save(exported);
        }
    }
}
