package test.raven.moviescatalog.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Embeddable
public class RatingId implements java.io.Serializable {

    private static final long serialVersionUID = 3752174899357532518L;

    @Column(name = "movie_id")
    private Long movieId;

    @Column(name = "user_id")
    private Long userId;
}
