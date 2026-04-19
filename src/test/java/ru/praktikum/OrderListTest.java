package ru.praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;
import ru.praktikum.client.OrderClient;

import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.notNullValue;

public class OrderListTest {

    private final OrderClient orderClient = new OrderClient();

    @Test
    @DisplayName("Получение списка заказов")
    @Description("Проверка, что в ответе на запрос списка заказов возвращается список orders")
    public void getOrdersListTest() {
        orderClient.getOrdersList()
                .then()
                .statusCode(SC_OK)
                .body("orders", notNullValue());
    }
}