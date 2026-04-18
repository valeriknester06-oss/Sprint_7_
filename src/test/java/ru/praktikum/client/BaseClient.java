package ru.praktikum.client;

import ru.praktikum.config.TestConfig;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class BaseClient {

    protected RequestSpecification getBaseSpec() {
        return RestAssured
                .given()
                .baseUri(TestConfig.BASE_URL)
                .contentType(ContentType.JSON);
    }
}