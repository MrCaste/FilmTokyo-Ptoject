package com.filmtokio.FormData;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MovieFormData {

    @NotBlank (message = "A tittle is required")
    private String title;

    @NotNull (message = "A year is required")
    @Positive (message = "Number cant be negative")
    @Min (1)
    @Max (9999)
    private Integer releaseYear;

    @NotNull (message = "Select a director")
    private Long director;

    @NotNull (message = "Select actors")
    private List<Long> actorList;

    @NotNull (message = "Add a poster")
    private MultipartFile poster;

}
