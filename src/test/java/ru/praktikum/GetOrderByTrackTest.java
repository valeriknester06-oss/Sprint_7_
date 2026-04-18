package ru.praktikum;

import org.junit.Before;
import org.junit.Test;
import ru.praktikum.client.OrderClient;
import ru.praktikum.data.OrderGenerator;
import ru.praktikum.model.Order;

import java.util.Collections;

import static org.hamcrest.Matchers.notNullValue;

public class GetOrderByTrackTest {

    private final OrderClient orderClient = new OrderClient();
    private Integer track;

    @Before
    public void setUp() {
        Order order = OrderGenerator.getOrderWithColor(Collections.singletonList("GREY"));
        track = orderClient.createOrder(order)
                .then()
                .extract()
                .path("track");
    }

    @Test
    public void getOrderByTrackSuccessTest() {
        orderClient.getOrderByTrack(track)
                .then()
                .statusCode(200)
                .body("order", notNullValue());
    }

    @Test
    public void getOrderWithoutTrackTest() {
        orderClient.getOrderWithoutTrack()
                .then()
                .statusCode(400);
    }

    @Test
    public void getOrderByWrongTrackTest() {
        orderClient.getOrderByTrack(999999)
                .then()
                .statusCode(404);
    }
}