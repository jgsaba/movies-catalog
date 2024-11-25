package test.raven.moviescatalog.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import test.raven.moviescatalog.model.DeleteMovieRating;
import test.raven.moviescatalog.model.MovieRatingDTO;
import test.raven.moviescatalog.model.RatingDTO;
import test.raven.moviescatalog.services.RatingsService;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("ratings")
@RequiredArgsConstructor
public class RatingController {

    private final RatingsService service;

    @PostMapping("/rate")
    @SecurityRequirement(name = "bearerAuth")
    @ResponseStatus(HttpStatus.CREATED)
    public void rate(@RequestBody @Valid MovieRatingDTO payload, @RequestHeader("Authorization") String authorizationHeader){
        service.rateMovie(payload, authorizationHeader);
    }

    @GetMapping("mine")
    @SecurityRequirement(name = "bearerAuth")
    public Collection<RatingDTO> getUserRatings(@RequestHeader("Authorization") String authorizationHeader){
        return service.getUserRatings(authorizationHeader);
    }

    @DeleteMapping
    @SecurityRequirement(name = "bearerAuth")
    public void deleteRating(@RequestBody @Valid DeleteMovieRating deleteMovieRating, @RequestHeader("Authorization") String authorizationHeader){
        service.removeRating(deleteMovieRating, authorizationHeader);
    }

}
