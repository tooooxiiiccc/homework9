package db.dao;

import db.domain.Movie;
import db.domain.User;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import java.util.Optional;

public interface MoviesDao {
    @SqlQuery("SELECT * FROM movies WHERE id = :id")
    Optional<Movie> getMovieById(@Bind("id") Long id);

    @SqlQuery("SELECT COUNT(*) FROM movies WHERE id = :id")
    boolean existsById(@Bind("id") Long id);

    @SqlUpdate("DELETE FROM movies WHERE id = :id")
    void deleteMovieById(@Bind("id") Long id);

    @SqlQuery("SELECT * FROM movies WHERE name = :name")
    Optional<Movie> getMovieByName(@Bind("name") String name);
}
