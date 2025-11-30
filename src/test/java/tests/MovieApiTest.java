package tests;

import api.dto.MovieRequest;
import api.dto.MovieResponse;
import api.steps.AuthApiSteps;
import api.steps.MovieApiSteps;
import db.domain.Movie;
import db.steps.MoviesDbSteps;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Комплексные тесты для API работы с фильмами.
 * 
 * Этот класс демонстрирует правильный подход к написанию автотестов:
 * 1. Использование шагов (Steps) для инкапсуляции логики
 * 2. Проверка данных как через API, так и через БД
 * 3. Позитивные и негативные сценарии
 * 4. Использование Allure для отчетности
 */
@Epic("Movie API")
@Feature("CRUD операции с фильмами")
public class MovieApiTest {

    // Инициализируем шаги один раз для всех тестов
    private static AuthApiSteps authApiSteps;
    private static MovieApiSteps movieApiSteps;
    private static MoviesDbSteps moviesDbSteps;
    private static String authToken;

    /**
     * @BeforeAll - выполняется один раз перед всеми тестами в классе.
     * Здесь мы:
     * 1. Инициализируем все необходимые шаги
     * 2. Получаем токен авторизации для использования во всех тестах
     */
    @BeforeAll
    public static void setUp() {
        // Инициализация шагов
        authApiSteps = new AuthApiSteps();
        movieApiSteps = new MovieApiSteps();
        moviesDbSteps = new MoviesDbSteps();

        // Получаем токен авторизации один раз для всех тестов
        // Это эффективнее, чем логиниться в каждом тесте
        authToken = authApiSteps.loginAsAdmin();
    }

    /**
     * ========== ТЕСТ 1: POST /movies - Создание фильма ==========
     * 
     * Позитивный тест: проверяем успешное создание фильма
     * и сохранение данных в БД
     */
    @Test
    @DisplayName("Позитивный тест: создание фильма через API и проверка в БД")
    @Description("Тест проверяет, что фильм успешно создается через API и сохраняется в базе данных")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Создание фильма")
    public void testCreateMovie_Positive() {
        // ШАГ 1: Подготовка тестовых данных
        // Создаем объект запроса с данными фильма
        MovieRequest movieRequest = MovieRequest.builder()
            .name("Тестовый фильм " + System.currentTimeMillis()) // Уникальное имя
            .description("Описание тестового фильма")
            .price(1500)
            .genreId(1)
            .imageUrl("https://example.com/image.jpg")
            .location("MSK")
            .published(true)
            .rating(9.5)
            .build();

        // ШАГ 2: Выполнение действия через API
        // Используем шаги для создания фильма
        MovieResponse movieResponse = movieApiSteps.createMovieSuccessfully(movieRequest, authToken);

        // ШАГ 3: Проверка ответа API
        // Проверяем, что API вернул корректные данные
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

        // ШАГ 4: Проверка в базе данных
        // Это ключевой момент - проверяем, что данные действительно сохранились в БД
        Long movieId = movieResponse.getId();
        Optional<Movie> dbMovie = moviesDbSteps.findMovieById(movieId);

        // Проверяем, что фильм найден в БД
        assertThat(dbMovie)
            .as("Фильм должен существовать в базе данных")
            .isPresent();

        // Проверяем, что данные в БД совпадают с отправленными
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
    }

    /**
     * Негативный тест: создание фильма без обязательных полей
     */
    @Test
    @DisplayName("Негативный тест: создание фильма без обязательных полей")
    @Description("Тест проверяет, что API возвращает ошибку при попытке создать фильм без обязательных полей")
    @Severity(SeverityLevel.NORMAL)
    @Story("Создание фильма")
    public void testCreateMovie_Negative_MissingFields() {
        // ШАГ 1: Создаем невалидный запрос (без обязательных полей)
        MovieRequest invalidRequest = MovieRequest.builder()
            .name(null) // Имя отсутствует
            .price(1000)
            .build();

        // ШАГ 2: Отправляем запрос и ожидаем ошибку
        // Для негативных тестов используем клиент напрямую, чтобы получить Response
        Response response = movieApiSteps.getMovieClient().createMovie(invalidRequest, authToken);

        // ШАГ 3: Проверяем, что API вернул ошибку (400 Bad Request)
        response.then()
            .statusCode(is(400)); // Ожидаем ошибку валидации
    }

