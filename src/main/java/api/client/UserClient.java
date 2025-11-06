package api.client;

import api.dto.RegistrationRequest;
import api.specs.CinescopeSpecs;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class UserClient {
    public Response registerUser(RegistrationRequest registrationRequest) {
        return given()
            .spec(CinescopeSpecs.getAuthspec())
            .body(registrationRequest)
            .when()
            .post("/register");
    }
}
