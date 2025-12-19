package com.example.fx_final_project;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class HelloApplication extends Application {

    // المستخدم الحالي
    private String currentUser = null;

    // Layout & Data
    private BorderPane mainLayout;
    private final ArrayList<Order> myCart = new ArrayList<>();

    // Inner Class for Order
    public static class Order {
        String name; String price; String size;
        public Order(String name, String price, String size) {
            this.name = name; this.price = price; this.size = size;
        }
    }

    @Override
    public void start(Stage stage) throws IOException {
        mainLayout = new BorderPane();

        // 1. Header
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

        // 2. Scene
        Scene scene = new Scene(mainLayout, 900, 700);
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
        root.setAlignment(Pos.CENTER); root.setHgap(10); root.setVgap(10); root.setPadding(new Insets(20));

        Label nameLabel = new Label("Name"); TextField nameField = new TextField(); nameField.setPromptText("Enter your name");
        Label passLabel = new Label("Password: "); PasswordField passField = new PasswordField(); passField.setPromptText("Enter your password");

        Button signInBtn = new Button("Sign In");
        signInBtn.getStyleClass().add("primary-btn");
        signInBtn.setOnAction(e -> {
            String username = nameField.getText().trim();
            String password = passField.getText().trim();

            if(username.isEmpty() || password.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Error", "Enter username and password"); return;
            }

            try (Connection con = DBConnection.getConnection()) {
                String sql = "SELECT name FROM users WHERE name=? AND password=?";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setString(1, username);
                ps.setString(2, password);

                ResultSet rs = ps.executeQuery();
                if(rs.next()) {
                    currentUser = rs.getString("name");
                    // showAlert(Alert.AlertType.INFORMATION, "Welcome", "Hello " + currentUser + "!");
                    mainLayout.setCenter(createCategoriesPane());
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Wrong username or password");
                }
                rs.close(); ps.close();
            } catch (Exception ex) {
                ex.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Database error!");
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
        signupRoot.setPadding(new Insets(20)); signupRoot.setAlignment(Pos.CENTER);
        signupRoot.setVgap(10); signupRoot.setHgap(10);

        Label nameLabel = new Label("Name:"); TextField nameField = new TextField();
        Label numberLabel = new Label("Phone:"); TextField numberField = new TextField();
        Label passLabel = new Label("Password:"); PasswordField passField = new PasswordField();
        Label repassLabel = new Label("Re-Password:"); PasswordField repassField = new PasswordField();

        // Fix Gender Issue
        RadioButton maleRb = new RadioButton("Male");
        RadioButton femaleRb = new RadioButton("Female");
        ToggleGroup tg = new ToggleGroup();
        maleRb.setToggleGroup(tg); femaleRb.setToggleGroup(tg);
        maleRb.setSelected(true);
        HBox genderBox = new HBox(15, maleRb, femaleRb);

        Button registerBtn = new Button("Register");
        registerBtn.getStyleClass().add("primary-btn");
        registerBtn.setOnAction(e -> {
            if (nameField.getText().isEmpty() || passField.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Error", "Please fill all fields"); return;
            }
            if (!passField.getText().equals(repassField.getText())) {
                showAlert(Alert.AlertType.ERROR, "Error", "Passwords do not match!"); return;
            }
            try (Connection con = DBConnection.getConnection()) {
                String sql = "INSERT INTO users(name, phone, password, gender) VALUES(?, ?, ?, ?)";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setString(1, nameField.getText().trim());
                ps.setString(2, numberField.getText().trim());
                ps.setString(3, passField.getText().trim());

                // Correct Gender Logic
                String selectedGender = maleRb.isSelected() ? "Male" : "Female";
                ps.setString(4, selectedGender);

                ps.executeUpdate();
                showAlert(Alert.AlertType.INFORMATION, "Success", "Account created!");
                mainLayout.setCenter(createLoginPane());
            } catch (Exception ex) {
                ex.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Database error or Username exists!");
            }
        });

        Button backBtn = new Button("Back to Login");
        backBtn.getStyleClass().addAll("primary-btn", "btn-secondary");
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
    // 2. CATEGORIES (HOME)
    // ============================================

    private GridPane createCategoriesPane() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER); grid.setHgap(20); grid.setVgap(20); grid.setPadding(new Insets(20));

        Label welcomeLabel = new Label("Choose a Category:");
        welcomeLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Button gentsBtn = new Button("Gents"); gentsBtn.setPrefSize(120, 50);
        gentsBtn.setOnAction(e -> mainLayout.setCenter(GentsSection()));

        Button ladiesBtn = new Button("Ladies"); ladiesBtn.setPrefSize(120, 50);
        ladiesBtn.setOnAction(e -> mainLayout.setCenter(LadiesSection()));

        Button kidsBtn = new Button("Kids"); kidsBtn.setPrefSize(120, 50);
        kidsBtn.setOnAction(e -> mainLayout.setCenter(KidsSection()));

        Button profileBtn = new Button("Profile"); profileBtn.setPrefSize(120, 50);
        profileBtn.getStyleClass().add("primary-btn");
        profileBtn.setOnAction(e -> mainLayout.setCenter(ProfileSection()));

        Button cartBtn = new Button("My Cart (" + myCart.size() + ")");
        cartBtn.setPrefSize(200, 50);
        cartBtn.getStyleClass().add("btn-warning");
        cartBtn.setOnAction(e -> mainLayout.setCenter(createCartScreen()));

        Button logoutBtn = new Button("Logout");
        logoutBtn.getStyleClass().add("btn-purple");
        logoutBtn.setOnAction(e -> {
            myCart.clear();
            currentUser = null;
            mainLayout.setCenter(createLoginPane());
        });

        grid.add(welcomeLabel, 0, 0, 4, 1);
        grid.add(gentsBtn, 0, 1); grid.add(ladiesBtn, 1, 1); grid.add(kidsBtn, 2, 1); grid.add(profileBtn, 3, 1);
        grid.add(cartBtn, 0, 2, 4, 1);
        grid.add(logoutBtn, 0, 3, 4, 1);

        return grid;
    }

    // ============================================
    // 3. CART
    // ============================================

    private ScrollPane createCartScreen() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER); grid.setVgap(15); grid.setPadding(new Insets(20));

        Label title = new Label("My Shopping Cart"); title.getStyleClass().add("section-title");
        grid.add(title, 0, 0);

        if (myCart.isEmpty()) {
            Label emptyLbl = new Label("Your cart is empty!");
            emptyLbl.setStyle("-fx-font-size: 18px; -fx-text-fill: gray;");
            grid.add(emptyLbl, 0, 1);

            Button backBtn = new Button("Back to Categories");
            backBtn.getStyleClass().addAll("primary-btn", "btn-secondary");
            backBtn.setOnAction(e -> mainLayout.setCenter(createCategoriesPane()));
            grid.add(backBtn, 0, 2);
        } else {
            int row = 1;
            for (Order order : myCart) {
                HBox itemRow = new HBox(20);
                itemRow.setAlignment(Pos.CENTER_LEFT);
                itemRow.getStyleClass().add("white-box");
                itemRow.setPrefWidth(550);

                Label nameLbl = new Label(order.name); nameLbl.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;"); nameLbl.setPrefWidth(120);
                Label sizeLbl = new Label("Size: " + order.size); sizeLbl.setPrefWidth(100);
                Label priceLbl = new Label(order.price); priceLbl.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");

                Button deleteBtn = new Button("Remove");
                deleteBtn.getStyleClass().addAll("btn-danger", "btn-sm");
                deleteBtn.setOnAction(e -> {
                    myCart.remove(order);
                    mainLayout.setCenter(createCartScreen());
                });

                itemRow.getChildren().addAll(nameLbl, sizeLbl, priceLbl, new Region(), deleteBtn);
                HBox.setHgrow(itemRow.getChildren().get(3), Priority.ALWAYS);
                grid.add(itemRow, 0, row++);
            }

            Button backBtn = new Button("Back Shopping");
            backBtn.getStyleClass().addAll("primary-btn", "btn-secondary");
            backBtn.setOnAction(e -> mainLayout.setCenter(createCategoriesPane()));

            Button checkoutBtn = new Button("Proceed to Checkout ➔");
            checkoutBtn.getStyleClass().add("btn-success");
            checkoutBtn.setOnAction(e -> mainLayout.setCenter(createPaymentScreen()));

            HBox actions = new HBox(15, backBtn, checkoutBtn); actions.setAlignment(Pos.CENTER);
            grid.add(actions, 0, row + 1);
        }
        ScrollPane scroll = new ScrollPane(grid); scroll.setFitToWidth(true);
        return scroll;
    }

    // ============================================
    // 4. CHECKOUT SCREEN
    // ============================================

    private ScrollPane createPaymentScreen() {
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.TOP_CENTER); layout.setPadding(new Insets(30));

        Label title = new Label("Checkout"); title.getStyleClass().add("section-title");
        double totalAmount = calculateTotal();
        Label totalLbl = new Label("Total to Pay: $" + totalAmount);
        totalLbl.getStyleClass().add("total-price-text");

        VBox shippingBox = new VBox(10);
        shippingBox.getStyleClass().add("white-box");
        Label shipTitle = new Label("📍 Shipping Address"); shipTitle.getStyleClass().add("field-label");
        TextField addressField = new TextField(); addressField.setPromptText("Enter your full address");
        TextField phoneField = new TextField(); phoneField.setPromptText("Contact Phone Number");
        shippingBox.getChildren().addAll(shipTitle, addressField, phoneField);

        VBox paymentBox = new VBox(10);
        paymentBox.getStyleClass().add("white-box");
        Label payTitle = new Label("💳 Payment Method"); payTitle.getStyleClass().add("field-label");
        RadioButton cardRb = new RadioButton("Credit Card"); RadioButton cashRb = new RadioButton("Cash on Delivery");
        ToggleGroup group = new ToggleGroup(); cardRb.setToggleGroup(group); cashRb.setToggleGroup(group); cardRb.setSelected(true);

        GridPane cardGrid = new GridPane(); cardGrid.setHgap(10); cardGrid.setVgap(10);
        TextField cardNum = new TextField(); cardNum.setPromptText("0000 0000 0000 0000");
        TextField cardName = new TextField(); cardName.setPromptText("Cardholder Name");
        TextField cardExp = new TextField(); cardExp.setPromptText("MM/YY"); cardExp.setPrefWidth(80);
        TextField cardCvv = new TextField(); cardCvv.setPromptText("CVV"); cardCvv.setPrefWidth(80);
        cardGrid.add(new Label("Card Number:"), 0, 0); cardGrid.add(cardNum, 1, 0);
        cardGrid.add(new Label("Holder Name:"), 0, 1); cardGrid.add(cardName, 1, 1);
        HBox secInfo = new HBox(10, cardExp, cardCvv); cardGrid.add(new Label("Exp/CVV:"), 0, 2); cardGrid.add(secInfo, 1, 2);

        cashRb.setOnAction(e -> cardGrid.setDisable(true));
        cardRb.setOnAction(e -> cardGrid.setDisable(false));
        paymentBox.getChildren().addAll(payTitle, cardRb, cashRb, new Separator(), cardGrid);

        Button payBtn = new Button("Confirm Payment ($" + totalAmount + ")");
        payBtn.getStyleClass().add("btn-success");
        payBtn.setPrefWidth(250);

        payBtn.setOnAction(e -> {
            if(addressField.getText().trim().isEmpty() || phoneField.getText().trim().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Missing Info", "Please enter your shipping address and phone."); return;
            }
            if (cardRb.isSelected()) {
                if (!cardNum.getText().matches("\\d{16}")) { showAlert(Alert.AlertType.ERROR, "Invalid Card", "Card number must be 16 digits."); return; }
                if (!cardCvv.getText().matches("\\d{3}")) { showAlert(Alert.AlertType.ERROR, "Invalid CVV", "CVV must be 3 digits."); return; }
                if (cardExp.getText().isEmpty()) { showAlert(Alert.AlertType.ERROR, "Invalid Date", "Please enter expiry date."); return; }
            }
            showAlert(Alert.AlertType.INFORMATION, "Payment Successful", "Order Placed Successfully!\nShipping to: " + addressField.getText());
            myCart.clear();
            mainLayout.setCenter(createCategoriesPane());
        });

        Button cancelBtn = new Button("Cancel");
        cancelBtn.getStyleClass().add("btn-danger");
        cancelBtn.setOnAction(e -> mainLayout.setCenter(createCartScreen()));

        HBox actions = new HBox(20, payBtn, cancelBtn); actions.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(title, totalLbl, shippingBox, paymentBox, actions);
        ScrollPane scroll = new ScrollPane(layout); scroll.setFitToWidth(true);
        return scroll;
    }

    // ============================================
    // 5. PROFILE SECTION (FIXED & REFACTORED)
    // ============================================

    private ScrollPane ProfileSection() {
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.TOP_CENTER); layout.setPadding(new Insets(30));

        Label title = new Label("My Profile"); title.getStyleClass().add("section-title");

        ImageView profileImg = new ImageView(loadImage("https://cdn-icons-png.flaticon.com/512/3135/3135715.png"));
        profileImg.setFitWidth(100); profileImg.setPreserveRatio(true);
        profileImg.getStyleClass().add("profile-image-view");
        Circle clip = new Circle(50, 50, 50); profileImg.setClip(clip);

        VBox formBox = new VBox(15);
        formBox.getStyleClass().add("profile-form-box");
        formBox.setMaxWidth(400);

        Label nameLbl = new Label("Username:"); nameLbl.getStyleClass().add("field-label");
        TextField nameField = new TextField(currentUser);
        nameField.setEditable(false);
        nameField.getStyleClass().add("profile-field");

        Label passLbl = new Label("Enter New Password:"); passLbl.getStyleClass().add("field-label");

        // Fix: Empty TextField (no space)
        TextField newPassField = new TextField();
        newPassField.setPromptText("Enter new password");
        newPassField.setEditable(false);
        newPassField.getStyleClass().add("profile-field");

        formBox.getChildren().addAll(nameLbl, nameField, passLbl, newPassField);

        Button editBtn = new Button("Edit Profile");
        editBtn.getStyleClass().add("primary-btn");
        editBtn.setPrefWidth(200);

        editBtn.setOnAction(e -> {
            boolean isEditable = nameField.isEditable();
            if (!isEditable) {
                // Enable Edit
                nameField.setEditable(true);
                newPassField.setEditable(true);
                nameField.getStyleClass().add("profile-field-editable");
                newPassField.getStyleClass().add("profile-field-editable");
                editBtn.setText("Save Changes");
                editBtn.getStyleClass().remove("primary-btn");
                editBtn.getStyleClass().add("btn-success");
            } else {
                // Save Logic
                String newPass = newPassField.getText().trim();

                if(newPass.isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Password cannot be empty"); return;
                }

                try (Connection con = DBConnection.getConnection()) {
                    String sql = "UPDATE users SET password = ? WHERE name = ?";
                    PreparedStatement ps = con.prepareStatement(sql);
                    ps.setString(1, newPass);
                    ps.setString(2, currentUser);
                    int updatedRows = ps.executeUpdate();
                    ps.close();

                    if (updatedRows > 0) {
                        showAlert(Alert.AlertType.INFORMATION, "Saved", "Profile updated successfully!");
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Error", "Failed to update profile.");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    showAlert(Alert.AlertType.ERROR, "Error", "Database error occurred!");
                }

                // Disable Edit
                nameField.setEditable(false);
                newPassField.setEditable(false);
                nameField.getStyleClass().remove("profile-field-editable");
                newPassField.getStyleClass().remove("profile-field-editable");
                editBtn.setText("Edit Profile");
                editBtn.getStyleClass().remove("btn-success");
                editBtn.getStyleClass().add("primary-btn");
            }
        });

        Button deleteBtn = new Button("Delete Account");
        deleteBtn.getStyleClass().add("btn-danger");
        deleteBtn.setPrefWidth(200);
        deleteBtn.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Account");
            alert.setHeaderText("Are you sure?");
            if (alert.showAndWait().get() == ButtonType.OK) {
                try (Connection con = DBConnection.getConnection()) {
                    String sql = "DELETE FROM users WHERE name = ?";
                    PreparedStatement ps = con.prepareStatement(sql);
                    ps.setString(1, currentUser);
                    int deletedRows = ps.executeUpdate();

                    if (deletedRows > 0) {
                        showAlert(Alert.AlertType.INFORMATION, "Deleted", "Account deleted successfully!");
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete account.");
                    }
                    ps.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    showAlert(Alert.AlertType.ERROR, "Error", "Database error occurred!");
                }
                myCart.clear();
                currentUser = null;
                mainLayout.setCenter(createLoginPane());
            }
        });

        Button backBtn = new Button("Back");
        backBtn.getStyleClass().addAll("primary-btn", "btn-secondary");
        backBtn.setPrefWidth(200);
        backBtn.setOnAction(e -> mainLayout.setCenter(createCategoriesPane()));

        layout.getChildren().addAll(title, profileImg, formBox, editBtn, deleteBtn, backBtn);
        ScrollPane scroll = new ScrollPane(layout); scroll.setFitToWidth(true);
        return scroll;
    }

    // ============================================
    // 6. SECTIONS & HELPERS
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
        grid.setPadding(new Insets(20)); grid.setHgap(30); grid.setVgap(45); grid.setAlignment(Pos.CENTER);
        Label title = new Label(titleStr); title.getStyleClass().add("section-title");
        grid.add(title, 0, 0, 3, 1);
        int col = 0; int row = 1;
        for (String[] prod : productsData) {
            grid.add(createCard(prod[0], prod[1], prod[2]), col, row);
            col++; if (col == 3) { col = 0; row++; }
        }
        Button backBtn = new Button("Back");
        backBtn.getStyleClass().addAll("primary-btn", "btn-secondary");
        backBtn.setOnAction(e -> mainLayout.setCenter(createCategoriesPane()));
        grid.add(backBtn, 1, row + 1);
        ScrollPane scroll = new ScrollPane(grid); scroll.setFitToWidth(true);
        return scroll;
    }

    private VBox createCard(String name, String price, String imagePath) {
        VBox card = new VBox(10); card.setAlignment(Pos.CENTER); card.getStyleClass().add("product-card");
        ImageView imageView = new ImageView(loadImage(imagePath));
        imageView.setFitWidth(120); imageView.setPreserveRatio(true);
        Label productName = new Label(name); productName.getStyleClass().add("product-name");
        Label productPrice = new Label(price); productPrice.getStyleClass().add("product-price");
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
        popupStage.setMinWidth(300);

        VBox layout = new VBox(15);
        layout.setAlignment(Pos.CENTER); layout.setPadding(new Insets(20)); layout.setStyle("-fx-background-color: white;");
        ImageView img = new ImageView(loadImage(imagePath));
        img.setFitWidth(150); img.setPreserveRatio(true);
        Label nameLbl = new Label(name); nameLbl.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        Label priceLbl = new Label(price); priceLbl.setStyle("-fx-font-size: 18px; -fx-text-fill: green;");
        Label sizeLbl = new Label("Select Size:");
        ComboBox<String> sizeBox = new ComboBox<>();
        sizeBox.getItems().addAll("Small", "Medium", "Large", "X-Large");
        sizeBox.setValue("Medium");

        Button confirmBtn = new Button("Confirm & Add to Cart");
        confirmBtn.getStyleClass().add("btn-success");
        confirmBtn.setOnAction(e -> {
            myCart.add(new Order(name, price, sizeBox.getValue()));
            showAlert(Alert.AlertType.INFORMATION, "Success", "Added to Cart!");
            popupStage.close();
        });

        layout.getChildren().addAll(img, nameLbl, priceLbl, sizeLbl, sizeBox, confirmBtn);
        Scene scene = new Scene(layout);
        try { scene.getStylesheets().add(getClass().getResource("mickey.css").toExternalForm()); } catch (Exception ex) {}
        popupStage.setScene(scene);
        popupStage.showAndWait();
    }

    private double calculateTotal() {
        double total = 0.0;
        for (Order order : myCart) {
            try { total += Double.parseDouble(order.price.replace("$", "").trim()); } catch (Exception e) {}
        }
        return total;
    }

    private ImageView getLogoView() {
        ImageView logoView = new ImageView();
        try { logoView.setImage(new Image(getClass().getResourceAsStream("mickey_store.jpg"))); logoView.setFitWidth(150); logoView.setPreserveRatio(true); } catch (Exception e) {}
        return logoView;
    }

    private Image loadImage(String path) {
        try { return new Image(getClass().getResource(path).toExternalForm()); } catch (Exception e) { return new Image("https://via.placeholder.com/150"); }
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