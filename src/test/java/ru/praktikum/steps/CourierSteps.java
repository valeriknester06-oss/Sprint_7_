package ru.praktikum.steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.praktikum.client.CourierClient;
import ru.praktikum.model.Courier;
import ru.praktikum.model.CourierLogin;

public class CourierSteps {

    private final CourierClient courierClient = new CourierClient();

    @Step("Создать и получить response")
    public Response create(Courier courier) {
        return courierClient.createCourier(courier);
    }

    @Step("Логин и получить response")
    public Response login(CourierLogin courierLogin) {
        return courierClient.loginCourier(courierLogin);
    }

    @Step("Получить id курьера")
    public int getCourierId(CourierLogin courierLogin) {
        return login(courierLogin)
                .then()
                .extract()
                .path("id");
    }

    @Step("Удалить курьера, если он создан")
    public void deleteByIdIfExists(Integer courierId) {
        if (courierId != null && courierId > 0) {
            courierClient.deleteCourier(courierId);
        }
    }
}