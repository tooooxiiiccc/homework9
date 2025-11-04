package db.domain;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class User {
    private String id;
    private String email;
    private String fullName;
    private String password;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean verified;
    private Boolean banned;
    private String[] roles;
}
