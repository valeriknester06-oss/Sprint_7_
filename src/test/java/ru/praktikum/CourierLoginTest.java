package ru.praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.praktikum.client.CourierClient;
import ru.praktikum.data.CourierGenerator;
import ru.praktikum.model.Courier;
import ru.praktikum.model.CourierLogin;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.apache.http.HttpStatus.SC_OK;
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
    @DisplayName("Успешный логин курьера")
    @Description("Проверка, что курьер может авторизоваться по корректному логину и паролю")
    public void courierLoginSuccessTest() {
        CourierLogin loginData = CourierGenerator.fromCourier(courier);

        courierClient.loginCourier(loginData)
                .then()
                .statusCode(SC_OK)
                .body("id", notNullValue());
    }

    @Test
    @DisplayName("Логин курьера без логина")
    @Description("Проверка, что при попытке авторизации без логина возвращается ошибка 400")
    public void courierLoginWithoutLoginTest() {
        CourierLogin loginData = new CourierLogin(null, courier.getPassword());

        courierClient.loginCourier(loginData)
                .then()
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Логин курьера без пароля")
    @Description("Проверка, что при попытке авторизации без пароля возвращается ошибка 400")
    public void courierLoginWithoutPasswordTest() {
        CourierLogin loginData = new CourierLogin(courier.getLogin(), "");

        courierClient.loginCourier(loginData)
                .then()
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Логин курьера с неверным логином")
    @Description("Проверка, что при авторизации с неверным логином возвращается ошибка 404")
    public void courierLoginWithWrongLoginTest() {
        CourierLogin loginData = new CourierLogin("wrong_" + courier.getLogin(), courier.getPassword());

        courierClient.loginCourier(loginData)
                .then()
                .statusCode(SC_NOT_FOUND)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Логин курьера с неверным паролем")
    @Description("Проверка, что при авторизации с неверным паролем возвращается ошибка 404")
    public void courierLoginWithWrongPasswordTest() {
        CourierLogin loginData = new CourierLogin(courier.getLogin(), "wrong_password");

        courierClient.loginCourier(loginData)
                .then()
                .statusCode(SC_NOT_FOUND)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Логин несуществующего курьера")
    @Description("Проверка, что при авторизации несуществующего курьера возвращается ошибка 404")
    public void courierLoginWithNonExistentCourierTest() {
        CourierLogin loginData = new CourierLogin("nonexistent_login", "nonexistent_password");

        courierClient.loginCourier(loginData)
                .then()
                .statusCode(SC_NOT_FOUND)
                .body("message", equalTo("Учетная запись не найдена"));
    }
}