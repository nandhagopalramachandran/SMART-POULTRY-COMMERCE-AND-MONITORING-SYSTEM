// ConfirmedOrder.java
package com.example.orderpage;

import java.io.Serializable;
import java.util.List;

public class ConfirmedOrder implements Serializable {
    private int orderId;
    private String name;
    private String mobileNumber;
    private String address;
    private List<Item> items;
    private double totalCost;

    public ConfirmedOrder(int orderId, String name, String mobileNumber, String address, List<Item> items, double totalCost) {
        this.orderId = orderId;
        this.name = name;
        this.mobileNumber = mobileNumber;
        this.address = address;
        this.items = items;
        this.totalCost = totalCost;
    }

    // Getter methods
    public int getOrderId() {
        return orderId;
    }

    public String getName() {
        return name;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getAddress() {
        return address;
    }

    public List<Item> getItems() {
        return items;
    }

    public double getTotalCost() {
        return totalCost;
    }
}
