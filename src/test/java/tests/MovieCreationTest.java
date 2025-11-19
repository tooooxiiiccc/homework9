package tests;

import api.client.AuthClient;
import api.client.MovieClient;
import api.dto.AuthRequest;
import api.dto.MovieRequest;
import api.dto.MovieResponse;
import util.AdminCredentials;
import util.AdminCredentialsLoader;
import org.junit.jupiter.api.Test;
import io.restassured.response.Response;

import static org.assertj.core.api.Assertions.assertThat;

public class MovieCreationTest {
    @Test
    public void movieCreateTest(){
        AdminCredentials adminCreds = AdminCredentialsLoader.getAdminCredentials();

        AuthRequest authRequest = AuthRequest.builder()
            .email(adminCreds.getEmail())
            .password(adminCreds.getPassword())
            .build();

        MovieRequest movieRequest = MovieRequest.builder()
            .name("Movie Test")
            .description("WOWOWOW")
            .price(1000)
            .genreId(2)
            .imageUrl("data:image/jpeg;base64,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")
            .location("MSK")
            .published(true)
            .rating(8.5)
            .build();

        AuthClient authClient = new AuthClient();
        MovieClient movieClient = new MovieClient();

        String authToken = authClient.getAuthToken(authRequest);

        Response response = movieClient.createMovie(movieRequest, authToken);

        MovieResponse movieResponse = response.as(MovieResponse.class);
        assertThat(movieResponse.getId()).isNotNull();
        assertThat(movieResponse.getName()).isEqualTo(movieRequest.getName());
    }
}
