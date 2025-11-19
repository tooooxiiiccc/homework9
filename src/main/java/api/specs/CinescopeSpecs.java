package api.specs;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

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

        public static ResponseSpecification createdResponse() {
            return new ResponseSpecBuilder()
                .expectStatusCode(201)
                .build();
        }

        public static ResponseSpecification successResponse() {
            return new ResponseSpecBuilder()
                .expectStatusCode(200)
                .build();
        }

        public static ResponseSpecification forbiddenResponse() {
            return new ResponseSpecBuilder()
                .expectStatusCode(403)
                .build();
        }
    }
