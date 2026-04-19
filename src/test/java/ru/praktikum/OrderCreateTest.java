package ru.praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.praktikum.client.OrderClient;
import ru.praktikum.data.OrderGenerator;
import ru.praktikum.model.Order;

import java.util.List;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class OrderCreateTest {

    private final OrderClient orderClient = new OrderClient();
    private final List<String> color;

    public OrderCreateTest(List<String> color) {
        this.color = color;
    }

    @Parameterized.Parameters(name = "Цвет самоката: {0}")
    public static Object[][] getColorData() {
        return new Object[][]{
                {List.of("BLACK")},
                {List.of("GREY")},
                {List.of("BLACK", "GREY")},
                {null}
        };
    }

    @Test
    @DisplayName("Создание заказа")
    @Description("Проверка, что заказ создаётся с разными вариантами цвета самоката")
    public void createOrderTest() {
        Order order = OrderGenerator.getOrderWithColor(color);

        orderClient.createOrder(order)
                .then()
                .statusCode(SC_CREATED)
                .body("track", notNullValue());
    }
}