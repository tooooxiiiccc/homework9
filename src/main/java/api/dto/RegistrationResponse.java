package api.dto;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RegistrationResponse {
    private String id;
    private String email;
    private String fullName;
    private String[] roles;
    private Boolean verified;
    private String createdAt;
    private Boolean banned;
}
