package test.raven.moviescatalog.services;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import test.raven.moviescatalog.entities.Movie;
import test.raven.moviescatalog.entities.Rating;
import test.raven.moviescatalog.entities.RatingId;
import test.raven.moviescatalog.entities.RegularUser;
import test.raven.moviescatalog.exceptions.DuplicateRatingException;
import test.raven.moviescatalog.exceptions.MovieNotFoundException;
import test.raven.moviescatalog.model.DeleteMovieRating;
import test.raven.moviescatalog.model.MovieRatingDTO;
import test.raven.moviescatalog.model.RatingDTO;
import test.raven.moviescatalog.repositories.RatingRepository;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RatingsService {

    private final RatingRepository ratingRepository;

    private final UsersService usersService;

    private final MoviesService moviesService;

    public void rateMovie(MovieRatingDTO payload, String authorizationHeader){

        RegularUser user = usersService.getUserFromAuthorization(authorizationHeader);
        Movie movie = moviesService.findMovie(payload.getMovieId());

        RatingId ratingId = new RatingId();
        ratingId.setUserId(user.getId());
        ratingId.setMovieId(movie.getId());

        if (ratingRepository.existsById(ratingId)){
            throw new DuplicateRatingException();
        }

        Rating rating = new Rating(movie, user, payload.getRating());

        ratingRepository.save(rating);
    }

    public Collection<RatingDTO> getUserRatings(String authorizationHeader){
        RegularUser user = usersService.getUserFromAuthorization(authorizationHeader);
        return ratingRepository.findRatingsOfUser(user.getId())
                .stream()
                .map(RatingDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public void removeRating(DeleteMovieRating payload, String authorizationHeader){

        RegularUser user = usersService.getUserFromAuthorization(authorizationHeader);
        if (!moviesService.checkIfMovieExists(payload.getMovieId())){
            throw new MovieNotFoundException();
        }

        RatingId ratingId = new RatingId();
        ratingId.setUserId(user.getId());
        ratingId.setMovieId(payload.getMovieId());

        try {
            ratingRepository.deleteById(ratingId);
        } catch (EmptyResultDataAccessException ignored){}

    }
}
