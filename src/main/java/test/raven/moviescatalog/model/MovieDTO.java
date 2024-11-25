package test.raven.moviescatalog.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import test.raven.moviescatalog.entities.Category;
import test.raven.moviescatalog.entities.Movie;
import test.raven.moviescatalog.entities.MovieProjection;

import java.util.Set;
import java.util.stream.Collectors;

@Data
public class MovieDTO {

    private String title;

    private int releaseYear;

    private String synopsis;

    private float rating;

    private Set<String> categories;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String poster;

    public static MovieDTO fromProjection(MovieProjection movie){
        MovieDTO movieDTO = new MovieDTO();

        movieDTO.setTitle(movie.getName());
        movieDTO.setReleaseYear(movie.getReleaseYear());
        movieDTO.setSynopsis(movie.getSynopsis());
        movieDTO.setRating(movie.getRating());
        movieDTO.setPoster(movie.getPoster());

        movieDTO.setCategories(movie.getCategories()
                .stream()
                .map(Category::getCategory)
                .collect(Collectors.toSet()));

        return movieDTO;
    }

    public static MovieDTO fromEntity(Movie movie){
        MovieDTO movieDTO = new MovieDTO();

        movieDTO.setTitle(movie.getName());
        movieDTO.setReleaseYear(movie.getReleaseYear());
        movieDTO.setSynopsis(movie.getSynopsis());
        movieDTO.setRating(movie.getRating());
        movieDTO.setPoster(movie.getPoster());

        movieDTO.setCategories(movie.getCategories()
                .stream()
                .map(Category::getCategory)
                .collect(Collectors.toSet()));

        return movieDTO;
    }
}
