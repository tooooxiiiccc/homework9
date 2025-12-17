package util;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.InputStream;

public class DbUtils {
    private static ObjectMapper mapper = new ObjectMapper();
    private static final String CREDENTIALS_FILE = "db_creds_cinescope.json";

    public static DbCredentials getCredentials(DbName dbName) {
        try (InputStream is = DbUtils.class.getClassLoader().getResourceAsStream(CREDENTIALS_FILE)) {
            java.util.Map<String, Object> credentialsMap = mapper.readValue(is, java.util.Map.class);
            java.util.Map<String, Object> dbConfig = (java.util.Map<String, Object>) credentialsMap.get(dbName.name().toLowerCase());

            DbCredentials dbCredentials = new DbCredentials();
            dbCredentials.setHost((String) dbConfig.get("host"));
            dbCredentials.setPort((Integer) dbConfig.get("port"));
            dbCredentials.setUsername((String) dbConfig.get("username"));
            dbCredentials.setPassword((String) dbConfig.get("password"));
            dbCredentials.setDatabase((String) dbConfig.get("database"));
            return dbCredentials;
        }
        catch (Exception e) {
            throw new RuntimeException("Ошибка подключения к БД" + CREDENTIALS_FILE, e);
        }
    }
}