package com.filmtokio.Service.Movies;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.filmtokio.DTO.ArtistDTO;
import com.filmtokio.DTO.MovieCardDTO;
import com.filmtokio.Entities.Artists;
import com.filmtokio.Entities.MovieArtist;
import com.filmtokio.Entities.Movies;
import com.filmtokio.Enum.ArtistType;
import com.filmtokio.FormData.MovieFormData;
import com.filmtokio.Repositorys.MovieArtistRepository;
import com.filmtokio.Repositorys.MovieRepository;
import com.filmtokio.Service.Artists.ArtistService;
import com.filmtokio.Service.Reviews.ReviewRestService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class MovieService {

    public final ArtistService artistService;
    public final MovieRepository movieRepository;
    public final FileStorageService fileStorageService;
    public final ReviewRestService reviewService;
    public final MovieArtistRepository movieArtistRepository;

    public void createMovie (MovieFormData formData) throws IOException {

        Artists director = artistService.getDirector(formData.getDirector())
                .orElseThrow(() -> new EntityNotFoundException("Director not found"));

        List<Artists> actorsList = artistService.getActors(formData.getActorList());

        Movies movie = Movies.builder()
            .title(formData.getTitle())
            .releaseYear(formData.getReleaseYear())
            .poster(fileStorageService.savePoster(formData.getPoster()))
            .build();

        movie.getMovieArtists().add(createMovieArtist(movie, director, ArtistType.DIRECTOR));

        for (Artists actor: actorsList) {

            movie.getMovieArtists().add(createMovieArtist(movie, actor, ArtistType.ACTOR));
        }

        movieRepository.save(movie);
    }

    public void updateMovie(MovieFormData movieFormData, Long movieId) throws IOException {

        Artists director = artistService.getDirector(movieFormData.getDirector())
                .orElseThrow(() -> new EntityNotFoundException("Director not found"));

        List<Artists> actorsList = artistService.getActors(movieFormData.getActorList());

        Movies movie = movieRepository.findById(movieId).orElseThrow(() -> new EntityNotFoundException("No se encontro la pelicula"));

        movie.setTitle(movieFormData.getTitle());
        movie.setReleaseYear(movieFormData.getReleaseYear());
        movie.getMovieArtists().clear();
        movie.getMovieArtists().add(createMovieArtist(movie, director, ArtistType.DIRECTOR));

        for (Artists actor: actorsList) {

            movie.getMovieArtists().add(createMovieArtist(movie, actor, ArtistType.ACTOR));
        }

        // Solo actualizar el poster si el usuario ha seleccionado uno nuevo
        if (movieFormData.getPoster() != null && !movieFormData.getPoster().isEmpty()) {

            String fileName = UUID.randomUUID() + "_" + movieFormData.getPoster().getOriginalFilename();

            Path path = Paths.get("uploads", fileName);
            Files.copy(movieFormData.getPoster().getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            movie.setPoster(fileName);
        }

        movieRepository.save(movie);
    }

    public MovieArtist createMovieArtist (Movies movie, Artists artist, ArtistType role) {

        return MovieArtist.builder()
            .movie(movie)
            .artist(artist)
            .role(role)
            .build();
    }

    public List<MovieCardDTO> getAllMovies() {

        return movieRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public Optional<MovieCardDTO> getMovieById(Long movieId) {

        return movieRepository.findById(movieId).map(this::toDTO);
    }

    public List<MovieCardDTO> search(String title) {

        if (title == null || title.isBlank()) {
            return getAllMovies();
        }

        return movieRepository
                .findByTitleContainingIgnoreCase(title)
                .stream()
                .map(this::toDTO)
                .toList();
    }






    private ArtistDTO getDirector(Movies movie) {
    return movie.getMovieArtists().stream()
            .filter(movieArtist -> movieArtist.getRole() == ArtistType.DIRECTOR)
            .findFirst()
            .map(movieArtist -> artistService.toDTO(movieArtist.getArtist()))
            .orElse(null);
}

    private List<ArtistDTO> getActors(Movies movie) {
        return movie.getMovieArtists().stream()
                .filter(movieArtist -> movieArtist.getRole() == ArtistType.ACTOR)
                .map(movieArtist -> artistService.toDTO(movieArtist.getArtist()))
                .toList();
    }

    private MovieCardDTO toDTO(Movies movie) {

    Double averageRating = reviewService.getAverageRating(movie.getId());

    return MovieCardDTO.builder()
            .id(movie.getId())
            .title(movie.getTitle())
            .releaseYear(movie.getReleaseYear())
            .poster(movie.getPoster())
            .averageRating(averageRating)
            .director(getDirector(movie))
            .reviewServiceAvailable(averageRating != null)
            .actors(getActors(movie))
            .build();
    }   

}
