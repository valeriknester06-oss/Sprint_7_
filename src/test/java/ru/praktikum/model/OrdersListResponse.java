package ru.praktikum.model;

import java.util.List;
import java.util.Map;

public class OrdersListResponse {
    private List<Map<String, Object>> orders;

    public List<Map<String, Object>> getOrders() {
        return orders;
    }
}