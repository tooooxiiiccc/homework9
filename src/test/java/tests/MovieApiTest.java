package tests;

import api.dto.MovieRequest;
import api.dto.MovieResponse;
import api.steps.AuthApiSteps;
import api.steps.MovieApiSteps;
import db.domain.Movie;
import db.steps.MoviesDbSteps;
import io.qameta.allure.*;
import io.qameta.allure.Allure;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Tag;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;

@Epic("Movie API")
@Feature("CRUD операции с фильмами")
public class MovieApiTest {

    private static AuthApiSteps authApiSteps;
    private static MovieApiSteps movieApiSteps;
    private static MoviesDbSteps moviesDbSteps;
    private static String authToken;

    @BeforeAll
    public static void setUp() {
        authApiSteps = new AuthApiSteps();
        movieApiSteps = new MovieApiSteps();
        moviesDbSteps = new MoviesDbSteps();
        authToken = authApiSteps.loginAsAdmin();
    }

    @Test
    @Tag("smoke")
    @DisplayName("Позитивный тест: создание фильма через API и проверка в БД")
    @Description("Тест проверяет, что фильм успешно создается через API и сохраняется в базе данных")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Создание фильма")
    public void testCreateMovie_Positive() {
        MovieRequest movieRequest = Allure.step("Подготовить данные для создания фильма", () -> 
            MovieRequest.builder()
                .name("Тестовый фильм " + System.currentTimeMillis())
                .description("Описание тестового фильма")
                .price(1500)
                .genreId(1)
                .imageUrl("https://example.com/image.jpg")
                .location("MSK")
                .published(true)
                .rating(9.5)
                .build()
        );

        MovieResponse movieResponse = Allure.step("Создать фильм через API", () ->
            movieApiSteps.createMovieSuccessfully(movieRequest, authToken)
        );

        Allure.step("Проверить ответ API", () -> {
            assertThat(movieResponse.getId())
                .as("ID фильма должен быть не null")
                .isNotNull();
            assertThat(movieResponse.getName())
                .as("Имя фильма должно совпадать с отправленным")
                .isEqualTo(movieRequest.getName());
            assertThat(movieResponse.getPrice())
                .as("Цена должна совпадать")
                .isEqualTo(movieRequest.getPrice());
            assertThat(movieResponse.getDescription())
                .as("Описание должно совпадать")
                .isEqualTo(movieRequest.getDescription());
        });

        Long movieId = movieResponse.getId();
        Optional<Movie> dbMovie = Allure.step("Получить фильм из БД по ID: " + movieId, () ->
            moviesDbSteps.findMovieById(movieId)
        );

        Allure.step("Проверить данные в БД", () -> {
            assertThat(dbMovie)
                .as("Фильм должен существовать в базе данных")
                .isPresent();

            Movie movieFromDb = dbMovie.get();
            assertThat(movieFromDb.getName())
                .as("Имя в БД должно совпадать с отправленным")
                .isEqualTo(movieRequest.getName());
            assertThat(movieFromDb.getPrice())
                .as("Цена в БД должна совпадать")
                .isEqualTo(movieRequest.getPrice());
            assertThat(movieFromDb.getDescription())
                .as("Описание в БД должно совпадать")
                .isEqualTo(movieRequest.getDescription());
            assertThat(movieFromDb.getGenreId())
                .as("ID жанра в БД должен совпадать")
                .isEqualTo(movieRequest.getGenreId().longValue());
        });
    }

    @Test
    @Tag("regress")
    @DisplayName("Негативный тест: создание фильма без обязательных полей")
    @Description("Тест проверяет, что API возвращает ошибку при попытке создать фильм без обязательных полей")
    @Severity(SeverityLevel.NORMAL)
    @Story("Создание фильма")
    public void testCreateMovie_Negative_MissingFields() {
        MovieRequest invalidRequest = MovieRequest.builder()
            .name(null) // Имя отсутствует
            .price(1000)
            .build();

        Response response = movieApiSteps.getMovieClient().createMovie(invalidRequest, authToken);

        response.then()
            .statusCode(is(400));
    }

