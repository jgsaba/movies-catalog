package test.raven.moviescatalog.integration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;
import test.raven.moviescatalog.entities.Movie;
import test.raven.moviescatalog.integration.utils.TestUtils;
import test.raven.moviescatalog.model.DeleteMovieRating;
import test.raven.moviescatalog.model.MovieRatingDTO;
import test.raven.moviescatalog.model.RatingDTO;
import test.raven.moviescatalog.repositories.MovieRepository;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Import(TestConfig.class)
public class RatingsIntegrationTests extends BaseIntegrationTests{

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private TestUtils utils;

    @Test
    public void rateMovie(){

        // Add new ratings
        ResponseEntity<Void> response1 = rateMovie(2L, 1, utils.getValidUserRoleSecurityHeader());
        ResponseEntity<Void> response2 = rateMovie(2L, 3, utils.getValidAdminRoleSecurityHeader());

        assertEquals(HttpStatus.CREATED, response1.getStatusCode());
        assertEquals(HttpStatus.CREATED, response2.getStatusCode());

        // Get the new ratings
        ResponseEntity<Collection<RatingDTO>> rates1 = getRates(utils.getValidUserRoleSecurityHeader());
        ResponseEntity<Collection<RatingDTO>> rates2 = getRates(utils.getValidAdminRoleSecurityHeader());

        assertEquals(HttpStatus.OK, rates1.getStatusCode());
        assertEquals(1, rates1.getBody().size());

        assertEquals(HttpStatus.OK, rates2.getStatusCode());
        assertEquals(1, rates2.getBody().size());


        // Verify movie has correct rating
        Movie movie = movieRepository.findById(2L).get();
        assertEquals(2, movie.getRating());

        // Delete a rating
        DeleteMovieRating deleteMovieRating = new DeleteMovieRating();
        deleteMovieRating.setMovieId(movie.getId());

        HttpEntity<DeleteMovieRating> request = new HttpEntity<>(deleteMovieRating, utils.getValidAdminRoleSecurityHeader());

        ResponseEntity<Void> exchange = restTemplate.exchange(
                "/ratings",
                HttpMethod.DELETE,
                request,
                Void.class);

        assertEquals(HttpStatus.OK, exchange.getStatusCode());

        // Verify movie has correct rating
        movie = movieRepository.findById(2L).get();
        assertEquals(1, movie.getRating());
    }

    private ResponseEntity<Collection<RatingDTO>> getRates(HttpHeaders headers){
        HttpEntity<Collection<RatingDTO>> request = new HttpEntity<>(null, headers);
        return restTemplate.exchange(
                "/ratings/mine",
                HttpMethod.GET,
                request,
                new ParameterizedTypeReference<>() {});
    }

    private ResponseEntity<Void> rateMovie(Long movieId, int rating, HttpHeaders headers){

        MovieRatingDTO movieRatingDTO = new MovieRatingDTO(movieId, rating);
        HttpEntity<MovieRatingDTO> request2 = new HttpEntity<>(movieRatingDTO, headers);

        return restTemplate.exchange(
                "/ratings/rate",
                HttpMethod.POST,
                request2,
                Void.class);
    }
}
