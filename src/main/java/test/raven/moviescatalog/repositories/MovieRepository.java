package test.raven.moviescatalog.repositories;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import test.raven.moviescatalog.entities.Movie;
import test.raven.moviescatalog.entities.MovieProjection;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    @Query("SELECT m FROM Movie m LEFT JOIN m.categories c " +
            "WHERE (:searchParam IS NULL OR LOWER(m.name) LIKE LOWER(CONCAT('%', :searchParam, '%')) OR LOWER(m.synopsis) LIKE LOWER(CONCAT('%', :searchParam, '%'))) " +
            "AND (:category IS NULL OR c.category = :category) " +
            "AND (:releaseYear IS NULL OR m.releaseYear = :releaseYear) " +
            "GROUP BY m.id")
    Page<MovieProjection> searchMovies(
            @Param("searchParam") String searchParam,
            @Param("category") String category,
            @Param("releaseYear") Integer releaseYear,
            Pageable pageable);
}
