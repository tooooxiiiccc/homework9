package api.steps;

import api.client.MovieClient;
import api.dto.MovieRequest;
import api.dto.MovieResponse;
import io.restassured.response.Response;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class MovieApiSteps {
    private MovieClient movieClient = new MovieClient();

    public MovieResponse createMovieSuccessfully(MovieRequest movieRequest, String token) {
        Response response = movieClient.createMovie(movieRequest, token);

        return response.then()
            .statusCode(201)
            .extract()
            .as(MovieResponse.class);
    }

    public MovieResponse getMovieSuccessfully(Long movieId, String token) {
        Response response = movieClient.getMovieById(movieId, token);

        return response.then()
            .statusCode(200)
            .extract()
            .as(MovieResponse.class);
    }
}
