package com.example.fx_final_project;

import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.sql.*;
import java.util.ArrayList;

public class HelloApplication extends Application {

    // مبدأ الـ Association: التطبيق مرتبط بكائن مستخدم
    private User currentUser = null;
    private BorderPane mainLayout;

    // مبدأ الـ Aggregation: السلة تحتوي على قائمة من كائنات OrderItem
    private final ArrayList<OrderItem> myCart = new ArrayList<>();

    // مبدأ الـ Composition: الـ OrderItem جزء لا يتجزأ من منطق السلة هنا
    public static class OrderItem {
        Product product;
        String size;
        public OrderItem(Product p, String s) { this.product = p; this.size = s; }
    }

    @Override
    public void start(Stage stage) {
        mainLayout = new BorderPane();

        // Header
        VBox topContainer = new VBox(5);
        Label title = new Label("MICKEY STORE");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        Label slogan = new Label("Not just clothes, it's a store you could rely on");
        slogan.getStyleClass().add("slogan");
        topContainer.getChildren().addAll(title, slogan);
        topContainer.setAlignment(Pos.CENTER);
        topContainer.setPadding(new Insets(15));
        topContainer.getStyleClass().add("header");

        mainLayout.setTop(topContainer);
        showLoginScreen();

        Scene scene = new Scene(mainLayout, 950, 750);
        try {
            scene.getStylesheets().add(getClass().getResource("mickey.css").toExternalForm());
        } catch (Exception e) {
            System.out.println("CSS file not found, continuing with default styles.");
        }

        stage.setTitle("Mickey Store - OOP Edition");
        stage.setScene(scene);
        stage.show();
    }

    // ============================================
    // 1. LOGIN & DATABASE (Exception Handling)
    // ============================================
    private void showLoginScreen() {
        GridPane root = new GridPane();
        root.setAlignment(Pos.CENTER); root.setHgap(10); root.setVgap(10);

        TextField nameField = new TextField();
        PasswordField passField = new PasswordField();
        Button loginBtn = new Button("Sign In");
        loginBtn.getStyleClass().add("primary-btn");

        loginBtn.setOnAction(e -> {
            // المبدأ: Exception Handling للتعامل مع أخطاء قاعدة البيانات
            try (Connection con = DBConnection.getConnection()) {
                String sql = "SELECT name FROM users WHERE name=? AND password=?";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setString(1, nameField.getText().trim());
                ps.setString(2, passField.getText().trim());
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    currentUser = new User(rs.getString("name"));
                    mainLayout.setCenter(createCategoriesPane());
                } else {
                    showAlert(Alert.AlertType.ERROR, "Login Error", "Invalid username or password.");
                }
            } catch (SQLException ex) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Could not connect to database.");
                ex.printStackTrace();
            }
        });

        root.add(new Label("Username:"), 0, 0); root.add(nameField, 1, 0);
        root.add(new Label("Password:"), 0, 1); root.add(passField, 1, 1);
        root.add(loginBtn, 1, 2);
        mainLayout.setCenter(root);
    }

    // ============================================
    // 2. CATEGORIES (Using Polymorphism)
    // ============================================
    private GridPane createCategoriesPane() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER); grid.setHgap(20); grid.setVgap(20);

        Button gentsBtn = new Button("Gents");
        gentsBtn.setOnAction(e -> mainLayout.setCenter(GentsSection()));

        Button kidsBtn = new Button("Kids");
        kidsBtn.setOnAction(e -> mainLayout.setCenter(KidsSection()));

        Button cartBtn = new Button("My Cart (" + myCart.size() + ")");
        cartBtn.getStyleClass().add("btn-warning");
        cartBtn.setOnAction(e -> showCart());

        grid.add(new Label("Welcome, " + currentUser.getUsername()), 0, 0);
        grid.add(gentsBtn, 0, 1); grid.add(kidsBtn, 1, 1);
        grid.add(cartBtn, 0, 2, 2, 1);

        return grid;
    }

    // ============================================
    // 3. SECTIONS (Applying Inheritance)
    // ============================================
    private ScrollPane GentsSection() {
        ArrayList<Product> products = new ArrayList<>();
        // Polymorphism: نضع GentsProduct داخل مرجع من نوع Product
        products.add(new GentsProduct("Casual Shirt", 25.0, "shirt.jpg"));
        products.add(new GentsProduct("Jeans Pants", 40.0, "jeans.jpg"));
        return createSectionGrid("Gents Collection", products);
    }

    private ScrollPane KidsSection() {
        ArrayList<Product> products = new ArrayList<>();
        KidsProduct k1 = new KidsProduct("Baby Suit", 30.0, "baby.jpg");
        // استخدام الـ Interface: عمل خصم خاص لملابس الأطفال
        k1.applyDiscount(10);
        products.add(k1);
        return createSectionGrid("Kids Collection", products);
    }

    private ScrollPane createSectionGrid(String titleStr, ArrayList<Product> products) {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20)); grid.setHgap(25); grid.setVgap(25);

        Label title = new Label(titleStr);
        title.getStyleClass().add("section-title");
        grid.add(title, 0, 0, 3, 1);

        int col = 0, row = 1;
        for (Product p : products) {
            VBox card = new VBox(10);
            card.getStyleClass().add("product-card");
            card.setAlignment(Pos.CENTER);

            Label name = new Label(p.getName());
            Label price = new Label("$" + p.getPrice());
            Label type = new Label(p.getCategoryType()); // ينادي الميثود الـ Abstract
            type.setStyle("-fx-font-size: 10px; -fx-text-fill: gray;");

            Button buyBtn = new Button("Add to Cart");
            buyBtn.getStyleClass().add("primary-btn");
            buyBtn.setOnAction(e -> {
                myCart.add(new OrderItem(p, "Medium"));
                showAlert(Alert.AlertType.INFORMATION, "Success", p.getName() + " added!");
            });

            card.getChildren().addAll(name, type, price, buyBtn);
            grid.add(card, col++, row);
            if (col == 3) { col = 0; row++; }
        }

        Button backBtn = new Button("Back");
        backBtn.setOnAction(e -> mainLayout.setCenter(createCategoriesPane()));
        grid.add(backBtn, 0, row + 1);

        return new ScrollPane(grid);
    }

    // ============================================
    // 4. CHECKOUT (Custom Exception Handling)
    // ============================================
    private void showCart() {
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.TOP_CENTER);

        Label totalLbl = new Label("Total: $" + calculateTotal());
        TextField addressField = new TextField();
        addressField.setPromptText("Shipping Address");

        Button payBtn = new Button("Confirm Purchase");
        payBtn.getStyleClass().add("btn-success");

        payBtn.setOnAction(e -> {
            try {
                // استخدام الـ Custom Exception الذي أنشأناه
                if (addressField.getText().isEmpty()) {
                    throw new PaymentException("Address cannot be empty!");
                }
                if (myCart.isEmpty()) {
                    throw new PaymentException("Your cart is empty!");
                }

                showAlert(Alert.AlertType.INFORMATION, "Order Placed", "Sending to: " + addressField.getText());
                myCart.clear();
                mainLayout.setCenter(createCategoriesPane());

            } catch (PaymentException ex) {
                showAlert(Alert.AlertType.WARNING, "Checkout Issue", ex.getMessage());
            }
        });

        layout.getChildren().addAll(new Label("Your Items:"), totalLbl, addressField, payBtn);
        mainLayout.setCenter(layout);
    }

    private double calculateTotal() {
        double total = 0;
        for (OrderItem item : myCart) {
            total += item.product.getPrice();
        }
        return total;
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch();
    }
}