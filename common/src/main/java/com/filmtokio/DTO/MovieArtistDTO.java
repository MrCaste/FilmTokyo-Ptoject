package com.filmtokio.DTO;

import com.filmtokio.Entities.Artists;
import com.filmtokio.Entities.Movies;
import com.filmtokio.Enum.ArtistType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieArtistDTO {

    private Movies movie;
    private Artists artist;
    private ArtistType role;

}
