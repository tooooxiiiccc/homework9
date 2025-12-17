package api.client;

import api.config.ApiConstants;
import api.dto.AuthRequest;
import api.dto.AuthResponse;
import api.specs.CinescopeSpecs;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class AuthClient {
    public Response auth(AuthRequest authRequest) {
        return given()
            .spec(CinescopeSpecs.getAuthSpec())
            .body(authRequest)
            .when()
            .post(ApiConstants.LOGIN_ENDPOINT);
    }

    public String getAuthToken(AuthRequest authRequest) {
        Response response = auth(authRequest);
        AuthResponse authResponse = response.then()
            .statusCode(ApiConstants.HTTP_OK)
            .extract()
            .as(AuthResponse.class);
        return authResponse.getAccessToken();
    }
}