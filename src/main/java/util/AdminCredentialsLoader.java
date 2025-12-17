package util;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.InputStream;

public class AdminCredentialsLoader {
    private static final String CREDENTIALS_FILE = "admin_credentials.json";
    private static final ObjectMapper mapper = new ObjectMapper();

    public static AdminCredentials getAdminCredentials() {
        try (InputStream is = AdminCredentialsLoader.class.getClassLoader()
            .getResourceAsStream(CREDENTIALS_FILE)) {

            if (is == null) {
                throw new RuntimeException("Admin credentials file not found: " + CREDENTIALS_FILE);
            }

            java.util.Map<String, Object> credentialsMap = mapper.readValue(is, java.util.Map.class);
            java.util.Map<String, Object> adminConfig = (java.util.Map<String, Object>) credentialsMap.get("admin");

            AdminCredentials credentials = new AdminCredentials();
            credentials.setEmail((String) adminConfig.get("email"));
            credentials.setPassword((String) adminConfig.get("password"));

            return credentials;

        } catch (Exception e) {
            throw new RuntimeException("Failed to load admin credentials from: " + CREDENTIALS_FILE, e);
        }
    }
}