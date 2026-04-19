package ru.praktikum.model;

import java.util.List;
import java.util.Map;

public class OrdersListResponse {

    private List<Map<String, Object>> orders;

    public OrdersListResponse() {
    }

    public List<Map<String, Object>> getOrders() {
        return orders;
    }

    public void setOrders(List<Map<String, Object>> orders) {
        this.orders = orders;
    }
}