package com.filmtokio.batchDTO;

import com.filmtokio.Entities.Movies;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExportMovie {

    private Movies movie;
    private String csvLine;

}
