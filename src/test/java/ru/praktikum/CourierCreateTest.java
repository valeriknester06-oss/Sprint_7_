package ru.praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.praktikum.client.CourierClient;
import ru.praktikum.data.CourierGenerator;
import ru.praktikum.model.Courier;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_CONFLICT;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.hamcrest.CoreMatchers.equalTo;

public class CourierCreateTest {

    private final CourierClient courierClient = new CourierClient();
    private Courier courier;
    private Integer courierId;

    @Before
    public void setUp() {
        courier = CourierGenerator.getRandomCourier();
    }

    @After
    public void tearDown() {
        try {
            courierId = courierClient.loginCourier(CourierGenerator.fromCourier(courier))
                    .then()
                    .extract()
                    .path("id");
        } catch (Exception ignored) {
        }

        if (courierId != null) {
            courierClient.deleteCourier(courierId);
        }
    }

    @Test
    @DisplayName("Успешное создание курьера")
    @Description("Проверка, что можно создать нового курьера с корректными данными")
    public void createCourierSuccessTest() {
        courierClient.createCourier(courier)
                .then()
                .statusCode(SC_CREATED)
                .body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Нельзя создать двух одинаковых курьеров")
    @Description("Проверка, что повторное создание курьера с теми же данными возвращает ошибку 409")
    public void createDuplicateCourierTest() {
        courierClient.createCourier(courier);

        courierClient.createCourier(courier)
                .then()
                .statusCode(SC_CONFLICT)
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    @DisplayName("Нельзя создать курьера без логина")
    @Description("Проверка, что создание курьера без логина возвращает ошибку 400")
    public void createCourierWithoutLoginTest() {
        Courier invalidCourier = new Courier(null, courier.getPassword(), courier.getFirstName());

        courierClient.createCourier(invalidCourier)
                .then()
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Нельзя создать курьера без пароля")
    @Description("Проверка, что создание курьера без пароля возвращает ошибку 400")
    public void createCourierWithoutPasswordTest() {
        Courier invalidCourier = new Courier(courier.getLogin(), null, courier.getFirstName());

        courierClient.createCourier(invalidCourier)
                .then()
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }
}