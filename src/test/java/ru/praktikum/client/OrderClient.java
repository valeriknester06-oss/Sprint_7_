package ru.praktikum.client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.praktikum.model.Order;

public class OrderClient extends BaseClient {

    private static final String ORDERS_PATH = "/api/v1/orders";
    private static final String ACCEPT_ORDER_PATH = "/api/v1/orders/accept";

    @Step("Создать заказ")
    public Response createOrder(Order order) {
        return getBaseSpec()
                .body(order)
                .when()
                .post(ORDERS_PATH);
    }

    @Step("Получить список заказов")
    public Response getOrdersList() {
        return getBaseSpec()
                .when()
                .get(ORDERS_PATH);
    }

    @Step("Принять заказ с id заказа {orderId} курьером {courierId}")
    public Response acceptOrder(int orderId, Integer courierId) {
        return getBaseSpec()
                .queryParam("courierId", courierId)
                .when()
                .put(ACCEPT_ORDER_PATH + "/" + orderId);
    }

    @Step("Принять заказ без courierId")
    public Response acceptOrderWithoutCourierId(int orderId) {
        return getBaseSpec()
                .when()
                .put(ACCEPT_ORDER_PATH + "/" + orderId);
    }

    @Step("Принять заказ без id заказа")
    public Response acceptOrderWithoutTrack(int courierId) {
        return getBaseSpec()
                .queryParam("courierId", courierId)
                .when()
                .put(ACCEPT_ORDER_PATH);
    }

    @Step("Получить заказ по track {track}")
    public Response getOrderByTrack(int track) {
        return getBaseSpec()
                .queryParam("t", track)
                .when()
                .get(ORDERS_PATH + "/track");
    }

    @Step("Получить заказ без track")
    public Response getOrderWithoutTrack() {
        return getBaseSpec()
                .when()
                .get(ORDERS_PATH + "/track");
    }
}