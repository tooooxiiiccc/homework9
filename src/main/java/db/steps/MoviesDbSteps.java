package db.steps;

import db.DbBaseSteps;
import db.dao.MoviesDao;
import db.domain.Movie;

import java.util.Optional;

public class MoviesDbSteps extends DbBaseSteps {
    public Optional<Movie> findMovieById(Long id) {
        return jdbi.withExtension(MoviesDao.class, dao -> dao.getMovieById(id));
    }

    public boolean existsMovie(Long id) {
        return jdbi.withExtension(MoviesDao.class, dao -> dao.existsById(id));
    }

    public void deleteMovie(Long id) {
        jdbi.useExtension(MoviesDao.class, dao -> dao.deleteMovieById(id));
    }

    public Optional<Movie> findMovieById(String name) {
        return jdbi.withExtension(MoviesDao.class, dao -> dao.getMovieByName(name));
    }
}
