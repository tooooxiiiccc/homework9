package api.specs;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class CinescopeSpecs {
    public static RequestSpecification getAuthspec() {
        return new RequestSpecBuilder()
            .setBaseUri("https://auth.cinescope.t-qa.ru")
            .setContentType(ContentType.JSON)
            .build();
    }
        public static RequestSpecification getApiSpec() {
            return new RequestSpecBuilder()
                .setBaseUri("https://api.cinescope.t-qa.ru")
                .setContentType(ContentType.JSON)
                .build();
        }
    }
