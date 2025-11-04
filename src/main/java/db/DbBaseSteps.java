package db;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.jackson2.Jackson2Plugin;
import org.jdbi.v3.postgres.PostgresPlugin;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import util.DbCredentials;
import util.DbName;
import util.DbUtils;

public abstract class DbBaseSteps {
    protected final Jdbi jdbi;

    public DbBaseSteps() {
        DbCredentials dbCredentials = DbUtils.getCredentials(DbName.DB_MOVIES);
        this.jdbi = Jdbi.create(dbCredentials.getJdbcUrl(), dbCredentials.getUsername(), dbCredentials.getPassword());

        jdbi.installPlugin(new SqlObjectPlugin());
        jdbi.installPlugin(new PostgresPlugin());
        jdbi.installPlugin(new Jackson2Plugin());
    }

}