    /**
     * ========== ТЕСТ 2: GET /movies/:id - Получение фильма ==========
     *
     * Позитивный тест: получаем фильм по ID и проверяем данные в БД
     */
    @Test
    @Tag("smoke")
    @DisplayName("Позитивный тест: получение фильма по ID и проверка в БД")
    @Description("Тест проверяет, что фильм успешно получается через API и данные совпадают с БД")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Получение фильма")
    public void testGetMovieById_Positive() {
        MovieRequest movieRequest = MovieRequest.builder()
            .name("Фильм для получения " + System.currentTimeMillis())
            .description("Тестовое описание")
            .price(2000)
            .genreId(2)
            .imageUrl("https://example.com/test.jpg")
            .location("SPB")
            .published(true)
            .rating(8.0)
            .build();

        MovieResponse createdMovie = movieApiSteps.createMovieSuccessfully(movieRequest, authToken);
        Long movieId = createdMovie.getId();

        MovieResponse movieFromApi = movieApiSteps.getMovieSuccessfully(movieId, authToken);
        assertThat(movieFromApi.getId()).isEqualTo(movieId);
        assertThat(movieFromApi.getName()).isEqualTo(movieRequest.getName());
        Optional<Movie> movieFromDb = moviesDbSteps.findMovieById(movieId);
        assertThat(movieFromDb).isPresent();
        Movie dbMovie = movieFromDb.get();

        assertThat(movieFromApi.getName())
            .as("Имя из API должно совпадать с именем в БД")
            .isEqualTo(dbMovie.getName());
        assertThat(movieFromApi.getPrice())
            .as("Цена из API должна совпадать с ценой в БД")
            .isEqualTo(dbMovie.getPrice());
        assertThat(movieFromApi.getDescription())
            .as("Описание из API должно совпадать с описанием в БД")
            .isEqualTo(dbMovie.getDescription());
    }

    @Test
    @Tag("regress")
    @DisplayName("Негативный тест: получение несуществующего фильма")
    @Description("Тест проверяет, что API возвращает ошибку при попытке получить несуществующий фильм")
    @Severity(SeverityLevel.NORMAL)
    @Story("Получение фильма")
    public void testGetMovieById_Negative_NotFound() {
        Long nonExistentId = 999999L;
        Optional<Movie> dbMovie = moviesDbSteps.findMovieById(nonExistentId);
        assertThat(dbMovie)
            .as("Фильм не должен существовать в БД")
            .isEmpty();
        Response response = movieApiSteps.getMovieClient().getMovieById(nonExistentId, authToken);
        response.then()
            .statusCode(is(404));
    }

    @Test
    @Tag("smoke")
    @DisplayName("Позитивный тест: обновление фильма и проверка изменений в БД")
    @Description("Тест проверяет, что фильм успешно обновляется через API и изменения сохраняются в БД")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Обновление фильма")
    public void testUpdateMovie_Positive() {
        MovieRequest originalRequest = Allure.step("Подготовить исходные данные фильма", () ->
            MovieRequest.builder()
                .name("Оригинальный фильм " + System.currentTimeMillis())
                .description("Оригинальное описание")
                .price(1000)
                .genreId(1)
                .imageUrl("https://example.com/original.jpg")
                .location("MSK")
                .published(false)
                .rating(7.0)
                .build()
        );

        MovieResponse createdMovie = Allure.step("Создать фильм для обновления", () ->
            movieApiSteps.createMovieSuccessfully(originalRequest, authToken)
        );
        Long movieId = createdMovie.getId();
        Allure.step("Проверить, что фильм создан с ID: " + movieId, () -> {
            assertThat(movieId)
                .as("ID созданного фильма не должен быть null")
                .isNotNull();
            Optional<Movie> createdDbMovie = moviesDbSteps.findMovieById(movieId);
            assertThat(createdDbMovie)
                .as("Созданный фильм должен существовать в БД перед обновлением")
                .isPresent();
            try {
                MovieResponse movieFromApi = movieApiSteps.getMovieSuccessfully(movieId, authToken);
                assertThat(movieFromApi.getId())
                    .as("Фильм должен быть доступен через GET API перед обновлением")
                    .isEqualTo(movieId);
            } catch (Exception e) {
                throw new AssertionError("Фильм с ID " + movieId + " не доступен через GET API: " + e.getMessage());
            }
        });
        MovieRequest updateRequest = Allure.step("Подготовить данные для обновления", () ->
            MovieRequest.builder()
                .name("Обновленный фильм " + System.currentTimeMillis())
                .description("Обновленное описание")
                .price(2500)
                .genreId(2)
                .imageUrl("https://example.com/updated.jpg")
                .location("SPB")
                .published(true)
                .rating(9.5)
                .build()
        );
        MovieResponse updatedMovie = Allure.step("Обновить фильм через API с ID: " + movieId, () ->
            movieApiSteps.updateMovieSuccessfully(movieId, updateRequest, authToken)
        );
        Allure.step("Проверить ответ API после обновления", () -> {
            assertThat(updatedMovie.getId()).isEqualTo(movieId);
            assertThat(updatedMovie.getName()).isEqualTo(updateRequest.getName());
            assertThat(updatedMovie.getPrice()).isEqualTo(updateRequest.getPrice());
            assertThat(updatedMovie.getDescription()).isEqualTo(updateRequest.getDescription());
        });
        Optional<Movie> dbMovie = Allure.step("Получить обновленный фильм из БД", () ->
            moviesDbSteps.findMovieById(movieId)
        );
        Allure.step("Проверить изменения в БД", () -> {
            assertThat(dbMovie).isPresent();
            Movie movieFromDb = dbMovie.get();
            assertThat(movieFromDb.getName())
                .as("Имя в БД должно быть обновлено")
                .isEqualTo(updateRequest.getName());
            assertThat(movieFromDb.getPrice())
                .as("Цена в БД должна быть обновлена")
                .isEqualTo(updateRequest.getPrice());
            assertThat(movieFromDb.getDescription())
                .as("Описание в БД должно быть обновлено")
                .isEqualTo(updateRequest.getDescription());
            assertThat(movieFromDb.getGenreId())
                .as("Жанр в БД должен быть обновлен")
                .isEqualTo(Long.valueOf(updateRequest.getGenreId()));
        });
    }

