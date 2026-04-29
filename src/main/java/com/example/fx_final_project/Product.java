package com.example.fx_final_project;

// 1. المبدأ: Interface
interface Discountable {
    void applyDiscount(double percentage);
}

// 2. المبدأ: Abstract Class
public abstract class Product {
    protected String name;
    protected double price;
    protected String imagePath;

    public Product(String name, double price, String imagePath) {
        this.name = name;
        this.price = price;
        this.imagePath = imagePath;
    }

    // Abstract method: لازم كل ابن يعملها Override بطريقته
    public abstract String getCategoryType();

    // Getters
    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getImagePath() { return imagePath; }
}