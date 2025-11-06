package api.dto;

import lombok.Data;

@Data
public class UserInfoObject {
    private String id;
    private String email;
    private String[] roles;
    private Boolean verified;
    private Boolean banned;
}
