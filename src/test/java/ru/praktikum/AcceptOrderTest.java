package ru.praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.praktikum.client.CourierClient;
import ru.praktikum.client.OrderClient;
import ru.praktikum.data.CourierGenerator;
import ru.praktikum.data.OrderGenerator;
import ru.praktikum.model.Courier;
import ru.praktikum.model.Order;

import java.util.Collections;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_CONFLICT;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.equalTo;

public class AcceptOrderTest {

    private final CourierClient courierClient = new CourierClient();
    private final OrderClient orderClient = new OrderClient();

    private Courier courier;
    private Integer courierId;
    private Integer orderId;

    @Before
    public void setUp() {
        courier = CourierGenerator.getRandomCourier();
        courierClient.createCourier(courier);

        courierId = courierClient.loginCourier(CourierGenerator.fromCourier(courier))
                .then()
                .extract()
                .path("id");

        Order order = OrderGenerator.getOrderWithColor(Collections.singletonList("BLACK"));

        int track = orderClient.createOrder(order)
                .then()
                .extract()
                .path("track");

        orderId = orderClient.getOrderByTrack(track)
                .then()
                .extract()
                .path("order.id");
    }

    @After
    public void tearDown() {
        if (courierId != null) {
            courierClient.deleteCourier(courierId);
        }
    }

    @Test
    @DisplayName("Успешное принятие заказа")
    @Description("Проверка, что курьер может принять существующий заказ")
    public void acceptOrderSuccessTest() {
        orderClient.acceptOrder(orderId, courierId)
                .then()
                .statusCode(SC_OK)
                .body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Принятие заказа без courierId")
    @Description("Проверка, что при отсутствии courierId возвращается ошибка 400")
    public void acceptOrderWithoutCourierIdTest() {
        orderClient.acceptOrder(orderId, null)
                .then()
                .statusCode(SC_BAD_REQUEST);
    }

    @Test
    @DisplayName("Принятие заказа с неверным courierId")
    @Description("Проверка, что при неверном courierId возвращается ошибка 404")
    public void acceptOrderWithWrongCourierIdTest() {
        orderClient.acceptOrder(orderId, 999999)
                .then()
                .statusCode(SC_NOT_FOUND);
    }

    @Test
    @DisplayName("Принятие заказа без id заказа")
    @Description("Проверка, что при отсутствии id заказа возвращается ошибка")
    public void acceptOrderWithoutTrackTest() {
        orderClient.acceptOrderWithoutTrack(courierId)
                .then()
                .statusCode(SC_NOT_FOUND);
    }

    @Test
    @DisplayName("Принятие заказа с неверным id заказа")
    @Description("Проверка, что при несуществующем id заказа возвращается ошибка 404")
    public void acceptOrderWithWrongTrackTest() {
        orderClient.acceptOrder(999999, courierId)
                .then()
                .statusCode(SC_NOT_FOUND);
    }

    @Test
    @DisplayName("Повторное принятие заказа")
    @Description("Проверка, что уже принятый заказ нельзя принять повторно")
    public void acceptOrderSecondTimeTest() {
        orderClient.acceptOrder(orderId, courierId);

        orderClient.acceptOrder(orderId, courierId)
                .then()
                .statusCode(SC_CONFLICT);
    }
}