package util;
import lombok.Data;

@Data
public class DbCredentials {
    private String host;
    private int port;
    private String username, password, database;
    public String getJdbcUrl() {
        return String.format("jdbc:postgresql://%s:%d/%s", host, port, database);
    }
}

