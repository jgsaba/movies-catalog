package test.raven.moviescatalog.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private int releaseYear;

    private String synopsis;

    @ManyToMany
    @JoinTable(
            name = "movie_category",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<Category> categories = new HashSet<>();

    @CreationTimestamp
    private LocalDate createdAt;

    @Formula("(SELECT COALESCE(AVG(r.stars), 0) from Rating r where r.movie_id = id)")
    private float rating;

    @ManyToOne
    @JoinColumn(name="created_by", nullable=false, updatable = false)
    private RegularUser createdBy;

    private String poster;
}
