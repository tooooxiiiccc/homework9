package api.specs;

import api.config.ApiConstants;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public final class CinescopeSpecs {

    private CinescopeSpecs() {}

    public static RequestSpecification getAuthSpec() {
        return new RequestSpecBuilder()
            .setBaseUri(ApiConstants.AUTH_BASE_URL)
            .setContentType(ContentType.JSON)
            .build();
    }

    public static RequestSpecification getApiSpec() {
        return new RequestSpecBuilder()
            .setBaseUri(ApiConstants.API_BASE_URL)
            .setContentType(ContentType.JSON)
            .build();
    }
}