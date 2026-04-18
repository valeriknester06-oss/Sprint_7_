package ru.praktikum;

import org.junit.Before;
import org.junit.Test;
import ru.praktikum.client.CourierClient;
import ru.praktikum.data.CourierGenerator;
import ru.praktikum.model.Courier;

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
    public void deleteCourierSuccessTest() {
        courierClient.deleteCourier(courierId)
                .then()
                .statusCode(200)
                .body("ok", equalTo(true));
    }

    @Test
    public void deleteCourierWithoutIdTest() {
        courierClient.deleteCourierWithoutId()
                .then()
                .statusCode(404);
    }

    @Test
    public void deleteCourierWithNonExistentIdTest() {
        courierClient.deleteCourier(999999)
                .then()
                .statusCode(404);
    }
}