package tests;

import org.junit.jupiter.api.Test;
import util.DbCredentials;
import util.DbName;
import util.DbUtils;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class connectionTest {
    @Test
    public void testConnection() {
        DbCredentials dbCredenitals = DbUtils.getCredentials(DbName.DB_MOVIES);

        assertThat(dbCredenitals.getHost()).isEqualTo("147.45.143.178");
        assertThat(dbCredenitals.getDatabase()).isEqualTo("db_movies");
        assertThat(dbCredenitals.getJdbcUrl()).isNotNull();
    }
}
