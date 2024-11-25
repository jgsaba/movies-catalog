package test.raven.moviescatalog.integration;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import test.raven.moviescatalog.entities.Movie;
import test.raven.moviescatalog.integration.utils.PaginatedResponse;
import test.raven.moviescatalog.integration.utils.TestUtils;
import test.raven.moviescatalog.model.CreateMovieDTO;
import test.raven.moviescatalog.model.DeleteMovieDTO;
import test.raven.moviescatalog.model.MovieDTO;
import test.raven.moviescatalog.model.UpdateMovieDTO;
import test.raven.moviescatalog.repositories.MovieRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Import(TestConfig.class)
public class MoviesIntegrationTests extends BaseIntegrationTests{

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private TestUtils utils;

    @Test
    public void testDeleteMovie() {
        DeleteMovieDTO deleteMovieDTO = new DeleteMovieDTO();
        deleteMovieDTO.setMovieId(1L);

        HttpEntity<DeleteMovieDTO> request = new HttpEntity<>(deleteMovieDTO, utils.getValidAdminRoleSecurityHeader());

        ResponseEntity<MovieDTO> response = restTemplate.exchange(
                "/movies",
                HttpMethod.DELETE,
                request,
                MovieDTO.class);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(movieRepository.findById(1L).isEmpty());
    }

    @Test
    public void testCreateMovie() throws IOException {

        CreateMovieDTO createMovieDTO = getCreateMovieDTO();

        HttpHeaders jsonHeaders = new HttpHeaders();
        jsonHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CreateMovieDTO> jsonPart = new HttpEntity<>(createMovieDTO, jsonHeaders);

        ClassPathResource resource = new ClassPathResource("troy.jpg");

        byte[] fileBytes = Files.readAllBytes(Path.of(resource.getURI()));
        ByteArrayResource fileResource = new ByteArrayResource(fileBytes) {
            @Override
            public String getFilename() {
                return resource.getFilename();
            }
        };

        HttpHeaders fileHeaders = new HttpHeaders();
        fileHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        HttpEntity<ByteArrayResource> filePart = new HttpEntity<>(fileResource, fileHeaders);

        MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("createMovieDTO", jsonPart);
        requestBody.add("poster", filePart);

        HttpHeaders validAdminRoleSecurityHeader = utils.getValidAdminRoleSecurityHeader();
        validAdminRoleSecurityHeader.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(requestBody, validAdminRoleSecurityHeader);

        ResponseEntity<MovieDTO> response = restTemplate.exchange(
                "/movies",
                HttpMethod.POST,
                requestEntity,
                MovieDTO.class);

        MovieDTO body = response.getBody();
        assertEquals(201, response.getStatusCodeValue());
        assertEquals(createMovieDTO.getTitle(), body.getTitle());
        assertEquals(createMovieDTO.getSynopsis(), body.getSynopsis());
        assertEquals(createMovieDTO.getReleaseYear(), body.getReleaseYear());
        assertEquals(createMovieDTO.getCategories(), body.getCategories());
        assertFalse(body.getPoster().isEmpty());
    }

    @Test
    @Transactional
    public void testUpdateMovie() {

        String newTitle = " Matrix Reloaded";
        String newSynopsis = "In this second adventure, Neo and the rebel leaders estimate that they have 72 hours until Zion falls under siege to the Machine Army.";
        Set<String> categories = Set.of("Action");
        int releaseYear = 2003;

        UpdateMovieDTO updateMovieDTO = new UpdateMovieDTO(2L, newTitle, releaseYear, newSynopsis, categories);

        HttpEntity<UpdateMovieDTO> request = new HttpEntity<>(updateMovieDTO, utils.getValidAdminRoleSecurityHeader());

        ResponseEntity<MovieDTO> response = restTemplate.exchange(
                "/movies",
                HttpMethod.PUT,
                request,
                MovieDTO.class);

        MovieDTO body = response.getBody();
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(updateMovieDTO.getTitle(), body.getTitle());
        assertEquals(updateMovieDTO.getSynopsis(), body.getSynopsis());
        assertEquals(updateMovieDTO.getCategories(), body.getCategories());
        assertEquals(releaseYear, body.getReleaseYear());

        Optional<Movie> byId = movieRepository.findById(2L);

        assertTrue(byId.isPresent());
        Movie movie = byId.get();

        movie.getCategories().stream().forEach(category -> {
            assertTrue(updateMovieDTO.getCategories().contains(category.getCategory()));
        });
        assertEquals(updateMovieDTO.getTitle(), movie.getName());
        assertEquals(updateMovieDTO.getSynopsis(), movie.getSynopsis());

        assertEquals(movie.getCategories().size(), updateMovieDTO.getCategories().size());

        assertEquals(releaseYear, movie.getReleaseYear());
    }

    private static CreateMovieDTO getCreateMovieDTO() {

        return new CreateMovieDTO("Troy",
                "An adaptation of Homer's great epic, the film follows the assault on Troy by the united Greek forces",
                2004,
                Set.of("Epic", "Action", "Drama"));
    }

    @Test
    public void testGetMovies(){

        HttpEntity<MovieDTO> request = new HttpEntity<>(null, utils.getValidUserRoleSecurityHeader());

        ResponseEntity<List<MovieDTO>> response = restTemplate.exchange(
                "/movies",
                HttpMethod.GET,
                request, new ParameterizedTypeReference<>() {});

        System.out.println(response.getBody());
    }

    @Test
    public void testQueryMovies(){

        String content = "about";
        String category = "Sci-Fi";
        Integer releaseYear = 2010;
        String orderBy = "name";
        String orderDirection = "asc";

        String url = String.format(
                "/movies/query?content=%s&category=%s&releaseYear=%d&orderBy=%s&orderDirection=%s",
                content, category, releaseYear, orderBy, orderDirection
        );

        HttpEntity<MovieDTO> request = new HttpEntity<>(utils.getValidUserRoleSecurityHeader());

        ResponseEntity<PaginatedResponse<MovieDTO>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                request,
                new ParameterizedTypeReference<>() {}
        );

        PaginatedResponse<MovieDTO> body = response.getBody();

        assertEquals(10, body.getSize());
        assertEquals(0, body.getNumber());
        assertEquals(1, body.getContent().size());
        assertEquals(1, body.getTotalElements());
        assertEquals(1, body.getTotalPages());
        assertTrue(body.isLast());
    }
    
}
