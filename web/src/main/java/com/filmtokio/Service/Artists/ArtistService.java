package com.filmtokio.Service.Artists;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.filmtokio.DTO.ArtistDTO;
import com.filmtokio.Entities.Artists;
import com.filmtokio.Repositorys.ArtistRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ArtistService {

    public final ArtistRepository artistRepository;

    public void registerArtist(String name, String surname) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("The fields name and role are required");
        }

        artistRepository.save(Artists.builder()
                .name(name)
                .surname(surname)
                .build());

    }

    public List<Artists> getAllArtists() {

        return artistRepository.findAll();
    }


    public Optional<Artists> getDirector(Long directorId) {

        return artistRepository.findById(directorId);
    }

    public List<Artists> getActors(List<Long> ids) {

        return artistRepository.findByIdIn(ids);
    }

    public ArtistDTO toDTO(Artists artist) {

        return ArtistDTO.builder()
            .id(artist.getId())
            .name(artist.getName())
            .surname(artist.getSurname())
            .build();
    }
    

}
