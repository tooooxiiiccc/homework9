package api.client;

import api.dto.AuthRequest;
import api.dto.AuthResponse;
import api.specs.CinescopeSpecs;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class AuthClient {
    public Response auth(AuthRequest authRequest) {
        return given()
            .spec(CinescopeSpecs.getAuthspec())
            .body(authRequest)
            .when()
            .post("/login");
    }

    public String getAuthToken(AuthRequest authRequest) {
        Response response = auth(authRequest);
        AuthResponse authResponse = response.then()
            .statusCode(200)
            .extract()
            .as(AuthResponse.class);
        return authResponse.getAccessToken();
    }
}
