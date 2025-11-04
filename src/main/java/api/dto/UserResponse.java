package api.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserResponse {
    private String id;
    private String email;
    private String fullName;
    private String[] roles;
    private Boolean verified;
    private LocalDateTime createdAt;
    private Boolean banned;
}
