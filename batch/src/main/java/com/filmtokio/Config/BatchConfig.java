package com.filmtokio.Config;

import java.util.Map;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.data.RepositoryItemReader;
import org.springframework.batch.infrastructure.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.infrastructure.item.file.FlatFileItemWriter;
import org.springframework.batch.infrastructure.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Sort;

import com.filmtokio.Entities.Movies;
import com.filmtokio.Listener.ExportMovieListener;
import com.filmtokio.Processor.MovieProcessor;
import com.filmtokio.Repository.MovieBatchRepository;
import com.filmtokio.batchDTO.ExportMovie;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    public BatchConfig() {
        System.out.println("BATCH CONFIG CARGADA");
    }

    @Bean
    public RepositoryItemReader<Movies> movieReader(MovieBatchRepository repository) {

        return new RepositoryItemReaderBuilder<Movies>()
            .name("movieReader")
            .repository(repository)
            .methodName("findMoviesNotExported")
            .sorts(Map.of("id", Sort.Direction.ASC))
            .build();
    }

    @Bean
    public FlatFileItemWriter<ExportMovie> movieWriter() {

        return new FlatFileItemWriterBuilder<ExportMovie>()
                .name("movieWriter")
                .resource(new FileSystemResource("export/movies.csv"))
                .lineAggregator(ExportMovie::getCsvLine)
                .append(true)
                .shouldDeleteIfExists(false)
                .build();
    }

    @Bean
    public Step exportMoviesStep(JobRepository jobRepository,
                                RepositoryItemReader<Movies> movieReader,
                                MovieProcessor movieProcessor,
                                FlatFileItemWriter<ExportMovie> movieWriter,
                                ExportMovieListener exportMovieListener) {

        return new StepBuilder("exportMoviesStep", jobRepository)
                .<Movies, ExportMovie>chunk(10)
                .reader(movieReader)
                .processor(movieProcessor)
                .writer(movieWriter)
                .listener(exportMovieListener)
                .build();
    }

    @Bean
    public Job exportMoviesJob(JobRepository jobRepository,
                            Step exportMoviesStep) {

        System.out.println("CREANDO JOB");

        return new JobBuilder("exportMoviesJob", jobRepository)
                .start(exportMoviesStep)
                .build();
    }
}
