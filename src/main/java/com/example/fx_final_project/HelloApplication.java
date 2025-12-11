package com.example.fx_final_project;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {

    // Main layout to hold Top (Logo) and Center (Login/Signup)
    private BorderPane mainLayout;

    @Override
    public void start(Stage stage) throws IOException {

        mainLayout = new BorderPane();

        // 1. Get Logo
        ImageView logo = getLogoView();

        // 2. Create Slogan Label
        Label slogan = new Label("not just clothes , it's a store you could rely on");
        slogan.setStyle("-fx-font-style: italic; -fx-text-fill: #2c3e50; -fx-font-size: 14px; -fx-font-weight: bold;");

        // 3. Group Logo and Slogan in a VBox
        VBox topContainer = new VBox(5); // 5 is spacing between logo and text
        topContainer.getChildren().addAll(logo, slogan);
        topContainer.setAlignment(Pos.CENTER);
        topContainer.setPadding(new Insets(15, 0, 15, 0));

        // 4. Set the VBox as the Top of the layout
        mainLayout.setTop(topContainer);
        mainLayout.setCenter(createLoginPane());

        // 5. Setup Scene and CSS
        Scene scene = new Scene(mainLayout, 600, 450);
        try {
            scene.getStylesheets().add(getClass().getResource("mickey.css").toExternalForm());
            scene.getStylesheets().add(getClass().getResource("mina.css").toExternalForm());
        } catch (Exception e) {
            System.out.println("CSS file not found");
        }

        stage.setTitle("Mickey Store");
        stage.setScene(scene);
        stage.show();
    }

    // --- Create Login Screen ---
    private GridPane createLoginPane() {
        GridPane root = new GridPane();
        root.setAlignment(Pos.CENTER);
        root.setHgap(10);
        root.setVgap(10);
        root.setPadding(new Insets(20));

        // UI Controls
        Label nameLabel = new Label("Name");
        TextField nameField = new TextField();
        nameField.setPromptText("Enter your name");

        Label passLabel = new Label("Password: ");
        PasswordField passField = new PasswordField();
        passField.setPromptText("Enter your password");

        // Sign In Button Action
        Button signInBtn = new Button("Sign In");

        signInBtn.setOnAction(e -> {
            // Check username and password
            if (nameField.getText().equals("mickey") && passField.getText().equals("123")) {
                mainLayout.setCenter(createCategoriesPane());
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Wrong username or password");
            }
        });

        // Switch to Signup Screen
        Button goToSignUpBtn = new Button("Sign Up");
        goToSignUpBtn.setOnAction(e -> {
            mainLayout.setCenter(createSignupPane());
        });

        // Add items to Grid
        root.add(nameLabel, 0, 0);
        root.add(nameField, 1, 0);
        root.add(passLabel, 0, 1);
        root.add(passField, 1, 1);
        root.add(signInBtn, 0, 2);
        root.add(goToSignUpBtn, 1, 2);

        return root;
    }

    // --- Create Signup Screen ---
    private GridPane createSignupPane() {
        GridPane signupRoot = new GridPane();
        signupRoot.setPadding(new Insets(20));
        signupRoot.setAlignment(Pos.CENTER);
        signupRoot.setVgap(10);
        signupRoot.setHgap(10);

        Label nameLabel = new Label("Name:");
        TextField nameField = new TextField();
        nameField.setPromptText("Enter your name");

        Label numberLabel = new Label("Phone Number:");
        TextField numberField = new TextField();
        numberField.setPromptText("Enter your Phone Number");

        Label passLabel = new Label("Password:");
        PasswordField passField = new PasswordField();
        passField.setPromptText("Enter your password");

        Label repassLabel = new Label("Re-Password:");
        PasswordField repassField = new PasswordField();
        repassField.setPromptText("Enter your password again");

        // Radio Buttons
        RadioButton maleRb = new RadioButton("Male");
        RadioButton femaleRb = new RadioButton("Female");
        ToggleGroup genderGroup = new ToggleGroup();
        maleRb.setToggleGroup(genderGroup);
        femaleRb.setToggleGroup(genderGroup);
        maleRb.setSelected(true);

        HBox genderBox = new HBox(15, maleRb, femaleRb);

        Button registerBtn = new Button("Register");
        registerBtn.setOnAction(e -> {
            if (nameField.getText().isEmpty() || passField.getText().isEmpty() || numberField.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Form Error", "Please fill all fields");
                return;
            }
            if (passField.getText().equals(repassField.getText())) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Account created for " + nameField.getText());
                mainLayout.setCenter(createLoginPane());
            } else {
                showAlert(Alert.AlertType.ERROR, "Password Error", "Passwords do not match!");
            }
        });

        Button backBtn = new Button("Back to Login");
        backBtn.setOnAction(e -> mainLayout.setCenter(createLoginPane()));

        signupRoot.add(nameLabel, 0, 0);
        signupRoot.add(nameField, 1, 0);
        signupRoot.add(numberLabel, 0, 1);
        signupRoot.add(numberField, 1, 1);
        signupRoot.add(passLabel, 0, 2);
        signupRoot.add(passField, 1, 2);
        signupRoot.add(repassLabel, 0, 3);
        signupRoot.add(repassField, 1, 3);
        signupRoot.add(genderBox, 1, 4);

        HBox buttonsBox = new HBox(10, registerBtn, backBtn);
        buttonsBox.setAlignment(Pos.CENTER_RIGHT);
        signupRoot.add(buttonsBox, 1, 5);

        return signupRoot;
    }

    // --- Create Categories Home Screen ---
    private GridPane createCategoriesPane() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(20);
        grid.setVgap(20);
        grid.setPadding(new Insets(20));

        Label welcomeLabel = new Label("Choose a Category:");
        welcomeLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Button gentsBtn = new Button("Gents");
        gentsBtn.setPrefSize(100, 50);
        gentsBtn.setOnAction(e -> mainLayout.setCenter(GentsSection()));

        Button ladiesBtn = new Button("Ladies");
        ladiesBtn.setPrefSize(100, 50);
        ladiesBtn.setOnAction(e -> mainLayout.setCenter(LadiesSection()));

        Button kidsBtn = new Button("Kids");
        kidsBtn.setPrefSize(100, 50);
        kidsBtn.setOnAction(e -> mainLayout.setCenter(KidsSection()));

        Button logoutBtn = new Button("Logout");
        logoutBtn.setStyle("-fx-background-color: #702963; -fx-text-fill: white;");
        logoutBtn.setOnAction(e -> mainLayout.setCenter(createLoginPane()));

        grid.add(welcomeLabel, 0, 0, 3, 1);
        grid.add(gentsBtn, 0, 1);
        grid.add(ladiesBtn, 1, 1);
        grid.add(kidsBtn, 2, 1);
        grid.add(logoutBtn, 1, 3);

        return grid;
    }

    // ============================================
    // SECTIONS
    // ============================================

    // --- GENTS SECTION ---
    private ScrollPane GentsSection() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setHgap(30);
        grid.setVgap(45);
        grid.setAlignment(Pos.CENTER);

        Label title = new Label("Gents Section");
        title.getStyleClass().add("section-title");
        grid.add(title, 0, 0, 3, 1);

        // Products Row 1
        grid.add(createCard("Casual Shirt", "$25", "/com/example/fx_final_project/image/shirt.jpg"), 0, 1);
        grid.add(createCard("Jeans Pants", "$40", "/com/example/fx_final_project/image/jeans.jpg"), 1, 1);
        grid.add(createCard("Hoodie", "$35", "/com/example/fx_final_project/image/hoodie.jpg"), 2, 1);

        // Products Row 2
        grid.add(createCard("Sport clothe", "$20", "/com/example/fx_final_project/image/sportt.jpg"), 0, 2);
        grid.add(createCard("Jacket", "$20", "/com/example/fx_final_project/image/jacket.jpg"), 1, 2);
        grid.add(createCard("Sneaker", "$35", "/com/example/fx_final_project/image/formal.jpg"), 2, 2);

        Button backBtn = new Button("Back");
        backBtn.getStyleClass().add("back-btn");
        backBtn.setOnAction(e -> mainLayout.setCenter(createCategoriesPane()));
        grid.add(backBtn, 1, 3);

        ScrollPane scroll = new ScrollPane(grid);
        scroll.setFitToWidth(true);
        return scroll;
    }

    // --- LADIES SECTION ---
    private ScrollPane LadiesSection() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setHgap(30);
        grid.setVgap(45);
        grid.setAlignment(Pos.CENTER);

        Label title = new Label("Ladies Section");
        title.getStyleClass().add("section-title");
        grid.add(title, 0, 0, 3, 1);

        // Products Row 1
        grid.add(createCard("Casual jacket", "$25", "/ladie1.jpeg"), 0, 1);
        grid.add(createCard("Jeans Pants", "$40", "/ladie2.jpeg"), 1, 1);
        grid.add(createCard("Coat", "$35", "/ladie4.jpeg"), 2, 1);

        // Products Row 2
        grid.add(createCard("Red Jacket", "$50", "/ladie4.jpeg"), 0, 2);
        grid.add(createCard("Grey jacket", "$60", "/ladie5.jpeg"), 1, 2);
        grid.add(createCard("Denim Jacket", "$45", "/ladie6.jpeg"), 2, 2);

        Button backBtn = new Button("Back");
        backBtn.getStyleClass().add("back-btn");
        backBtn.setOnAction(e -> mainLayout.setCenter(createCategoriesPane()));
        grid.add(backBtn, 1, 3);

        ScrollPane scroll = new ScrollPane(grid);
        scroll.setFitToWidth(true);
        return scroll;
    }

    // --- KIDS SECTION ---
    private ScrollPane KidsSection() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setHgap(30);
        grid.setVgap(45);
        grid.setAlignment(Pos.CENTER);

        Label title = new Label("Kids Section");
        title.getStyleClass().add("section-title");
        grid.add(title, 0, 0, 3, 1);

        // Products Row 1
        grid.add(createCard("Kids Shirt", "$12", "/com/example/fx_final_project/image/shirttt.jpeg"), 0, 1);
        grid.add(createCard("Dress", "$20", "/com/example/fx_final_project/image/skerts.jpg"), 1, 1);
        grid.add(createCard("Baby Clothes", "$13", "/com/example/fx_final_project/image/baby.jpg"), 2, 1);

        // Products Row 2
        grid.add(createCard("Sport Clothes", "$10", "/com/example/fx_final_project/image/sport2.jpg"), 0, 2);
        grid.add(createCard("Jacket", "$15", "/com/example/fx_final_project/image/kjacket.jpg"), 1, 2);
        grid.add(createCard("Shoes", "$13", "/com/example/fx_final_project/image/shoes.jpg"), 2, 2);

        Button backBtn = new Button("Back");
        backBtn.getStyleClass().add("back-btn");
        backBtn.setOnAction(e -> mainLayout.setCenter(createCategoriesPane()));
        grid.add(backBtn, 1, 3);

        ScrollPane scroll = new ScrollPane(grid);
        scroll.setFitToWidth(true);
        return scroll;
    }

    // --- Helper: UNIFIED Card Creation ---
    // دي الدالة اللي بتوحد الشكل في كل الصفحات
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
        btn.getStyleClass().add("primary-btn"); // Unified CSS Class

        btn.setOnAction(e -> showAlert(Alert.AlertType.INFORMATION, "Added", name + " added to cart!"));

        card.getChildren().addAll(imageView, productName, productPrice, btn);
        return card;
    }

    // --- Helper: Load Image ---
    private ImageView getLogoView() {
        ImageView logoView = new ImageView();
        try {
            Image image = new Image(getClass().getResourceAsStream("mickey_store.jpg"));
            logoView.setImage(image);
            logoView.setFitWidth(150);
            logoView.setPreserveRatio(true);
        } catch (Exception e) {
            System.out.println("Image not found");
        }
        return logoView;
    }

    private Image loadImage(String path) {
        try {
            return new Image(getClass().getResource(path).toExternalForm());
        } catch (Exception e) {
            // Fallback if image not found
            System.err.println("Image not found: " + path);
            return new Image("https://via.placeholder.com/150");
        }
    }

    // --- Helper: Show Alert ---
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch();
    }
}