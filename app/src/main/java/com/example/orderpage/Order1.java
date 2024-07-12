package com.example.orderpage;
public class Order1 {
    private int orderId;
    private String name;

    public Order1(int orderId, String name) {
        this.orderId = orderId;
        this.name = name;
    }

    public int getOrderId() {
        return orderId;
    }

    public String getName() {
        return name;
    }
}

