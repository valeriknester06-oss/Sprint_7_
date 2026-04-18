package ru.praktikum.steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.praktikum.client.OrderClient;
import ru.praktikum.model.Order;

public class OrderSteps {

    private final OrderClient orderClient = new OrderClient();

    @Step("Создать заказ и получить response")
    public Response create(Order order) {
        return orderClient.createOrder(order);
    }

    @Step("Получить track созданного заказа")
    public int getTrack(Order order) {
        return create(order)
                .then()
                .extract()
                .path("track");
    }
}