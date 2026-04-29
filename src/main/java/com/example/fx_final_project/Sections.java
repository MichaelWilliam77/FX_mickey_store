package com.example.fx_final_project;

public class Sections {
    // ملف تجميعي للأقسام
}

// 3. المبدأ: Inheritance (الرجال)
class GentsProduct extends Product {
    public GentsProduct(String name, double price, String imagePath) {
        super(name, price, imagePath);
    }
    @Override
    public String getCategoryType() { return "Men's Wear"; }
}

// 4. المبدأ: Inheritance (السيدات)
class LadiesProduct extends Product {
    public LadiesProduct(String name, double price, String imagePath) {
        super(name, price, imagePath);
    }
    @Override
    public String getCategoryType() { return "Women's Wear"; }
}

// 5. المبدأ: Inheritance & Interface implementation (الأطفال)
class KidsProduct extends Product implements Discountable {
    public KidsProduct(String name, double price, String imagePath) {
        super(name, price, imagePath);
    }
    @Override
    public String getCategoryType() { return "Children's Wear"; }

    @Override
    public void applyDiscount(double percentage) {
        this.price -= this.price * (percentage / 100);
    }
}