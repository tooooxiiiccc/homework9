package api.client;

import api.dto.MovieRequest;
import api.specs.CinescopeSpecs;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class MovieClient {
   public Response createMovie(MovieRequest movieRequest, String token) {
       return given()
           .spec(CinescopeSpecs.getApiSpec())
           .header("Authorization", "Bearer " + token)
           .body(movieRequest)
           .when()
           .post("/movies");
    }

    public Response getMovieById(Long movieId, String token) {
       return given()
           .spec(CinescopeSpecs.getApiSpec())
           .header("Authorization", "Bearer " + token)
           .when()
           .get("/movies/" + movieId);
    }

    public Response updateMovie(Long movieId, MovieRequest movieRequest, String token) {
       return given()
           .spec(CinescopeSpecs.getApiSpec())
           .header("Authorization", "Bearer " + token)
           .body(movieRequest)
           .when()
           .put("/movies/" + movieId);
    }

    public Response deleteMovieById(Long movieId, String token) {
       return given()
           .spec(CinescopeSpecs.getApiSpec())
           .header("Authorization", "Bearer " + token)
           .when()
           .delete("/movies/" + movieId);
    }
}
