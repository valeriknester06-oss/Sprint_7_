package ru.praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Before;
import org.junit.Test;
import ru.praktikum.client.CourierClient;
import ru.praktikum.data.CourierGenerator;
import ru.praktikum.model.Courier;

import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.equalTo;

public class CourierDeleteTest {

    private final CourierClient courierClient = new CourierClient();
    private Courier courier;
    private Integer courierId;

    @Before
    public void setUp() {
        courier = CourierGenerator.getRandomCourier();
        courierClient.createCourier(courier);
        courierId = courierClient.loginCourier(CourierGenerator.fromCourier(courier))
                .then()
                .extract()
                .path("id");
    }

    @Test
    @DisplayName("Успешное удаление курьера")
    @Description("Проверка, что существующего курьера можно удалить")
    public void deleteCourierSuccessTest() {
        courierClient.deleteCourier(courierId)
                .then()
                .statusCode(SC_OK)
                .body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Удаление курьера без id")
    @Description("Проверка, что при удалении курьера без id возвращается ошибка")
    public void deleteCourierWithoutIdTest() {
        courierClient.deleteCourierWithoutId()
                .then()
                .statusCode(SC_NOT_FOUND);
    }

    @Test
    @DisplayName("Удаление несуществующего курьера")
    @Description("Проверка, что при удалении несуществующего курьера возвращается ошибка")
    public void deleteCourierWithNonExistentIdTest() {
        courierClient.deleteCourier(999999)
                .then()
                .statusCode(SC_NOT_FOUND);
    }
}