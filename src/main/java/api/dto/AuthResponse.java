package api.dto;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthResponse {
    private UserInfoObject user;
    private String accessToken;
    private Long expiresIn;
}
