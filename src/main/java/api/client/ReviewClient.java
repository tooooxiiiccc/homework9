package api.client;

import api.config.ApiConstants;
import api.dto.ReviewRequest;
import api.specs.CinescopeSpecs;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import static io.restassured.RestAssured.given;

public class ReviewClient {
    private RequestSpecification createAuthenticatedRequest(String token) {
        return given()
            .spec(CinescopeSpecs.getApiSpec())
            .header(ApiConstants.AUTHORIZATION_HEADER, ApiConstants.BEARER_PREFIX + token);
    }

    public Response createReview(Long movieId, ReviewRequest reviewRequest, String token) {
        return createAuthenticatedRequest(token)
            .body(reviewRequest)
            .when()
            .post(ApiConstants.MOVIES_ENDPOINT + "/" + movieId + ApiConstants.REVIEWS_ENDPOINT);
    }

    public Response getReviewsByMovieId(Long movieId, String token) {
        return createAuthenticatedRequest(token)
            .when()
            .get(ApiConstants.MOVIES_ENDPOINT + "/" + movieId + ApiConstants.REVIEWS_ENDPOINT);
    }

    public Response deleteReviewById(Long movieId, Long reviewId, String token) {
        return createAuthenticatedRequest(token)
            .when()
            .delete(ApiConstants.MOVIES_ENDPOINT + "/" + movieId + ApiConstants.REVIEWS_ENDPOINT + "/" + reviewId);
    }
}



