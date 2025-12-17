package api.steps;

import api.client.AuthClient;
import api.dto.AuthRequest;
import util.AdminCredentials;
import util.AdminCredentialsLoader;

public class AuthApiSteps {
    
    private final AuthClient authClient;

    public AuthApiSteps() {
        this.authClient = new AuthClient();
    }

    public String loginAsAdmin() {
        AdminCredentials adminCreds = AdminCredentialsLoader.getAdminCredentials();
        AuthRequest authRequest = buildAuthRequest(adminCreds);
        return authClient.getAuthToken(authRequest);
    }

    private AuthRequest buildAuthRequest(AdminCredentials credentials) {
        return AuthRequest.builder()
            .email(credentials.getEmail())
            .password(credentials.getPassword())
            .build();
    }
}