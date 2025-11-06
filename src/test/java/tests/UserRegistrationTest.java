package tests;

import api.client.UserClient;
import api.dto.RegistrationRequest;
import api.dto.RegistrationResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class UserRegistrationTest {

@Test
public void userRegistrationTest() {
    String uniqueEmail = "test" + System.currentTimeMillis() + "@mail.ru";

    RegistrationRequest request = RegistrationRequest.builder()
        .email(uniqueEmail)
        .password("Dada1234")
        .passwordRepeat("Dada1234")
        .fullName("Test User")
        .build();

    UserClient userClient = new UserClient();

    Response response = userClient.registerUser(request);

    response.then().statusCode(201);

    RegistrationResponse registrationResponse = response.as(RegistrationResponse.class);
    assertThat(registrationResponse.getId()).isNotNull();
    assertThat(registrationResponse.getEmail()).isEqualTo(uniqueEmail);
    assertThat(registrationResponse.getFullName()).isEqualTo("Test User");
    assertThat(registrationResponse.getVerified()).isFalse();

//    var dbUser = UserDbSteps.findUserByEmail(uniqueEmail);
//    assertThat(dbUser).isPresent();
//    assertThat(dbUser.get().getEmail()).isEqualTo(uniqueEmail);
    }
}
