package com.filmtokio.RestDTO;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateReviewRequest {

    @NotNull
    private Long filmId;

    @NotNull
    private String email;

    @Min(1)
    @Max(5)
    private Integer rating;

    @Size (min = 10, max = 1000, message = "Debe estar entre 10 y 1000 caracteres")
    private String comment;
}
