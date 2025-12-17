package api.steps;

import api.client.ReviewClient;
import api.config.ApiConstants;
import api.dto.ReviewRequest;
import api.dto.ReviewResponse;
import io.restassured.response.Response;
import java.util.List;
import static org.hamcrest.Matchers.*;

public class ReviewApiSteps {
    private final ReviewClient reviewClient;

    public ReviewApiSteps() {
        this.reviewClient = new ReviewClient();
    }

    public ReviewClient getReviewClient() {
        return reviewClient;
    }

    private ReviewResponse extractReviewResponse(Response response, int expectedStatusCode) {
        return response.then()
            .statusCode(expectedStatusCode)
            .extract()
            .as(ReviewResponse.class);
    }

    public ReviewResponse createReviewSuccessfully(Long movieId, ReviewRequest reviewRequest, String token) {
        Response response = reviewClient.createReview(movieId, reviewRequest, token);
        return extractReviewResponse(response, ApiConstants.HTTP_CREATED);
    }

    public List<ReviewResponse> getReviewsByMovieId(Long movieId, String token) {
        Response response = reviewClient.getReviewsByMovieId(movieId, token);
        return response.then()
            .statusCode(ApiConstants.HTTP_OK)
            .extract()
            .jsonPath()
            .getList("", ReviewResponse.class);
    }

    public void deleteReviewSuccessfully(Long movieId, Long reviewId, String token) {
        Response response = reviewClient.deleteReviewById(movieId, reviewId, token);
        response.then()
            .statusCode(anyOf(is(ApiConstants.HTTP_OK), is(ApiConstants.HTTP_NO_CONTENT)));
    }

    /**
     * Находит отзыв по тексту и удаляет его
     * @param movieId ID фильма
     * @param reviewText Текст отзыва для поиска
     * @param token Токен авторизации
     * @return true если отзыв был найден и удален, false если не найден
     */
    public boolean deleteReviewByText(Long movieId, String reviewText, String token) {
        List<ReviewResponse> reviews = getReviewsByMovieId(movieId, token);
        for (ReviewResponse review : reviews) {
            if (review.getText() != null && review.getText().contains(reviewText)) {
                deleteReviewSuccessfully(movieId, review.getId(), token);
                return true;
            }
        }
        return false;
    }
}



