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
        javafx.scene.layout.VBox topContainer = new javafx.scene.layout.VBox(5); // 5 is spacing between logo and text
        topContainer.getChildren().addAll(logo, slogan);
        topContainer.setAlignment(Pos.CENTER);
        topContainer.setPadding(new Insets(15, 0, 15, 0));

        // 4. Set the VBox as the Top of the layout
        mainLayout.setTop(topContainer);
        mainLayout.setCenter(createLoginPane());

        // 3. Setup Scene and CSS
        Scene scene = new Scene(mainLayout, 600, 450);
        try {
            scene.getStylesheets().add(getClass().getResource("mickey.css").toExternalForm());
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
            // هنا المفروض تتأكد من الداتابيز (حالياً هنسيبها static زي ما هي للتجربة)
            if (nameField.getText().equals("mickey") && passField.getText().equals("123")) {
                mainLayout.setCenter(createCategoriesPane());
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Wrong username or password");
            }
        });

        // Switch to Signup Screen
        Button goToSignUpBtn = new Button("Sign Up");
        goToSignUpBtn.setOnAction(e -> {
            mainLayout.setCenter(createSignupPane()); // Change Center View
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

        // UI Controls
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

        // Radio Buttons for Gender
        RadioButton maleRb = new RadioButton("Male");
        RadioButton femaleRb = new RadioButton("Female");
        ToggleGroup genderGroup = new ToggleGroup();
        maleRb.setToggleGroup(genderGroup);
        femaleRb.setToggleGroup(genderGroup);
        maleRb.setSelected(true); // Default select

        HBox genderBox = new HBox(15, maleRb, femaleRb);

        // Register Button Action
        Button registerBtn = new Button("Register");
        registerBtn.setOnAction(e -> {
            // 1. Check empty fields
            if (nameField.getText().isEmpty() || passField.getText().isEmpty() || numberField.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Form Error", "Please fill all fields");
                return;
            }

            // 2. Check password match
            if (passField.getText().equals(repassField.getText())) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Account created for " + nameField.getText());
                mainLayout.setCenter(createLoginPane()); // Go back to Login
            } else {
                showAlert(Alert.AlertType.ERROR, "Password Error", "Passwords do not match!");
            }
        });

        // Back Button
        Button backBtn = new Button("Back to Login");
        backBtn.setOnAction(e -> {
            mainLayout.setCenter(createLoginPane()); // Go back to Login
        });

        // Add items to Grid
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

        // 1. Gents Button
        Button gentsBtn = new Button("Gents");
        gentsBtn.setPrefSize(100, 50);
        gentsBtn.setOnAction(e -> mainLayout.setCenter(createProductScene("Gents")));

        // 2. Ladies Button
        Button ladiesBtn = new Button("Ladies");
        ladiesBtn.setPrefSize(100, 50);
        ladiesBtn.setOnAction(e -> mainLayout.setCenter(createProductScene("Ladies")));

        // 3. Kids Button
        Button kidsBtn = new Button("Kids");
        kidsBtn.setPrefSize(100, 50);
        kidsBtn.setOnAction(e -> mainLayout.setCenter(createProductScene("Kids")));

        // logout
        Button logoutBtn = new Button("Logout");
        logoutBtn.setStyle("-fx-background-color: #702963;");
        logoutBtn.setOnAction(e -> mainLayout.setCenter(createLoginPane()));


        grid.add(welcomeLabel, 0, 0, 3, 1);
        grid.add(gentsBtn, 0, 1);
        grid.add(ladiesBtn, 1, 1);
        grid.add(kidsBtn, 2, 1);
        grid.add(logoutBtn, 1, 3);

        return grid;
    }

    // --- Create Generic Product Screen ---
    private GridPane createProductScene(String categoryName) {
        GridPane prodGrid = new GridPane();
        prodGrid.setAlignment(Pos.CENTER);
        prodGrid.setVgap(20);

        // scene title
        Label titleLabel = new Label(categoryName + " Section");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-text-fill: #333;");

        Label infoLabel = new Label("List of " + categoryName + " products will appear here...");

        // back to categories
        Button backBtn = new Button("Back to Categories");
        backBtn.setOnAction(e -> mainLayout.setCenter(createCategoriesPane()));

        prodGrid.add(titleLabel, 0, 0);
        prodGrid.add(infoLabel, 0, 1);
        prodGrid.add(backBtn, 0, 2);

        return prodGrid;
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