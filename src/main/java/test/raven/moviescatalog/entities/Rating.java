package test.raven.moviescatalog.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Rating {

    @EmbeddedId
    private RatingId ratingId;

    @ManyToOne
    @MapsId("movieId")
    @JoinColumn(name = "movie_id", nullable = false, updatable = false)
    private Movie movie;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private RegularUser user;

    @Column(name = "stars", nullable = false)
    private int stars;

    public Rating(Movie movie, RegularUser user, int stars) {
        this.movie = movie;
        this.user = user;
        this.stars = stars;
        this.ratingId = new RatingId(movie.getId(), user.getId());
    }
}
