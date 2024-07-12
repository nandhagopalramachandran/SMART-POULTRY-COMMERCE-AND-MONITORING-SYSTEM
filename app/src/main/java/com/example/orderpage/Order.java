package com.example.orderpage;

public class Order {
    private int orderId;
    private String name;
    private String mobileNumber;
    private String address;
    private String itemName;
    private int quantity;
    private int cost;
    private int totalCost;

    public Order(int orderId, String name, String mobileNumber, String address, String itemName, int quantity, int cost, int totalCost) {
        this.orderId = orderId;
        this.name = name;
        this.mobileNumber = mobileNumber;
        this.address = address;
        this.itemName = itemName;
        this.quantity = quantity;
        this.cost = cost;
        this.totalCost = totalCost;
    }

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

    public String getItemName() {
        return itemName;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getCost() {
        return cost;
    }

    public int getTotalCost() {
        return totalCost;
    }
}
