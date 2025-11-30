package api.steps;

import api.client.MovieClient;
import api.dto.MovieRequest;
import api.dto.MovieResponse;
import io.restassured.response.Response;
import static org.hamcrest.Matchers.*;

public class MovieApiSteps {
    private MovieClient movieClient = new MovieClient();

    public MovieClient getMovieClient() {
        return movieClient;
    }

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

    /**
     * Обновляет фильм по ID и возвращает обновленный объект.
     * 
     * @param movieId ID фильма для обновления
     * @param movieRequest объект с новыми данными фильма
     * @param token токен авторизации
     * @return обновленный объект MovieResponse
     */
    public MovieResponse updateMovieSuccessfully(Long movieId, MovieRequest movieRequest, String token) {
        Response response = movieClient.updateMovie(movieId, movieRequest, token);

        return response.then()
            .statusCode(200)
            .extract()
            .as(MovieResponse.class);
    }

    /**
     * Удаляет фильм по ID.
     * Проверяет, что запрос вернул статус 204 (No Content) или 200 (OK).
     * 
     * @param movieId ID фильма для удаления
     * @param token токен авторизации
     */
    public void deleteMovieSuccessfully(Long movieId, String token) {
        Response response = movieClient.deleteMovieById(movieId, token);

        response.then()
            .statusCode(anyOf(is(200), is(204)));
    }
}
