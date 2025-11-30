package api.steps;

import api.client.AuthClient;
import api.dto.AuthRequest;
import util.AdminCredentials;
import util.AdminCredentialsLoader;

public class AuthApiSteps {
    private AuthClient authClient = new AuthClient();
    public String loginAsAdmin() {
        AdminCredentials adminCreds = AdminCredentialsLoader.getAdminCredentials();
        AuthRequest authRequest = AuthRequest.builder()
            .email(adminCreds.getEmail())
            .password(adminCreds.getPassword())
            .build();
        String token = authClient.getAuthToken(authRequest);
        return token;
    }
}