    @Test
    @Tag("regress")
    @DisplayName("Негативный тест: обновление несуществующего фильма")
    @Description("Тест проверяет, что API возвращает ошибку при попытке обновить несуществующий фильм")
    @Severity(SeverityLevel.NORMAL)
    @Story("Обновление фильма")
    public void testUpdateMovie_Negative_NotFound() {
        MovieRequest updateRequest = MovieRequest.builder()
            .name("Обновленный фильм")
            .description("Описание")
            .price(1000)
            .genreId(1)
            .build();
        Long nonExistentId = 999999L;
        Response response = movieApiSteps.getMovieClient().updateMovie(nonExistentId, updateRequest, authToken);
        response.then()
            .statusCode(is(404));
    }

    @Test
    @Tag("smoke")
    @DisplayName("Позитивный тест: удаление фильма и проверка удаления в БД")
    @Description("Тест проверяет, что фильм успешно удаляется через API и удаляется из БД")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Удаление фильма")
    public void testDeleteMovie_Positive() {
        MovieRequest movieRequest = MovieRequest.builder()
            .name("Фильм для удаления " + System.currentTimeMillis())
            .description("Этот фильм будет удален")
            .price(1000)
            .genreId(1)
            .imageUrl("https://example.com/delete.jpg")
            .location("MSK")
            .published(true)
            .rating(5.0)
            .build();

        MovieResponse createdMovie = movieApiSteps.createMovieSuccessfully(movieRequest, authToken);
        Long movieId = createdMovie.getId();
        Optional<Movie> movieBeforeDelete = moviesDbSteps.findMovieById(movieId);
        assertThat(movieBeforeDelete)
            .as("Фильм должен существовать в БД перед удалением")
            .isPresent();
        movieApiSteps.deleteMovieSuccessfully(movieId, authToken);
        Optional<Movie> movieAfterDelete = moviesDbSteps.findMovieById(movieId);
        assertThat(movieAfterDelete)
            .as("Фильм должен быть удален из БД")
            .isEmpty();
    }

    @Test
    @Tag("regress")
    @DisplayName("Негативный тест: удаление несуществующего фильма")
    @Description("Тест проверяет, что API возвращает ошибку при попытке удалить несуществующий фильм")
    @Severity(SeverityLevel.NORMAL)
    @Story("Удаление фильма")
    public void testDeleteMovie_Negative_NotFound() {
        Long nonExistentId = 999999L;
        Optional<Movie> dbMovie = moviesDbSteps.findMovieById(nonExistentId);
        assertThat(dbMovie).isEmpty();
        Response response = movieApiSteps.getMovieClient().deleteMovieById(nonExistentId, authToken);
        response.then()
            .statusCode(is(404));
    }
}

