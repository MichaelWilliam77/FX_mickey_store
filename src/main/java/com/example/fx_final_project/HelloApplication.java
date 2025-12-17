package com.example.fx_final_project;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;

public class HelloApplication extends Application {

    // Main layout
    private BorderPane mainLayout;

    // Virtual Database (The Cart) - مخزن مؤقت للمشتريات
    private final ArrayList<Order> myCart = new ArrayList<>();

    // Inner Class to represent an Order
    public static class Order {
        String name;
        String price;
        String size;

        public Order(String name, String price, String size) {
            this.name = name;
            this.price = price;
            this.size = size;
        }
    }

    @Override
    public void start(Stage stage) throws IOException {
        mainLayout = new BorderPane();

        // 1. Header (Logo + Slogan)
        ImageView logo = getLogoView();
        Label slogan = new Label("not just clothes, it's a store you could rely on");
        slogan.setStyle("-fx-font-style: italic; -fx-text-fill: #2c3e50; -fx-font-size: 14px; -fx-font-weight: bold;");

        VBox topContainer = new VBox(5);
        topContainer.getChildren().addAll(logo, slogan);
        topContainer.setAlignment(Pos.CENTER);
        topContainer.setPadding(new Insets(15, 0, 15, 0));
        topContainer.getStyleClass().add("header");

        mainLayout.setTop(topContainer);
        mainLayout.setCenter(createLoginPane());

        // 2. Scene Setup
        Scene scene = new Scene(mainLayout, 900, 700); // كبرنا الشاشة عشان تسع صفحة الدفع
        try {
            scene.getStylesheets().add(getClass().getResource("mickey.css").toExternalForm());
        } catch (Exception e) {
            System.out.println("CSS file not found");
        }

        stage.setTitle("Mickey Store");
        stage.setScene(scene);
        stage.show();
    }

    // ============================================
    // 1. LOGIN & SIGNUP SCREENS
    // ============================================

    private GridPane createLoginPane() {
        GridPane root = new GridPane();
        root.setAlignment(Pos.CENTER);
        root.setHgap(10);
        root.setVgap(10);
        root.setPadding(new Insets(20));

        Label nameLabel = new Label("Name");
        TextField nameField = new TextField();
        nameField.setPromptText("Enter your name");

        Label passLabel = new Label("Password: ");
        PasswordField passField = new PasswordField();
        passField.setPromptText("Enter your password");

        Button signInBtn = new Button("Sign In");
        signInBtn.getStyleClass().add("primary-btn");
        signInBtn.setOnAction(e -> {
            if (nameField.getText().equals("mickey") && passField.getText().equals("123")) {
                mainLayout.setCenter(createCategoriesPane());
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Wrong username or password");
            }
        });

        Button goToSignUpBtn = new Button("Sign Up");
        goToSignUpBtn.getStyleClass().add("primary-btn");
        goToSignUpBtn.setOnAction(e -> mainLayout.setCenter(createSignupPane()));

        root.add(nameLabel, 0, 0); root.add(nameField, 1, 0);
        root.add(passLabel, 0, 1); root.add(passField, 1, 1);
        root.add(signInBtn, 0, 2); root.add(goToSignUpBtn, 1, 2);

        return root;
    }

    private GridPane createSignupPane() {
        GridPane signupRoot = new GridPane();
        signupRoot.setPadding(new Insets(20));
        signupRoot.setAlignment(Pos.CENTER);
        signupRoot.setVgap(10);
        signupRoot.setHgap(10);

        Label nameLabel = new Label("Name:"); TextField nameField = new TextField();
        Label numberLabel = new Label("Phone:"); TextField numberField = new TextField();
        Label passLabel = new Label("Password:"); PasswordField passField = new PasswordField();
        Label repassLabel = new Label("Re-Password:"); PasswordField repassField = new PasswordField();
        HBox genderBox = new HBox(15, new RadioButton("Male"), new RadioButton("Female"));

        Button registerBtn = new Button("Register");
        registerBtn.getStyleClass().add("primary-btn");
        registerBtn.setOnAction(e -> {
            if (nameField.getText().isEmpty() || passField.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Form Error", "Please fill all fields");
                return;
            }
            if (passField.getText().equals(repassField.getText())) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Account created!");
                mainLayout.setCenter(createLoginPane());
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Passwords do not match!");
            }
        });

