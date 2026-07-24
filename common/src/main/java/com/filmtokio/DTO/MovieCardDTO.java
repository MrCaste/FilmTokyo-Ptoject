package com.filmtokio.DTO;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieCardDTO {

    private Long id;
    private String title;
    private Integer releaseYear;
    private String poster;
    private Double averageRating;
    private ArtistDTO director;
    private List<ArtistDTO> actors;

}
