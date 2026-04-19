package ru.praktikum.data;

import ru.praktikum.model.Order;

import java.util.List;

public class OrderGenerator {

    public static Order getDefaultOrder() {
        return new Order(
                "Валерия",
                "Нестерова",
                "г. Краснодар, ул. Пушкина, д. 10",
                "4",
                "+79001234567",
                3,
                "2026-04-20",
                "Тестовый заказ",
                List.of("BLACK")
        );
    }

    public static Order getOrderWithColor(List<String> colors) {
        return new Order(
                "Валерия",
                "Нестерова",
                "г. Краснодар, ул. Пушкина, д. 10",
                "4",
                "+79001234567",
                3,
                "2026-04-20",
                "Тестовый заказ",
                colors
        );
    }
}