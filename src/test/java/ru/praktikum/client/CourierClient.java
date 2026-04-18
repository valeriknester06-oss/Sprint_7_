package ru.praktikum.client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.praktikum.model.Courier;
import ru.praktikum.model.CourierLogin;

public class CourierClient extends BaseClient {

    private static final String COURIER_PATH = "/api/v1/courier";
    private static final String LOGIN_PATH = "/api/v1/courier/login";

    @Step("Создать курьера")
    public Response createCourier(Courier courier) {
        return getBaseSpec()
                .body(courier)
                .when()
                .post(COURIER_PATH);
    }

    @Step("Авторизовать курьера")
    public Response loginCourier(CourierLogin courierLogin) {
        return getBaseSpec()
                .body(courierLogin)
                .when()
                .post(LOGIN_PATH);
    }

    @Step("Удалить курьера по id")
    public Response deleteCourier(int courierId) {
        return getBaseSpec()
                .when()
                .delete(COURIER_PATH + "/" + courierId);
    }

    @Step("Удалить курьера без id")
    public Response deleteCourierWithoutId() {
        return getBaseSpec()
                .when()
                .delete(COURIER_PATH);
    }
}