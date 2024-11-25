package test.raven.moviescatalog.model;

import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MovieRatingDTO {

    @NotNull
    @Positive
    private Long movieId;

    @NotNull
    @Min(1)
    @Max(5)
    private Integer rating;
}
