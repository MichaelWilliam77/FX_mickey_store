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

        // 1. Add Logo to the Top
        ImageView logo = getLogoView();
        mainLayout.setTop(logo);
        BorderPane.setAlignment(logo, Pos.CENTER);
        BorderPane.setMargin(logo, new Insets(20, 0, 10, 0));

        // 2. Show Login Screen in the Center first
        mainLayout.setCenter(createLoginPane());

        // 3. Setup Scene and CSS
        Scene scene = new Scene(mainLayout, 600, 450);
        try {
            scene.getStylesheets().add(getClass().getResource("mickey.css").toExternalForm());
        } catch (Exception e) {
            System.out.println("CSS file not found");
        }

        stage.setTitle("Authentication Screen");
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
                showAlert(Alert.AlertType.INFORMATION, "Signed In", "Hello Mickey!");
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