package com.example.orderpage;

public class Hen {
    private String name;
    private String breed;
    private String age;
    private String price;
    private int imageResource;
    private int quantity; // New field

    public Hen(String name, String breed, String age, String price, int imageResource) {
        this.name = name;
        this.breed = breed;
        this.age = age;
        this.price = price;
        this.imageResource = imageResource;
        this.quantity = 0;
    }

    public String getName() {
        return name;
    }

    public String getBreed() {
        return breed;
    }

    public String getAge() {
        return age;
    }

    public String getPrice() {
        return price;
    }

    public int getImageResource() {
        return imageResource;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return String.format("%s - %s | %s | %s", name, breed, age, price);
    }
}

