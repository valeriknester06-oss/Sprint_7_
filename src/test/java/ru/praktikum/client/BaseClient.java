package ru.praktikum.client;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import ru.praktikum.config.TestConfig;

public class BaseClient {

    protected RequestSpecification getBaseSpec() {
        return RestAssured
                .given()
                .baseUri(TestConfig.BASE_URL)
                .contentType(ContentType.JSON);
    }
}