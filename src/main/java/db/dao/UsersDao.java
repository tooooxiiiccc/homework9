package db.dao;

import db.domain.User;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import java.util.Optional;

public interface UsersDao {
    @SqlQuery("SELECT * FROM users WHERE id = :id")
    Optional<User> findById(@Bind("id") String id);

    @SqlQuery("SELECT * FROM users WHERE email = :email")
    Optional<User> findByEmail(@Bind("email") String email);

    @SqlQuery("SELECT COUNT(*) FROM users WHERE id = :id")
    boolean existsById(@Bind("id") String id);

    @SqlUpdate("DELETE FROM users WHERE id = :id")
    void deleteById(@Bind("id") Long id);
}