        Button backBtn = new Button("Back to Login");
        backBtn.getStyleClass().add("primary-btn");
        backBtn.setOnAction(e -> mainLayout.setCenter(createLoginPane()));

        signupRoot.add(nameLabel, 0, 0); signupRoot.add(nameField, 1, 0);
        signupRoot.add(numberLabel, 0, 1); signupRoot.add(numberField, 1, 1);
        signupRoot.add(passLabel, 0, 2); signupRoot.add(passField, 1, 2);
        signupRoot.add(repassLabel, 0, 3); signupRoot.add(repassField, 1, 3);
        signupRoot.add(genderBox, 1, 4);

        HBox btns = new HBox(10, registerBtn, backBtn);
        btns.setAlignment(Pos.CENTER_RIGHT);
        signupRoot.add(btns, 1, 5);

        return signupRoot;
    }

    // ============================================
    // 2. CATEGORIES (HOME) & CART
    // ============================================

    private GridPane createCategoriesPane() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(20);
        grid.setVgap(20);
        grid.setPadding(new Insets(20));

        Label welcomeLabel = new Label("Choose a Category:");
        welcomeLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Button gentsBtn = new Button("Gents");
        gentsBtn.setPrefSize(120, 50);
        gentsBtn.setOnAction(e -> mainLayout.setCenter(GentsSection()));

        Button ladiesBtn = new Button("Ladies");
        ladiesBtn.setPrefSize(120, 50);
        ladiesBtn.setOnAction(e -> mainLayout.setCenter(LadiesSection()));

        Button kidsBtn = new Button("Kids");
        kidsBtn.setPrefSize(120, 50);
        kidsBtn.setOnAction(e -> mainLayout.setCenter(KidsSection()));

        // --- تعديل حجم الزرار هنا لـ 200 عشان الكلام يظهر ---
        Button cartBtn = new Button("My Cart (" + myCart.size() + ")");
        cartBtn.setPrefSize(200, 50);
        cartBtn.setStyle("-fx-background-color: #ff9f43; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 10;");
        cartBtn.setOnAction(e -> mainLayout.setCenter(createCartScreen()));

        Button logoutBtn = new Button("Logout");
        logoutBtn.setStyle("-fx-background-color: #702963; -fx-text-fill: white;");
        logoutBtn.setOnAction(e -> {
            myCart.clear();
            mainLayout.setCenter(createLoginPane());
        });

        grid.add(welcomeLabel, 0, 0, 3, 1);
        grid.add(gentsBtn, 0, 1);
        grid.add(ladiesBtn, 1, 1);
        grid.add(kidsBtn, 2, 1);
        grid.add(cartBtn, 0, 2, 3, 1);
        grid.add(logoutBtn, 0, 4, 3, 1);

        return grid;
    }

    private ScrollPane createCartScreen() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(15);
        grid.setPadding(new Insets(20));

        Label title = new Label("My Shopping Cart");
        title.getStyleClass().add("section-title");
        grid.add(title, 0, 0);

        if (myCart.isEmpty()) {
            Label emptyLbl = new Label("Your cart is empty!");
            emptyLbl.setStyle("-fx-font-size: 18px; -fx-text-fill: gray;");
            grid.add(emptyLbl, 0, 1);

            Button backBtn = new Button("Back to Categories");
            backBtn.getStyleClass().add("primary-btn");
            backBtn.setOnAction(e -> mainLayout.setCenter(createCategoriesPane()));
            grid.add(backBtn, 0, 2);
        } else {
            int row = 1;
            for (Order order : myCart) {
                HBox itemRow = new HBox(20);
                itemRow.setAlignment(Pos.CENTER_LEFT);
                itemRow.setStyle("-fx-background-color: white; -fx-padding: 10; -fx-background-radius: 10; -fx-border-color: #ddd; -fx-border-radius: 10;");
                itemRow.setPrefWidth(550); // وسعنا الصف شوية

                Label nameLbl = new Label(order.name); nameLbl.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;"); nameLbl.setPrefWidth(120);
                Label sizeLbl = new Label("Size: " + order.size); sizeLbl.setPrefWidth(100);
                Label priceLbl = new Label(order.price); priceLbl.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");

                Button deleteBtn = new Button("Remove");
                deleteBtn.setStyle("-fx-background-color: #ff4d4d; -fx-text-fill: white; -fx-font-size: 10px;");
                deleteBtn.setOnAction(e -> {
                    myCart.remove(order);
                    mainLayout.setCenter(createCartScreen());
                });

                itemRow.getChildren().addAll(nameLbl, sizeLbl, priceLbl, new Region(), deleteBtn);
                HBox.setHgrow(itemRow.getChildren().get(3), Priority.ALWAYS);
                grid.add(itemRow, 0, row++);
            }

            // Buttons: Back & Proceed to Checkout
            Button backBtn = new Button("Back Shopping");
            backBtn.getStyleClass().add("primary-btn");
            backBtn.setStyle("-fx-background-color: #95a5a6;");
            backBtn.setOnAction(e -> mainLayout.setCenter(createCategoriesPane()));

            Button checkoutBtn = new Button("Proceed to Checkout ➔");
            checkoutBtn.getStyleClass().add("primary-btn");
            checkoutBtn.setStyle("-fx-background-color: #27ae60; -fx-font-size: 14px;");
            checkoutBtn.setOnAction(e -> mainLayout.setCenter(createPaymentScreen()));

            HBox actions = new HBox(15, backBtn, checkoutBtn);
            actions.setAlignment(Pos.CENTER);
            grid.add(actions, 0, row + 1);
        }

        ScrollPane scroll = new ScrollPane(grid);
        scroll.setFitToWidth(true);
        return scroll;
    }

    // ============================================
    // 3. CHECKOUT / PAYMENT SCREEN (NEW)
    // ============================================

    private ScrollPane createPaymentScreen() {
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setPadding(new Insets(30));
        layout.setStyle("-fx-background-color: #f4f4f4;");

        // Title & Total
        Label title = new Label("Checkout");
        title.getStyleClass().add("section-title");

        double totalAmount = calculateTotal();
        Label totalLbl = new Label("Total to Pay: $" + totalAmount);
        totalLbl.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #27ae60;");

        // Shipping Address
        VBox shippingBox = new VBox(10);
        shippingBox.setStyle("-fx-background-color: white; -fx-padding: 20; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");

        Label shipTitle = new Label("📍 Shipping Address");
        shipTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");
        TextField addressField = new TextField(); addressField.setPromptText("Enter your full address");
        TextField phoneField = new TextField(); phoneField.setPromptText("Contact Phone Number");
        shippingBox.getChildren().addAll(shipTitle, addressField, phoneField);

        // Payment Method
        VBox paymentBox = new VBox(10);
        paymentBox.setStyle("-fx-background-color: white; -fx-padding: 20; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");

        Label payTitle = new Label("💳 Payment Method");
        payTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

        RadioButton cardRb = new RadioButton("Credit Card");
        RadioButton cashRb = new RadioButton("Cash on Delivery");
        ToggleGroup group = new ToggleGroup();
        cardRb.setToggleGroup(group); cashRb.setToggleGroup(group);
        cardRb.setSelected(true);

        GridPane cardGrid = new GridPane();
        cardGrid.setHgap(10); cardGrid.setVgap(10);
        TextField cardNum = new TextField(); cardNum.setPromptText("0000 0000 0000 0000");
        TextField cardName = new TextField(); cardName.setPromptText("Cardholder Name");
        TextField cardExp = new TextField(); cardExp.setPromptText("MM/YY"); cardExp.setPrefWidth(80);
        TextField cardCvv = new TextField(); cardCvv.setPromptText("CVV"); cardCvv.setPrefWidth(80);

        cardGrid.add(new Label("Card Number:"), 0, 0); cardGrid.add(cardNum, 1, 0);
        cardGrid.add(new Label("Holder Name:"), 0, 1); cardGrid.add(cardName, 1, 1);
        HBox secInfo = new HBox(10, cardExp, cardCvv);
        cardGrid.add(new Label("Exp/CVV:"), 0, 2); cardGrid.add(secInfo, 1, 2);

        cashRb.setOnAction(e -> cardGrid.setDisable(true));
        cardRb.setOnAction(e -> cardGrid.setDisable(false));

        paymentBox.getChildren().addAll(payTitle, cardRb, cashRb, new Separator(), cardGrid);

        // Actions
// ... (باقي الكود زي ما هو لحد تعريف الزرار)

        Button payBtn = new Button("Confirm Payment ($" + totalAmount + ")");
        payBtn.getStyleClass().add("primary-btn");
        payBtn.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold;");
        payBtn.setPrefWidth(250);

        payBtn.setOnAction(e -> {
            // 1. التحقق من العنوان والتليفون (للكل)
            if(addressField.getText().trim().isEmpty() || phoneField.getText().trim().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Missing Info", "Please enter your shipping address and phone.");
                return; // وقف الكود هنا
            }

            // 2. التحقق من بيانات الفيزا (فقط لو مختار Credit Card)
            if (cardRb.isSelected()) {
                // التأكد إن رقم الكارت 16 رقم بالظبط (Regex)
                if (!cardNum.getText().matches("\\d{16}")) {
                    showAlert(Alert.AlertType.ERROR, "Invalid Card", "Card number must be 16 digits (Numbers only).");
                    return;
                }

                // التأكد إن الـ CVV مكون من 3 أرقام
                if (!cardCvv.getText().matches("\\d{3}")) {
                    showAlert(Alert.AlertType.ERROR, "Invalid CVV", "CVV must be 3 digits.");
                    return;
                }

                // التأكد إن التاريخ مش فاضي
                if (cardExp.getText().isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Invalid Date", "Please enter expiry date.");
                    return;
                }
            }

            // 3. لو عدى من كل الشروط اللي فوق، تم الدفع بنجاح
            showAlert(Alert.AlertType.INFORMATION, "Payment Successful",
                    "Order Placed Successfully!\nShipping to: " + addressField.getText());

            myCart.clear(); // فضي السلة
            mainLayout.setCenter(createCategoriesPane()); // ارجع للصفحة الرئيسية
        });

        Button cancelBtn = new Button("Cancel");
        cancelBtn.getStyleClass().add("primary-btn");
        cancelBtn.setStyle("-fx-background-color: #e74c3c;");
        cancelBtn.setOnAction(e -> mainLayout.setCenter(createCartScreen()));

        HBox actions = new HBox(20, payBtn, cancelBtn);
        actions.setAlignment(Pos.CENTER);

        layout.getChildren().addAll(title, totalLbl, shippingBox, paymentBox, actions);
        ScrollPane scroll = new ScrollPane(layout);
        scroll.setFitToWidth(true);
        return scroll;
    }

    // ============================================
    // 4. PRODUCT SECTIONS & HELPERS
    // ============================================

    private ScrollPane GentsSection() {
        return createSectionGrid("Gents Section", new String[][]{
                {"Casual Shirt", "$25", "/com/example/fx_final_project/image/shirt.jpg"},
                {"Jeans Pants", "$40", "/com/example/fx_final_project/image/jeans.jpg"},
                {"Hoodie", "$35", "/com/example/fx_final_project/image/hoodie.jpg"},
                {"Sport clothe", "$20", "/com/example/fx_final_project/image/sportt.jpg"},
                {"Jacket", "$20", "/com/example/fx_final_project/image/jacket.jpg"},
                {"Sneaker", "$35", "/com/example/fx_final_project/image/formal.jpg"}
        });
    }

    private ScrollPane LadiesSection() {
        return createSectionGrid("Ladies Section", new String[][]{
                {"Casual jacket", "$25", "/ladie1.jpeg"},
                {"Jeans Pants", "$40", "/ladie2.jpeg"},
                {"Coat", "$35", "/ladie4.jpeg"},
                {"Red Jacket", "$50", "/ladie4.jpeg"},
                {"Grey jacket", "$60", "/ladie5.jpeg"},
                {"Denim Jacket", "$45", "/ladie6.jpeg"}
        });
    }

    private ScrollPane KidsSection() {
        return createSectionGrid("Kids Section", new String[][]{
                {"Kids Shirt", "$12", "/com/example/fx_final_project/image/shirttt.jpeg"},
                {"Dress", "$20", "/com/example/fx_final_project/image/skerts.jpg"},
                {"Baby Clothes", "$13", "/com/example/fx_final_project/image/baby.jpg"},
                {"Sport Clothes", "$10", "/com/example/fx_final_project/image/sport2.jpg"},
                {"Jacket", "$15", "/com/example/fx_final_project/image/kjacket.jpg"},
                {"Shoes", "$13", "/com/example/fx_final_project/image/shoes.jpg"}
        });
    }

    private ScrollPane createSectionGrid(String titleStr, String[][] productsData) {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setHgap(30);
        grid.setVgap(45);
        grid.setAlignment(Pos.CENTER);

        Label title = new Label(titleStr);
        title.getStyleClass().add("section-title");
        grid.add(title, 0, 0, 3, 1);

        int col = 0;
        int row = 1;
        for (String[] prod : productsData) {
            grid.add(createCard(prod[0], prod[1], prod[2]), col, row);
            col++;
            if (col == 3) {
                col = 0;
                row++;
            }
        }

        Button backBtn = new Button("Back");
        backBtn.getStyleClass().add("primary-btn");
        backBtn.setStyle("-fx-background-color: #6c757d;");
        backBtn.setOnAction(e -> mainLayout.setCenter(createCategoriesPane()));
        grid.add(backBtn, 1, row + 1);

        ScrollPane scroll = new ScrollPane(grid);
        scroll.setFitToWidth(true);
        return scroll;
    }

    private VBox createCard(String name, String price, String imagePath) {
        VBox card = new VBox(10);
        card.setAlignment(Pos.CENTER);
        card.getStyleClass().add("product-card");

        ImageView imageView = new ImageView(loadImage(imagePath));
        imageView.setFitWidth(120);
        imageView.setPreserveRatio(true);

        Label productName = new Label(name);
        productName.getStyleClass().add("product-name");

        Label productPrice = new Label(price);
        productPrice.getStyleClass().add("product-price");

        Button btn = new Button("Buy");
        btn.getStyleClass().add("primary-btn");
        btn.setOnAction(e -> showProductDetails(name, price, imagePath));

        card.getChildren().addAll(imageView, productName, productPrice, btn);
        return card;
    }

    private void showProductDetails(String name, String price, String imagePath) {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Product Details");

        VBox layout = new VBox(15);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: white;");

        ImageView img = new ImageView(loadImage(imagePath));
        img.setFitWidth(150); img.setPreserveRatio(true);

        Label nameLbl = new Label(name); nameLbl.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        Label priceLbl = new Label(price); priceLbl.setStyle("-fx-font-size: 18px; -fx-text-fill: green;");
        Label sizeLbl = new Label("Select Size:");
        ComboBox<String> sizeBox = new ComboBox<>();
        sizeBox.getItems().addAll("Small", "Medium", "Large", "X-Large");
        sizeBox.setValue("Medium");

        Button confirmBtn = new Button("Confirm & Add to Cart");
        confirmBtn.getStyleClass().add("primary-btn");
        confirmBtn.setOnAction(e -> {
            myCart.add(new Order(name, price, sizeBox.getValue()));
            showAlert(Alert.AlertType.INFORMATION, "Success", "Added to Cart!");
            popupStage.close();
        });

        layout.getChildren().addAll(img, nameLbl, priceLbl, sizeLbl, sizeBox, confirmBtn);
        Scene scene = new Scene(layout);
        popupStage.setMinWidth(300);
        try { scene.getStylesheets().add(getClass().getResource("mickey.css").toExternalForm()); }
        catch (Exception ex) { /* ignore */ }
        popupStage.setScene(scene);
        popupStage.showAndWait();
    }

    private double calculateTotal() {
        double total = 0.0;
        for (Order order : myCart) {
            try {
                String cleanPrice = order.price.replace("$", "").trim();
                total += Double.parseDouble(cleanPrice);
            } catch (NumberFormatException e) { /* ignore */ }
        }
        return total;
    }

    private ImageView getLogoView() {
        ImageView logoView = new ImageView();
        try {
            logoView.setImage(new Image(getClass().getResourceAsStream("mickey_store.jpg")));
            logoView.setFitWidth(150); logoView.setPreserveRatio(true);
        } catch (Exception e) { /* ignore */ }
        return logoView;
    }

    private Image loadImage(String path) {
        try { return new Image(getClass().getResource(path).toExternalForm()); }
        catch (Exception e) { return new Image("https://via.placeholder.com/150"); }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title); alert.setContentText(content);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch();
    }
}