package test.raven.moviescatalog.model;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMovieDTO {

    @Positive
    @NotNull
    private Long id;

    @NotBlank
    private String title;

    @Positive
    private int releaseYear;

    @NotBlank
    private String synopsis;

    @NotEmpty
    private Set<String> categories;
}
