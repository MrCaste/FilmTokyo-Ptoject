package com.filmtokio.FormData;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class ReviewFormData {

    @NotNull (message = "El id no puedo ser nulo")
    private Long id;

    private Integer rating;

    @Size (min = 10, max = 1000, message = "Debe estar entre 10 y 1000 caracteres")
    private String comment;

}
