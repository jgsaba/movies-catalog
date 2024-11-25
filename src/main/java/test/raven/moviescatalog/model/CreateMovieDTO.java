package test.raven.moviescatalog.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateMovieDTO {

    @NotBlank
    private String title;

    @NotBlank
    private String synopsis;

    @Positive
    private int releaseYear;

    @NotEmpty
    private Set<String> categories;
}
