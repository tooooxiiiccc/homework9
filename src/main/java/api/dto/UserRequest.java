package api.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserRequest {
    private String email;
    private String fullName;
    private String password;
    private String passwordRepeat;
}
