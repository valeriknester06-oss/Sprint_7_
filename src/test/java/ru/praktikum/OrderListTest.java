package ru.praktikum;

import ru.praktikum.client.OrderClient;
import org.junit.Test;

import static org.hamcrest.Matchers.notNullValue;

public class OrderListTest {

    private final OrderClient orderClient = new OrderClient();

    @Test
    public void getOrdersListTest() {
        orderClient.getOrdersList()
                .then()
                .statusCode(200)
                .body("orders", notNullValue());
    }
}