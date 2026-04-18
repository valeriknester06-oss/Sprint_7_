package ru.praktikum;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.praktikum.client.CourierClient;
import ru.praktikum.data.CourierGenerator;
import ru.praktikum.model.Courier;

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
        if (courierId != null) {
            courierClient.deleteCourier(courierId);
        }
    }

    @Test
    public void createCourierSuccessTest() {
        courierClient.createCourier(courier)
                .then()
                .statusCode(201)
                .body("ok", equalTo(true));

        courierId = courierClient.loginCourier(CourierGenerator.fromCourier(courier))
                .then()
                .extract()
                .path("id");
    }

    @Test
    public void createDuplicateCourierTest() {
        courierClient.createCourier(courier);
        courierId = courierClient.loginCourier(CourierGenerator.fromCourier(courier))
                .then()
                .extract()
                .path("id");

        courierClient.createCourier(courier)
                .then()
                .statusCode(409)
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    public void createCourierWithoutLoginTest() {
        Courier invalidCourier = new Courier(null, courier.getPassword(), courier.getFirstName());

        courierClient.createCourier(invalidCourier)
                .then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    public void createCourierWithoutPasswordTest() {
        Courier invalidCourier = new Courier(courier.getLogin(), null, courier.getFirstName());

        courierClient.createCourier(invalidCourier)
                .then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }
}