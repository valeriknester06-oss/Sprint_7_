package ru.praktikum.data;

import ru.praktikum.model.Courier;
import ru.praktikum.model.CourierLogin;

import java.util.UUID;

public class CourierGenerator {

    public static Courier getRandomCourier() {
        String unique = UUID.randomUUID().toString().replace("-", "").substring(0, 10);
        return new Courier(
                "login_" + unique,
                "pass_" + unique,
                "name_" + unique
        );
    }

    public static CourierLogin fromCourier(Courier courier) {
        return new CourierLogin(courier.getLogin(), courier.getPassword());
    }
}