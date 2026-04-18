package ru.praktikum;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.praktikum.client.CourierClient;
import ru.praktikum.data.CourierGenerator;
import ru.praktikum.model.Courier;
import ru.praktikum.model.CourierLogin;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class CourierLoginTest {

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

    @After
    public void tearDown() {
        if (courierId != null) {
            courierClient.deleteCourier(courierId);
        }
    }

    @Test
    public void courierLoginSuccessTest() {
        CourierLogin loginData = CourierGenerator.fromCourier(courier);

        courierClient.loginCourier(loginData)
                .then()
                .statusCode(200)
                .body("id", notNullValue());
    }

    @Test
    public void courierLoginWithoutLoginTest() {
        CourierLogin loginData = new CourierLogin(null, courier.getPassword());

        courierClient.loginCourier(loginData)
                .then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    public void courierLoginWithoutPasswordTest() {
        CourierLogin loginData = new CourierLogin(courier.getLogin(), null);

        courierClient.loginCourier(loginData)
                .then()
                .statusCode(504);
    }

    @Test
    public void courierLoginWithWrongLoginTest() {
        CourierLogin loginData = new CourierLogin("wrong_" + courier.getLogin(), courier.getPassword());

        courierClient.loginCourier(loginData)
                .then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    public void courierLoginWithWrongPasswordTest() {
        CourierLogin loginData = new CourierLogin(courier.getLogin(), "wrong_password");

        courierClient.loginCourier(loginData)
                .then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    public void courierLoginWithNonExistentCourierTest() {
        CourierLogin loginData = new CourierLogin("nonexistent_login", "nonexistent_password");

        courierClient.loginCourier(loginData)
                .then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }
}