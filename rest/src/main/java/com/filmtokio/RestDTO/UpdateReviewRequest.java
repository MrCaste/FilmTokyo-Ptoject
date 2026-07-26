package com.filmtokio.RestDTO;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateReviewRequest {

    @Min(1)
    @Max(5)
    private Integer rating;

    @Size (min = 10, max = 1000, message = "Debe estar entre 10 y 1000 caracteres")
    private String comment;
}
