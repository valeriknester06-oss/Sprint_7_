package ru.praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Before;
import org.junit.Test;
import ru.praktikum.client.OrderClient;
import ru.praktikum.data.OrderGenerator;
import ru.praktikum.model.Order;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.notNullValue;

public class GetOrderByTrackTest {

    private final OrderClient orderClient = new OrderClient();
    private Integer track;

    @Before
    public void setUp() {
        Order order = OrderGenerator.getOrderWithColor(java.util.Collections.singletonList("GREY"));
        track = orderClient.createOrder(order)
                .then()
                .extract()
                .path("track");
    }

    @Test
    @DisplayName("Успешное получение заказа по треку")
    @Description("Проверка, что по существующему track возвращается объект заказа")
    public void getOrderByTrackSuccessTest() {
        orderClient.getOrderByTrack(track)
                .then()
                .statusCode(SC_OK)
                .body("order", notNullValue());
    }

    @Test
    @DisplayName("Получение заказа без трека")
    @Description("Проверка, что запрос без track возвращает ошибку 400")
    public void getOrderWithoutTrackTest() {
        orderClient.getOrderWithoutTrack()
                .then()
                .statusCode(SC_BAD_REQUEST);
    }

    @Test
    @DisplayName("Получение заказа по неверному треку")
    @Description("Проверка, что запрос с несуществующим track возвращает ошибку 404")
    public void getOrderByWrongTrackTest() {
        orderClient.getOrderByTrack(999999)
                .then()
                .statusCode(SC_NOT_FOUND);
    }
}