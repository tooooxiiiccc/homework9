package db.steps;

import db.DbBaseSteps;
import db.dao.MoviesDao;
import db.domain.Movie;

import java.util.Optional;

public class MoviesDbSteps extends DbBaseSteps {

    public Optional<Movie> findMovieById(Long id) {
        return jdbi.withExtension(MoviesDao.class, dao -> dao.getMovieById(id));
    }
}