    /**
     * ========== ТЕСТ 2: GET /movies/:id - Получение фильма ==========
     * 
     * Позитивный тест: получаем фильм по ID и проверяем данные в БД
     */
    @Test
    @DisplayName("Позитивный тест: получение фильма по ID и проверка в БД")
    @Description("Тест проверяет, что фильм успешно получается через API и данные совпадают с БД")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Получение фильма")
    public void testGetMovieById_Positive() {
        // ШАГ 1: Сначала создаем фильм для тестирования
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

        // ШАГ 2: Получаем фильм через API
        MovieResponse movieFromApi = movieApiSteps.getMovieSuccessfully(movieId, authToken);

        // ШАГ 3: Проверяем ответ API
        assertThat(movieFromApi.getId()).isEqualTo(movieId);
        assertThat(movieFromApi.getName()).isEqualTo(movieRequest.getName());

        // ШАГ 4: Получаем фильм из БД
        Optional<Movie> movieFromDb = moviesDbSteps.findMovieById(movieId);

        // ШАГ 5: Сравниваем данные из API и БД
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

    /**
     * Негативный тест: получение несуществующего фильма
     */
    @Test
    @DisplayName("Негативный тест: получение несуществующего фильма")
    @Description("Тест проверяет, что API возвращает ошибку при попытке получить несуществующий фильм")
    @Severity(SeverityLevel.NORMAL)
    @Story("Получение фильма")
    public void testGetMovieById_Negative_NotFound() {
        // ШАГ 1: Используем несуществующий ID
        Long nonExistentId = 999999L;

        // ШАГ 2: Проверяем, что фильма нет в БД
        Optional<Movie> dbMovie = moviesDbSteps.findMovieById(nonExistentId);
        assertThat(dbMovie)
            .as("Фильм не должен существовать в БД")
            .isEmpty();

        // ШАГ 3: Пытаемся получить фильм через API
        Response response = movieApiSteps.getMovieClient().getMovieById(nonExistentId, authToken);

        // ШАГ 4: Проверяем, что API вернул ошибку 404
        response.then()
            .statusCode(is(404)); // Not Found
    }

    /**
     * ========== ТЕСТ 3: PUT /movies/:id - Обновление фильма ==========
     * 
     * Позитивный тест: обновляем фильм и проверяем изменения в БД
     */
    @Test
    @DisplayName("Позитивный тест: обновление фильма и проверка изменений в БД")
    @Description("Тест проверяет, что фильм успешно обновляется через API и изменения сохраняются в БД")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Обновление фильма")
    public void testUpdateMovie_Positive() {
        // ШАГ 1: Создаем фильм для обновления
        MovieRequest originalRequest = MovieRequest.builder()
            .name("Оригинальный фильм " + System.currentTimeMillis())
            .description("Оригинальное описание")
            .price(1000)
            .genreId(1)
            .imageUrl("https://example.com/original.jpg")
            .location("MSK")
            .published(false)
            .rating(7.0)
            .build();

        MovieResponse createdMovie = movieApiSteps.createMovieSuccessfully(originalRequest, authToken);
        Long movieId = createdMovie.getId();

        // ШАГ 2: Подготавливаем данные для обновления
        MovieRequest updateRequest = MovieRequest.builder()
            .name("Обновленный фильм " + System.currentTimeMillis())
            .description("Обновленное описание")
            .price(2500) // Изменили цену
            .genreId(2)  // Изменили жанр
            .imageUrl("https://example.com/updated.jpg")
            .location("SPB") // Изменили локацию
            .published(true) // Изменили статус публикации
            .rating(9.5) // Изменили рейтинг
            .build();

        // ШАГ 3: Обновляем фильм через API
        MovieResponse updatedMovie = movieApiSteps.updateMovieSuccessfully(movieId, updateRequest, authToken);

        // ШАГ 4: Проверяем ответ API
        assertThat(updatedMovie.getId()).isEqualTo(movieId);
        assertThat(updatedMovie.getName()).isEqualTo(updateRequest.getName());
        assertThat(updatedMovie.getPrice()).isEqualTo(updateRequest.getPrice());
        assertThat(updatedMovie.getDescription()).isEqualTo(updateRequest.getDescription());

        // ШАГ 5: Проверяем изменения в БД
        Optional<Movie> dbMovie = moviesDbSteps.findMovieById(movieId);
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
            .isEqualTo(updateRequest.getGenreId().longValue());
    }

    /**
     * Негативный тест: обновление несуществующего фильма
     */
    @Test
    @DisplayName("Негативный тест: обновление несуществующего фильма")
    @Description("Тест проверяет, что API возвращает ошибку при попытке обновить несуществующий фильм")
    @Severity(SeverityLevel.NORMAL)
    @Story("Обновление фильма")
    public void testUpdateMovie_Negative_NotFound() {
        // ШАГ 1: Подготавливаем данные для обновления
        MovieRequest updateRequest = MovieRequest.builder()
            .name("Обновленный фильм")
            .description("Описание")
            .price(1000)
            .genreId(1)
            .build();

        // ШАГ 2: Пытаемся обновить несуществующий фильм
        Long nonExistentId = 999999L;
        Response response = movieApiSteps.getMovieClient().updateMovie(nonExistentId, updateRequest, authToken);

        // ШАГ 3: Проверяем, что API вернул ошибку 404
        response.then()
            .statusCode(is(404));
    }

    /**
     * ========== ТЕСТ 4: DELETE /movies/:id - Удаление фильма ==========
     * 
     * Позитивный тест: удаляем фильм и проверяем, что он удален из БД
     */
    @Test
    @DisplayName("Позитивный тест: удаление фильма и проверка удаления в БД")
    @Description("Тест проверяет, что фильм успешно удаляется через API и удаляется из БД")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Удаление фильма")
    public void testDeleteMovie_Positive() {
        // ШАГ 1: Создаем фильм для удаления
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

        // ШАГ 2: Проверяем, что фильм существует в БД перед удалением
        Optional<Movie> movieBeforeDelete = moviesDbSteps.findMovieById(movieId);
        assertThat(movieBeforeDelete)
            .as("Фильм должен существовать в БД перед удалением")
            .isPresent();

        // ШАГ 3: Удаляем фильм через API
        movieApiSteps.deleteMovieSuccessfully(movieId, authToken);

        // ШАГ 4: Проверяем, что фильм удален из БД
        // Это ключевая проверка - данные должны быть удалены из базы
        Optional<Movie> movieAfterDelete = moviesDbSteps.findMovieById(movieId);
        assertThat(movieAfterDelete)
            .as("Фильм должен быть удален из БД")
            .isEmpty();

        // Дополнительная проверка через existsById
        boolean exists = moviesDbSteps.existsMovie(movieId);
        assertThat(exists)
            .as("Фильм не должен существовать в БД")
            .isFalse();
    }

    /**
     * Негативный тест: удаление несуществующего фильма
     */
    @Test
    @DisplayName("Негативный тест: удаление несуществующего фильма")
    @Description("Тест проверяет, что API возвращает ошибку при попытке удалить несуществующий фильм")
    @Severity(SeverityLevel.NORMAL)
    @Story("Удаление фильма")
    public void testDeleteMovie_Negative_NotFound() {
        // ШАГ 1: Используем несуществующий ID
        Long nonExistentId = 999999L;

        // ШАГ 2: Проверяем, что фильма нет в БД
        Optional<Movie> dbMovie = moviesDbSteps.findMovieById(nonExistentId);
        assertThat(dbMovie).isEmpty();

        // ШАГ 3: Пытаемся удалить несуществующий фильм
        Response response = movieApiSteps.getMovieClient().deleteMovieById(nonExistentId, authToken);

        // ШАГ 4: Проверяем, что API вернул ошибку 404
        response.then()
            .statusCode(is(404));
    }
}

