package test.raven.moviescatalog.model;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class DeleteMovieDTO {

    @NotNull
    private Long movieId;
}
