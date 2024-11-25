package test.raven.moviescatalog.services;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import test.raven.moviescatalog.utils.ImageHandler;
import test.raven.moviescatalog.entities.Category;
import test.raven.moviescatalog.entities.Movie;
import test.raven.moviescatalog.entities.MovieProjection;
import test.raven.moviescatalog.entities.RegularUser;
import test.raven.moviescatalog.exceptions.MovieNotFoundException;
import test.raven.moviescatalog.model.CreateMovieDTO;
import test.raven.moviescatalog.model.DeleteMovieDTO;
import test.raven.moviescatalog.model.MovieDTO;
import test.raven.moviescatalog.model.UpdateMovieDTO;
import test.raven.moviescatalog.repositories.CategoryRepository;
import test.raven.moviescatalog.repositories.MovieRepository;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MoviesService {

    private final UsersService usersService;

    private final ImageHandler imageHandler;

    private final MovieRepository movieRepository;

    private final CategoryRepository categoryRepository;

    public MovieDTO createMovie(CreateMovieDTO movie, String authorizationHeader) {

        RegularUser user = usersService.getUserFromAuthorization(authorizationHeader);
        Movie newMovie = createNewMovie(movie, user);

        return MovieDTO.fromEntity(movieRepository.save(newMovie));
    }

    public MovieDTO createMovieWithFile(CreateMovieDTO movie, String authorizationHeader, MultipartFile file) throws IOException {

        String posterName = (file == null || file.isEmpty()) ? "" : imageHandler.handleImage(file);

        RegularUser user = usersService.getUserFromAuthorization(authorizationHeader);
        Movie newMovie = createNewMovie(movie, user);
        newMovie.setPoster(posterName);

        return MovieDTO.fromEntity(movieRepository.save(newMovie));
    }



    public void deleteMovie(DeleteMovieDTO deleteMovieDTO){
        try {

            movieRepository.deleteById(deleteMovieDTO.getMovieId());

        } catch (EmptyResultDataAccessException exception){
            throw new MovieNotFoundException();
        }
    }

    public MovieDTO updateMovie(UpdateMovieDTO movieDTO){
        Movie movie = movieRepository.findById(movieDTO.getId())
                .orElseThrow(MovieNotFoundException::new);

        movie.setName(movieDTO.getTitle());
        movie.setReleaseYear(movieDTO.getReleaseYear());
        movie.setSynopsis(movieDTO.getSynopsis());
        movie.setCategories(movieDTO.getCategories()
                .stream()
                .map(s -> mapCategory(Category.normalizeCategory(s)))
                .collect(Collectors.toSet()));

        return MovieDTO.fromEntity(movieRepository.save(movie));
    }

    public Movie findMovie(Long movieId){
        return movieRepository.findById(movieId)
                .orElseThrow(MovieNotFoundException::new);
    }

    private Movie createNewMovie(CreateMovieDTO movieDTO, RegularUser user) {

        Movie newMovie = new Movie();
        newMovie.setName(movieDTO.getTitle());
        newMovie.setReleaseYear(movieDTO.getReleaseYear());
        newMovie.setSynopsis(movieDTO.getSynopsis());

        newMovie.setCategories(movieDTO.getCategories()
                .stream()
                .map(s -> mapCategory(Category.normalizeCategory(s)))
                .collect(Collectors.toSet()));

        newMovie.setCreatedBy(user);

        return movieRepository.save(newMovie);
    }

    private Category mapCategory(String category){
        return categoryRepository.findById(category)
                .orElseGet(() -> categoryRepository.save(new Category(category)));
    }

    public List<MovieDTO> getMovies(){
        return movieRepository.findAll().stream().map(MovieDTO::fromEntity).collect(Collectors.toList());
    }

    @Cacheable("movies-query")
    public Page<MovieDTO> queryMovie(String content, String category, Integer releaseYear, Integer pageSize, Integer pageNumber, String orderByField, String orderDirection){

        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.valueOf(orderDirection), orderByField));
        Page<MovieProjection> movies = movieRepository.searchMovies(content, category, releaseYear, pageRequest);

        return movies.map(MovieDTO::fromProjection);
    }

    public boolean checkIfMovieExists(Long movieId) {
        return movieRepository.existsById(movieId);
    }
}
