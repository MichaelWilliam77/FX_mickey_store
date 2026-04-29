package com.example.fx_final_project;

// 6. المبدأ: Encapsulation & Model Class
class User {
    private String username;
    public User(String username) { this.username = username; }
    public String getUsername() { return username; }
}

// 7. المبدأ: Custom Exception Handling
class PaymentException extends Exception {
    public PaymentException(String message) {
        super(message);
    }
}