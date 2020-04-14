package com.example.krunked;

public class Drink {

    private String name;
    private String price;

    public Drink(String name, String price) {
        this.name = name;
        this.price = price;
    }

    public String getDrinkName() {
        return name;
    }

    public String getDrinkPrice() {
        return price;
    }

    public void editName(String name) {
        this.name = name;
    }

    public void editPrice(String price) {
        this.price = price;
    }
}
