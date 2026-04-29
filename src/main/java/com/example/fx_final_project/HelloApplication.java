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

    // التعديل 1: استخدام كلاس User بدلاً من String (مبدأ Association)
    private User currentUser = null;

    // Layout & Data
    private BorderPane mainLayout;

    // التعديل 2: استخدام OrderItem بدلاً من كلاس Order الداخلي (مبدأ Aggregation)
    private final ArrayList<OrderItem> myCart = new ArrayList<>();

    // التعديل 3: مبدأ الـ Composition (OrderItem يعتمد على كائن Product)
    public static class OrderItem {
        Product product; String size;
        public OrderItem(Product p, String s) {
            this.product = p; this.size = s;
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
            // تعديل مؤقت للتجربة بدون داتابيز
            String username = nameField.getText().trim();
            String password = passField.getText().trim();

            if (!username.isEmpty() && !password.isEmpty()) {
                // بنعتبر أي مستخدم يدخل بياناته إنه سجل دخول بنجاح
                currentUser = new User(username);
                mainLayout.setCenter(createCategoriesPane());
                showAlert(Alert.AlertType.INFORMATION, "Demo Mode", "Logged in as: " + username);
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Please enter any name and password");
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
    // 3. SECTIONS (التعديل 5: استخدام مبدأ الـ Polymorphism)
    // ============================================

    private ScrollPane GentsSection() {
        ArrayList<Product> products = new ArrayList<>();
        products.add(new GentsProduct("Casual Shirt", 25.0, "/com/example/fx_final_project/image/shirt.jpg"));
        products.add(new GentsProduct("Jeans Pants", 40.0, "/com/example/fx_final_project/image/jeans.jpg"));
        products.add(new GentsProduct("Hoodie", 35.0, "/com/example/fx_final_project/image/hoodie.jpg"));
        products.add(new GentsProduct("Sport clothe", 20.0, "/com/example/fx_final_project/image/sportt.jpg"));
        products.add(new GentsProduct("Jacket", 20.0, "/com/example/fx_final_project/image/jacket.jpg"));
        products.add(new GentsProduct("Sneaker", 35.0, "/com/example/fx_final_project/image/formal.jpg"));
        return createSectionGrid("Gents Section", products);
    }

    private ScrollPane LadiesSection() {
        ArrayList<Product> products = new ArrayList<>();
        products.add(new LadiesProduct("Casual jacket", 25.0, "/ladie1.jpeg"));
        products.add(new LadiesProduct("Jeans Pants", 40.0, "/ladie2.jpeg"));
        products.add(new LadiesProduct("Coat", 35.0, "/ladie4.jpeg"));
        products.add(new LadiesProduct("Red Jacket", 50.0, "/ladie4.jpeg"));
        products.add(new LadiesProduct("Grey jacket", 60.0, "/ladie5.jpeg"));
        products.add(new LadiesProduct("Denim Jacket", 45.0, "/ladie6.jpeg"));
        return createSectionGrid("Ladies Section", products);
    }

    private ScrollPane KidsSection() {
        ArrayList<Product> products = new ArrayList<>();
        products.add(new KidsProduct("Kids Shirt", 12.0, "/com/example/fx_final_project/image/shirttt.jpeg"));
        products.add(new KidsProduct("Dress", 20.0, "/com/example/fx_final_project/image/skerts.jpg"));
        products.add(new KidsProduct("Baby Clothes", 13.0, "/com/example/fx_final_project/image/baby.jpg"));
        products.add(new KidsProduct("Sport Clothes", 10.0, "/com/example/fx_final_project/image/sport2.jpg"));
        products.add(new KidsProduct("Jacket", 15.0, "/com/example/fx_final_project/image/kjacket.jpg"));
        products.add(new KidsProduct("Shoes", 13.0, "/com/example/fx_final_project/image/shoes.jpg"));
        return createSectionGrid("Kids Section", products);
    }

    private ScrollPane createSectionGrid(String titleStr, ArrayList<Product> products) {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20)); grid.setHgap(30); grid.setVgap(45); grid.setAlignment(Pos.CENTER);
        Label title = new Label(titleStr); title.getStyleClass().add("section-title");
        grid.add(title, 0, 0, 3, 1);
        int col = 0; int row = 1;
        for (Product p : products) {
            grid.add(createCard(p), col, row);
            col++; if (col == 3) { col = 0; row++; }
        }
        Button backBtn = new Button("Back");
        backBtn.getStyleClass().addAll("primary-btn", "btn-secondary");
        backBtn.setOnAction(e -> mainLayout.setCenter(createCategoriesPane()));
        grid.add(backBtn, 1, row + 1);
        ScrollPane scroll = new ScrollPane(grid); scroll.setFitToWidth(true);
        return scroll;
    }

    private VBox createCard(Product p) {
        VBox card = new VBox(10); card.setAlignment(Pos.CENTER); card.getStyleClass().add("product-card");
        ImageView imageView = new ImageView(loadImage(p.getImagePath()));
        imageView.setFitWidth(120); imageView.setPreserveRatio(true);
        Label productName = new Label(p.getName()); productName.getStyleClass().add("product-name");
        Label productPrice = new Label("$" + p.getPrice()); productPrice.getStyleClass().add("product-price");
        Button btn = new Button("Buy");
        btn.getStyleClass().add("primary-btn");
        btn.setOnAction(e -> showProductDetails(p));
        card.getChildren().addAll(imageView, productName, productPrice, btn);
        return card;
    }

    private void showProductDetails(Product p) {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Product Details");
        popupStage.setMinWidth(300);

        VBox layout = new VBox(15);
        layout.setAlignment(Pos.CENTER); layout.setPadding(new Insets(20)); layout.setStyle("-fx-background-color: white;");
        ImageView img = new ImageView(loadImage(p.getImagePath()));
        img.setFitWidth(150); img.setPreserveRatio(true);
        Label nameLbl = new Label(p.getName()); nameLbl.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        Label priceLbl = new Label("$" + p.getPrice()); priceLbl.setStyle("-fx-font-size: 18px; -fx-text-fill: green;");
        Label sizeLbl = new Label("Select Size:");
        ComboBox<String> sizeBox = new ComboBox<>();
        sizeBox.getItems().addAll("Small", "Medium", "Large", "X-Large");
        sizeBox.setValue("Medium");

        Button confirmBtn = new Button("Confirm & Add to Cart");
        confirmBtn.getStyleClass().add("btn-success");
        confirmBtn.setOnAction(e -> {
            myCart.add(new OrderItem(p, sizeBox.getValue()));
            showAlert(Alert.AlertType.INFORMATION, "Success", "Added to Cart!");
            popupStage.close();
        });

        layout.getChildren().addAll(img, nameLbl, priceLbl, sizeLbl, sizeBox, confirmBtn);
        Scene scene = new Scene(layout);
        try { scene.getStylesheets().add(getClass().getResource("mickey.css").toExternalForm()); } catch (Exception ex) {}
        popupStage.setScene(scene);
        popupStage.showAndWait();
    }

    // ============================================
    // 4. CART & CHECKOUT
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
            for (OrderItem order : myCart) {
                HBox itemRow = new HBox(20);
                itemRow.setAlignment(Pos.CENTER_LEFT);
                itemRow.getStyleClass().add("white-box");
                itemRow.setPrefWidth(550);

                Label nameLbl = new Label(order.product.getName()); nameLbl.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;"); nameLbl.setPrefWidth(120);
                Label sizeLbl = new Label("Size: " + order.size); sizeLbl.setPrefWidth(100);
                Label priceLbl = new Label("$" + order.product.getPrice()); priceLbl.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");

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
        TextField cardExp = new TextField(); cardExp.setPromptText("MM/YY");
        TextField cardCvv = new TextField(); cardCvv.setPromptText("CVV");
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
            // التعديل 6: استخدام الـ Exception Handling
            try {
                if(addressField.getText().trim().isEmpty() || phoneField.getText().trim().isEmpty()) {
                    throw new PaymentException("Shipping address and phone are required!");
                }
                if (cardRb.isSelected()) {
                    if (!cardNum.getText().matches("\\d{16}")) throw new PaymentException("Card number must be 16 digits.");
                    if (!cardCvv.getText().matches("\\d{3}")) throw new PaymentException("CVV must be 3 digits.");
                }
                showAlert(Alert.AlertType.INFORMATION, "Payment Successful", "Order Placed Successfully!");
                myCart.clear();
                mainLayout.setCenter(createCategoriesPane());
            } catch (PaymentException ex) {
                showAlert(Alert.AlertType.ERROR, "Checkout Error", ex.getMessage());
            }
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
    // 5. PROFILE SECTION
    // ============================================

    private ScrollPane ProfileSection() {
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.TOP_CENTER); layout.setPadding(new Insets(30));

        Label title = new Label("My Profile"); title.getStyleClass().add("section-title");

        ImageView profileImg = new ImageView(loadImage("https://cdn-icons-png.flaticon.com/512/3135/3135715.png"));
        profileImg.setFitWidth(100); profileImg.setPreserveRatio(true);
        Circle clip = new Circle(50, 50, 50); profileImg.setClip(clip);

        VBox formBox = new VBox(15);
        formBox.getStyleClass().add("profile-form-box");
        formBox.setMaxWidth(400);

        Label nameLbl = new Label("Username:");
        TextField nameField = new TextField(currentUser != null ? currentUser.getUsername() : "");
        nameField.setEditable(false);

        formBox.getChildren().addAll(nameLbl, nameField);

        Button backBtn = new Button("Back");
        backBtn.getStyleClass().addAll("primary-btn", "btn-secondary");
        backBtn.setOnAction(e -> mainLayout.setCenter(createCategoriesPane()));

        layout.getChildren().addAll(title, profileImg, formBox, backBtn);
        return new ScrollPane(layout);
    }

    // ============================================
    // 6. HELPERS
    // ============================================

    private double calculateTotal() {
        double total = 0.0;
        for (OrderItem order : myCart) {
            total += order.product.getPrice();
        }
        return total;
    }

    private ImageView getLogoView() {
        ImageView logoView = new ImageView();
        try { logoView.setImage(new Image(getClass().getResourceAsStream("mickey_store.jpg"))); logoView.setFitWidth(150); logoView.setPreserveRatio(true); } catch (Exception e) {}
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

    public static void main(String[] args) { launch(); }
}