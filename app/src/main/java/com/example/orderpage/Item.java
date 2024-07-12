package com.example.orderpage;

import java.io.Serializable;

public class Item implements Serializable {
    private String itemName;
    private int quantity;
    private double cost;

    public Item(String itemName, int quantity, double cost) {
        this.itemName = itemName;
        this.quantity = quantity;
        this.cost = cost;
    }

    // Getter methods
    public String getItemName() {
        return itemName;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getCost() {
        return cost;
    }
}
