package test.raven.moviescatalog.model;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class DeleteMovieRating {

    @NotNull
    private Long movieId;
}
