package api.steps;

import api.client.MovieClient;
import api.config.ApiConstants;
import api.dto.MovieRequest;
import api.dto.MovieResponse;
import io.restassured.response.Response;
import static org.hamcrest.Matchers.*;

public class MovieApiSteps {
    private final MovieClient movieClient;

    public MovieApiSteps() {
        this.movieClient = new MovieClient();
    }

    public MovieClient getMovieClient() {
        return movieClient;
    }

    private MovieResponse extractMovieResponse(Response response, int expectedStatusCode) {
        return response.then()
            .statusCode(expectedStatusCode)
            .extract()
            .as(MovieResponse.class);
    }

    public MovieResponse createMovieSuccessfully(MovieRequest movieRequest, String token) {
        Response response = movieClient.createMovie(movieRequest, token);
        return extractMovieResponse(response, ApiConstants.HTTP_CREATED);
    }

    public MovieResponse getMovieSuccessfully(Long movieId, String token) {
        Response response = movieClient.getMovieById(movieId, token);
        return extractMovieResponse(response, ApiConstants.HTTP_OK);
    }

    public MovieResponse updateMovieSuccessfully(Long movieId, MovieRequest movieRequest, String token) {
        Response response = movieClient.updateMovie(movieId, movieRequest, token);
        if (response.getStatusCode() != ApiConstants.HTTP_OK) {
            System.out.println("Ошибка обновления фильма. Статус: " + response.getStatusCode());
            System.out.println("ID фильма: " + movieId);
            System.out.println("URL: " + response.getHeader("Location"));
            System.out.println("Тело ответа: " + response.getBody().asString());
        }
        
        return extractMovieResponse(response, ApiConstants.HTTP_OK);
    }

    public void deleteMovieSuccessfully(Long movieId, String token) {
        Response response = movieClient.deleteMovieById(movieId, token);
        response.then()
            .statusCode(anyOf(is(ApiConstants.HTTP_OK), is(ApiConstants.HTTP_NO_CONTENT)));
    }
}