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
import javafx.scene.layout.VBox;

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
        gentsBtn.setOnAction(e -> mainLayout.setCenter(GentsSection()));

        // 2. Ladies Button
        Button ladiesBtn = new Button("Ladies");
        ladiesBtn.setPrefSize(100, 50);
        ladiesBtn.setOnAction(e -> {
            mainLayout.setCenter(getView()); // calls your Ladies StorePage
        });

        // 3. Kids Button
        Button kidsBtn = new Button("Kids");
        kidsBtn.setPrefSize(100, 50);
        kidsBtn.setOnAction(e -> mainLayout.setCenter(Kids()));

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
    private ScrollPane GentsSection() {

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setHgap(30);
        grid.setVgap(45);
        grid.setAlignment(Pos.CENTER);

        Label title = new Label("Gents Section");
        title.getStyleClass().add("section-title");
        grid.add(title, 0, 0, 3, 1);

        // ============ PRODUCT 1 ============
        VBox card1 = new VBox(10);
        VBox card2 = new VBox(10);
        VBox card3 = new VBox(10);
        VBox card4 = new VBox(10);
        VBox card5 = new VBox(10);
        VBox card6 = new VBox(10);



        //***********************************************************
        card1.setAlignment(Pos.CENTER);
        card1.getStyleClass().add("product-card");

        card2.setAlignment(Pos.CENTER);
        card2.getStyleClass().add("product-card");

        card3.setAlignment(Pos.CENTER);
        card3.getStyleClass().add("product-card");

        card4.setAlignment(Pos.CENTER);
        card4.getStyleClass().add("product-card");

        card5.setAlignment(Pos.CENTER);
        card5.getStyleClass().add("product-card");

        card6.setAlignment(Pos.CENTER);
        card6.getStyleClass().add("product-card");
        //***********************************************************

        ImageView img1 = new ImageView(new Image(getClass().getResourceAsStream("/com/example/fx_final_project/image/shirt.jpg")));
        img1.setFitWidth(120);
        img1.setPreserveRatio(true);

        Label name1 = new Label("Casual Shirt");
        name1.getStyleClass().add("product-name");

        Label price1 = new Label("$25");
        price1.getStyleClass().add("product-price");

        Button add1 = new Button("Add to Cart");
        add1.getStyleClass().add("primary-btn");
        add1.setOnAction(e -> showAlert(Alert.AlertType.INFORMATION, "Added","Casual Shirt added to cart!"));

        card1.getChildren().addAll(img1, name1, price1 , add1);

        // ============ PRODUCT 2 ============


        ImageView img2 = new ImageView(new Image(getClass().getResourceAsStream("/com/example/fx_final_project/image/jeans.jpg")));
        img2.setFitWidth(120);
        img2.setPreserveRatio(true);

        Label name2 = new Label("Jeans Pants");
        name2.getStyleClass().add("product-name");

        Label price2 = new Label("$40");
        price2.getStyleClass().add("product-price");



        Button add2 = new Button("Add to Cart");
        add2.getStyleClass().add("primary-btn");
        add2.setOnAction(e -> showAlert(Alert.AlertType.INFORMATION, "Added","Jeans Pants added to cart!"));

        card2.getChildren().addAll(img2, name2, price2, add2);

        // ============ PRODUCT 3 ============


        ImageView img3 = new ImageView(new Image(getClass().getResourceAsStream("/com/example/fx_final_project/image/hoodie.jpg")));
        img3.setFitWidth(120);
        img3.setPreserveRatio(true);

        Label name3 = new Label("Hoodie");
        name3.getStyleClass().add("product-name");

        Label price3 = new Label("$35");
        price3.getStyleClass().add("product-price");



        Button add3 = new Button("Add to Cart");
        add3.getStyleClass().add("primary-btn");
        add3.setOnAction(e -> showAlert(Alert.AlertType.INFORMATION, "Added","Hoodie added to cart!"));

        card3.getChildren().addAll(img3, name3, price3, add3);

        //  ******************* product 3 **************************

        ImageView img4 = new ImageView(new Image(getClass().getResourceAsStream("/com/example/fx_final_project/image/sportt.jpg")));
        img4.setFitWidth(120);
        img4.setPreserveRatio(true);

        Label name4 = new Label("Sport clothe");
        name1.getStyleClass().add("product-name");

        Label price4 = new Label("$20");
        price1.getStyleClass().add("product-price");


        Button add4 = new Button("Add to Cart");
        add1.getStyleClass().add("primary-btn");
        add1.setOnAction(e -> showAlert(Alert.AlertType.INFORMATION, "Added","Casual Shirt added to cart!"));

        card4.getChildren().addAll(img4, name4, price4,  add4);

        //<><><>><><><><><>><><><><> P5<><><><><><><><><><><><><><><><><><><><><><

        ImageView img5 = new ImageView(new Image(getClass().getResourceAsStream("/com/example/fx_final_project/image/jacket.jpg")));
        img5.setFitWidth(120);
        img5.setPreserveRatio(true);

        Label name5 = new Label("jacket");
        name1.getStyleClass().add("product-name");

        Label price5 = new Label("$20");
        price1.getStyleClass().add("product-price");


        Button add5 = new Button("Add to Cart");
        add1.getStyleClass().add("primary-btn");
        add1.setOnAction(e -> showAlert(Alert.AlertType.INFORMATION, "Added","Casual Shirt added to cart!"));

        card5.getChildren().addAll(img5, name5, price5, add5);

//<><><><><><><><><<><><><><><>P6><><><><><><><><><><><><><><

        ImageView img6 = new ImageView(new Image(getClass().getResourceAsStream("/com/example/fx_final_project/image/formal.jpg")));
        img6.setFitWidth(120);
        img6.setPreserveRatio(true);

        Label name6 = new Label("sneaker");
        name1.getStyleClass().add("product-name");

        Label price6 = new Label("$35");
        price1.getStyleClass().add("product-price");




        Button add6 = new Button("Add to Cart");
        add1.getStyleClass().add("primary-btn");
        add1.setOnAction(e -> showAlert(Alert.AlertType.INFORMATION, "Added","Casual Shirt added to cart!"));

        card6.getChildren().addAll(img6, name6, price6,  add6);

//><><><><><><><><><><><><><><><><><><><<><><><><><><><><><><><><><><><><><><
        //  Grid
        grid.add(card1, 0, 1);
        grid.add(card2, 1, 1);
        grid.add(card3, 2, 1);
        grid.add(card4, 0, 2);
        grid.add(card5, 1, 2);
        grid.add(card6, 2, 2);



        // Back Button
        Button backBtn = new Button("Back");
        backBtn.getStyleClass().add("back-btn");
        backBtn.setOnAction(e -> mainLayout.setCenter(createCategoriesPane()));
        grid.add(backBtn, 1, 3);

        ScrollPane scroll = new ScrollPane(grid);
        scroll.setFitToWidth(true);

        return scroll;
    }


    //ladies section
    public BorderPane getView() {

        BorderPane root = new BorderPane();


        //header
        VBox header = new VBox();
        header.getStyleClass().add("header");


        //logo
        ImageView logo = new ImageView(loadImage("/mickey_store.jpg"));
        logo.setFitWidth(120);
        logo.setPreserveRatio(true);


        //content
        VBox content = new VBox();
        content.setAlignment(Pos.TOP_CENTER);
        content.setSpacing(30);
        content.setPadding(new Insets(30));



        //title
        Label title = new Label("Ladies Section");
        title.getStyleClass().add("title");

        GridPane grid = new GridPane();
        grid.setHgap(30);
        grid.setVgap(30);
        grid.setAlignment(Pos.CENTER);

        // Cards
        grid.add(createCard("Casual jacket", 25, "/ladie1.jpeg"), 0, 0);
        grid.add(createCard("Jeans Pants", 40, "/ladie2.jpeg"), 1, 0);
        grid.add(createCard("coot", 35, "/ladie4.jpeg"), 2, 0);

        grid.add(createCard("red Jacket", 50, "/ladie4.jpeg"), 0, 1);
        grid.add(createCard("grey jacket", 60, "/ladie5.jpeg"), 1, 1);
        grid.add(createCard("Denim Jacket", 45, "/ladie6.jpeg"), 2, 1);

        content.getChildren().addAll(title, grid);

        // علشان يبقي بيسكرول
        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setPannable(true);
        scrollPane.setStyle("-fx-background-color:transparent;");

        root.setTop(header);
        root.setCenter(scrollPane);


        root.getStylesheets().add(getClass().getResource("/mina.css").toExternalForm());

        return root;
    }

    // card
    private VBox createCard(String name, double price, String imagePath) {
        VBox card = new VBox();
        card.getStyleClass().add("card");

        ImageView imageView = new ImageView(loadImage(imagePath));
        imageView.setFitWidth(200);
        imageView.fitHeightProperty();
        imageView.setPreserveRatio(true);

        Label productName = new Label(name);
        productName.getStyleClass().add("product-name");

        Label productPrice = new Label("$" + price);
        productPrice.getStyleClass().add("product-price");

        Button btn = new Button("Add to Cart");
        btn.getStyleClass().add("add-to-cart-btn");

        card.getChildren().addAll(imageView, productName, productPrice, btn);

        return card;
    }
    private ScrollPane Kids() {

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setHgap(30);
        grid.setVgap(45);
        grid.setAlignment(Pos.CENTER);

        Label title = new Label("Kids Section");
        title.getStyleClass().add("section-title");
        grid.add(title, 0, 0, 3, 1);

        // ============ PRODUCT 1 ============
        VBox card1 = new VBox(10);
        VBox card2 = new VBox(10);
        VBox card3 = new VBox(10);
        VBox card4 = new VBox(10);
        VBox card5 = new VBox(10);
        VBox card6 = new VBox(10);



        //***********************************************************
        card1.setAlignment(Pos.CENTER);
        card1.getStyleClass().add("product-card");

        card2.setAlignment(Pos.CENTER);
        card2.getStyleClass().add("product-card");

        card3.setAlignment(Pos.CENTER);
        card3.getStyleClass().add("product-card");

        card4.setAlignment(Pos.CENTER);
        card4.getStyleClass().add("product-card");

        card5.setAlignment(Pos.CENTER);
        card5.getStyleClass().add("product-card");

        card6.setAlignment(Pos.CENTER);
        card6.getStyleClass().add("product-card");
        //***********************************************************

        ImageView img1 = new ImageView(new Image(getClass().getResourceAsStream("/com/example/fx_final_project/image/shirttt.jpeg")));
        img1.setFitWidth(120);
        img1.setPreserveRatio(true);

        Label name1 = new Label(" kids shirt");
        name1.getStyleClass().add("product-name");

        Label price1 = new Label("$12");
        price1.getStyleClass().add("product-price");

        Button add1 = new Button("Add to Cart");
        add1.getStyleClass().add("primary-btn");
        add1.setOnAction(e -> showAlert(Alert.AlertType.INFORMATION, "Added","Casual Shirt added to cart!"));

        card1.getChildren().addAll(img1, name1, price1 , add1);

        // ============ PRODUCT 2 ============


        ImageView img2 = new ImageView(new Image(getClass().getResourceAsStream("/com/example/fx_final_project/image/skerts.jpg")));
        img2.setFitWidth(120);
        img2.setPreserveRatio(true);

        Label name2 = new Label("Dress ");
        name2.getStyleClass().add("product-name");

        Label price2 = new Label("$20");
        price2.getStyleClass().add("product-price");



        Button add2 = new Button("Add to Cart");
        add2.getStyleClass().add("primary-btn");
        add2.setOnAction(e -> showAlert(Alert.AlertType.INFORMATION, "Added","dress added to cart!"));

        card2.getChildren().addAll(img2, name2, price2, add2);

        // ============ PRODUCT 3 ============


        ImageView img3 = new ImageView(new Image(getClass().getResourceAsStream("/com/example/fx_final_project/image/baby.jpg")));
        img3.setFitWidth(120);
        img3.setPreserveRatio(true);

        Label name3 = new Label("Baby Clothes");
        name3.getStyleClass().add("product-name");

        Label price3 = new Label("$13");
        price3.getStyleClass().add("product-price");



        Button add3 = new Button("Add to Cart");
        add3.getStyleClass().add("primary-btn");
        add3.setOnAction(e -> showAlert(Alert.AlertType.INFORMATION, "Added","Baby Clothes added to cart!"));

        card3.getChildren().addAll(img3, name3, price3, add3);

        //  ******************* product 3 **************************

        ImageView img4 = new ImageView(new Image(getClass().getResourceAsStream("/com/example/fx_final_project/image/sport2.jpg")));
        img4.setFitWidth(120);
        img4.setPreserveRatio(true);

        Label name4 = new Label("Sport Clothes");
        name1.getStyleClass().add("product-name");

        Label price4 = new Label("$10");
        price1.getStyleClass().add("product-price");


        Button add4 = new Button("Add to Cart");
        add1.getStyleClass().add("primary-btn");
        add1.setOnAction(e -> showAlert(Alert.AlertType.INFORMATION, "Added","Sport Clothes added to cart!"));

        card4.getChildren().addAll(img4, name4, price4,  add4);

        //<><><>><><><><><>><><><><> P5<><><><><><><><><><><><><><><><><><><><><><

        ImageView img5 = new ImageView(new Image(getClass().getResourceAsStream("/com/example/fx_final_project/image/kjacket.jpg")));
        img5.setFitWidth(120);
        img5.setPreserveRatio(true);

        Label name5 = new Label("jacket");
        name1.getStyleClass().add("product-name");

        Label price5 = new Label("$15");
        price1.getStyleClass().add("product-price");


        Button add5 = new Button("Add to Cart");
        add1.getStyleClass().add("primary-btn");
        add1.setOnAction(e -> showAlert(Alert.AlertType.INFORMATION, "Added","Casual Shirt added to cart!"));

        card5.getChildren().addAll(img5, name5, price5, add5);

//<><><><><><><><><<><><><><><>P6><><><><><><><><><><><><><><

        ImageView img6 = new ImageView(new Image(getClass().getResourceAsStream("/com/example/fx_final_project/image/shoes.jpg")));
        img6.setFitWidth(120);
        img6.setPreserveRatio(true);

        Label name6 = new Label("Shoes");
        name1.getStyleClass().add("product-name");

        Label price6 = new Label("$13");
        price1.getStyleClass().add("product-price");




        Button add6 = new Button("Add to Cart");
        add1.getStyleClass().add("primary-btn");
        add1.setOnAction(e -> showAlert(Alert.AlertType.INFORMATION, "Added","Shoes added to cart!"));

        card6.getChildren().addAll(img6, name6, price6,  add6);

//><><><><><><><><><><><><><><><><><><><<><><><><><><><><><><><><><><><><><><
        //  Grid
        grid.add(card1, 0, 1);
        grid.add(card2, 1, 1);
        grid.add(card3, 2, 1);
        grid.add(card4, 0, 2);
        grid.add(card5, 1, 2);
        grid.add(card6, 2, 2);



        // Back Button
        Button backBtn = new Button("Back");
        backBtn.getStyleClass().add("back-btn");
        backBtn.setOnAction(e -> mainLayout.setCenter(createCategoriesPane()));
        grid.add(backBtn, 1, 3);

        ScrollPane scroll = new ScrollPane(grid);
        scroll.setFitToWidth(true);

        return scroll;
    }

    // علشان الصوره لو مجتش او فيها error
    private Image loadImage(String path) {
        try {
            return new Image(getClass().getResource(path).toExternalForm());
        } catch (Exception e) {
            System.err.println("Image not found: " + path);
            return new Image("https://via.placeholder.com/150");
        }
    }

    public static void main(String[] args) {
        launch();
    }
}