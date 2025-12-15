package api.config;

public final class ApiConstants {
    
    private ApiConstants() {
    }

    public static final String AUTH_BASE_URL = "https://auth.cinescope.t-qa.ru";
    public static final String API_BASE_URL = "https://api.cinescope.t-qa.ru";

    public static final String LOGIN_ENDPOINT = "/login";
    public static final String MOVIES_ENDPOINT = "/movies";
    public static final String MOVIE_BY_ID_ENDPOINT = MOVIES_ENDPOINT + "/{id}";
    public static final String REVIEWS_ENDPOINT = "/reviews";
    public static final String REVIEW_BY_ID_ENDPOINT = REVIEWS_ENDPOINT + "/{id}";

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";

    public static final int HTTP_OK = 200;
    public static final int HTTP_CREATED = 201;
    public static final int HTTP_NO_CONTENT = 204;
    public static final int HTTP_BAD_REQUEST = 400;
    public static final int HTTP_UNAUTHORIZED = 401;
    public static final int HTTP_FORBIDDEN = 403;
    public static final int HTTP_NOT_FOUND = 404;
}





