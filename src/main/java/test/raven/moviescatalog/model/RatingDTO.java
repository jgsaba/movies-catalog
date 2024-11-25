package test.raven.moviescatalog.model;

import lombok.Data;
import test.raven.moviescatalog.entities.Rating;

@Data
public class RatingDTO {

    private MovieDTO movie;
    private int stars;

    public static RatingDTO fromEntity(Rating rating) {
        RatingDTO ratingDTO = new RatingDTO();
        ratingDTO.setStars(rating.getStars());
        ratingDTO.setMovie(MovieDTO.fromEntity(rating.getMovie()));

        return ratingDTO;
    }
}
