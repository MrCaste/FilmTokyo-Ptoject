package com.filmtokio.DTO;

import java.time.LocalDateTime;

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
public class ReviewDTO {

    private Long id;
    private String username;
    private String comment;
    private Integer rating;
    private LocalDateTime createdAt;

}
