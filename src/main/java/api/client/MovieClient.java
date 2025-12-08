package api.client;

import api.config.ApiConstants;
import api.dto.MovieRequest;
import api.specs.CinescopeSpecs;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import static io.restassured.RestAssured.given;

public class MovieClient {
    private RequestSpecification createAuthenticatedRequest(String token) {
        return given()
            .spec(CinescopeSpecs.getApiSpec())
            .header(ApiConstants.AUTHORIZATION_HEADER, ApiConstants.BEARER_PREFIX + token);
    }

    public Response createMovie(MovieRequest movieRequest, String token) {
        return createAuthenticatedRequest(token)
            .body(movieRequest)
            .when()
            .post(ApiConstants.MOVIES_ENDPOINT);
    }

    public Response getMovieById(Long movieId, String token) {
        return createAuthenticatedRequest(token)
            .when()
            .get(ApiConstants.MOVIES_ENDPOINT + "/" + movieId);
    }

    public Response updateMovie(Long movieId, MovieRequest movieRequest, String token) {
        return createAuthenticatedRequest(token)
            .body(movieRequest)
            .when()
            .patch(ApiConstants.MOVIES_ENDPOINT + "/" + movieId);
    }

    public Response deleteMovieById(Long movieId, String token) {
        return createAuthenticatedRequest(token)
            .when()
            .delete(ApiConstants.MOVIES_ENDPOINT + "/" + movieId);
    }
}