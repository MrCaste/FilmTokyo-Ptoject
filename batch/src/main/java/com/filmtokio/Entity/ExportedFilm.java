package com.filmtokio.Entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "film_exports")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExportedFilm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "job_id")
    private Long jobId;
    @Column(name = "film_id")
    private Long filmId;
    @Column(name = "exported_at")
    private LocalDateTime exportedAt;

}
