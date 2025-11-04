package db.steps;

import db.DbBaseSteps;
import db.dao.UsersDao;
import db.domain.User;
import java.util.Optional;

public class UserDbSteps extends DbBaseSteps {
    public Optional<User> findUserById(Long id) {
        return jdbi.withExtension(UsersDao.class, dao -> dao.findById(id));
    }

    public Optional<User> findUserByEmail(String email) {
        return jdbi.withExtension(UsersDao.class, dao -> dao.findByEmail(email));
    }

    public Boolean userExists(Long id) {
        return jdbi.withExtension(UsersDao.class, dao -> dao.existsById(id));
    }

    public void deleteUser(Long id) {
        jdbi.useExtension(UsersDao.class, dao -> dao.deleteById(id));
    }

}
