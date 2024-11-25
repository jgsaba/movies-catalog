package test.raven.moviescatalog.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import test.raven.moviescatalog.model.CreateMovieDTO;
import test.raven.moviescatalog.model.DeleteMovieDTO;
import test.raven.moviescatalog.model.MovieDTO;
import test.raven.moviescatalog.model.UpdateMovieDTO;
import test.raven.moviescatalog.services.MoviesService;
import test.raven.moviescatalog.utils.OrderBy;
import test.raven.moviescatalog.utils.OrderDirection;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("movies")
@RequiredArgsConstructor
public class MovieController {

    private final MoviesService moviesService;


    @PostMapping
    @SecurityRequirement(name = "bearerAuth")
    @ResponseStatus(HttpStatus.CREATED)
    public MovieDTO createMovie(@RequestPart @Valid CreateMovieDTO createMovieDTO, @RequestPart(required = false) MultipartFile poster, @RequestHeader("Authorization") String authorizationHeader) throws IOException {

        return moviesService.createMovieWithFile(createMovieDTO, authorizationHeader, poster);
    }

    @PutMapping
    @SecurityRequirement(name = "bearerAuth")
    public MovieDTO updateMovie(@RequestBody UpdateMovieDTO movieDTO){
        return moviesService.updateMovie(movieDTO);
    }

    @DeleteMapping
    @SecurityRequirement(name = "bearerAuth")
    public void deleteMovie(@RequestBody DeleteMovieDTO deleteMovieDTO){
        moviesService.deleteMovie(deleteMovieDTO);
    }

    @GetMapping
    @SecurityRequirement(name = "bearerAuth")
    @Parameter(name = "Authorization", in = ParameterIn.HEADER, required = true)
    public List<MovieDTO> getAllMovies(){
        return moviesService.getMovies();
    }


    @GetMapping("/query")
    @SecurityRequirement(name = "bearerAuth")
    public Page<MovieDTO> queryMovies(
            @RequestParam(required = false) String content,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Integer releaseYear,
            @RequestParam(required = false, defaultValue = "name") OrderBy orderBy,
            @RequestParam(required = false, defaultValue = "asc") OrderDirection orderDirection,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(required = false, defaultValue = "0") Integer pageNumber){

        return moviesService.queryMovie(content, category, releaseYear, pageSize, pageNumber, orderBy.toString(), orderDirection.name());

    }

}
