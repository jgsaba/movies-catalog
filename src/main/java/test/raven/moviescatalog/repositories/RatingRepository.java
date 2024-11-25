package test.raven.moviescatalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import test.raven.moviescatalog.entities.Rating;
import test.raven.moviescatalog.entities.RatingId;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating, RatingId> {

    @Query("select r from Rating r where r.user.id = :userId")
    List<Rating> findRatingsOfUser(Long userId);
}
