package ru.praktikum;

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

import static org.hamcrest.CoreMatchers.equalTo;

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
    public void acceptOrderSuccessTest() {
        orderClient.acceptOrder(orderId, courierId)
                .then()
                .statusCode(200)
                .body("ok", equalTo(true));
    }

    @Test
    public void acceptOrderWithoutCourierIdTest() {
        orderClient.acceptOrderWithoutCourierId(orderId)
                .then()
                .statusCode(400);
    }

    @Test
    public void acceptOrderWithWrongCourierIdTest() {
        orderClient.acceptOrder(orderId, 999999)
                .then()
                .statusCode(404);
    }

    @Test
    public void acceptOrderWithoutTrackTest() {
        orderClient.acceptOrderWithoutTrack(courierId)
                .then()
                .statusCode(404);
    }

    @Test
    public void acceptOrderWithWrongTrackTest() {
        orderClient.acceptOrder(999999, courierId)
                .then()
                .statusCode(404);
    }
}