package db.dao;

import db.domain.User;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import java.util.Optional;

/**
 * DAO интерфейс для работы с таблицей users.
 * 
 * Mapper для User регистрируется глобально в DbBaseSteps,
 * поэтому здесь аннотации не нужны.
 */
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
