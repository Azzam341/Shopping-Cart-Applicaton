package com.example.testingwithfx;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerMainPage {

    private CustomerSignInController customerSignInController;
    private Customer customer = new Customer();

    @FXML
    private GridPane gridPane;

    @FXML
    private Button checkout;

    @FXML
    private Button viewShoppingCart;

    @FXML
    private Button searchProduct;

    @FXML
    private Button viewCustomerDetails;

    @FXML
    private Button logOut;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private Button viewHistory;


    @FXML
    private ComboBox<String> searchBox;

    public void initialize() {
        productGridPane();
        scrollPane.setContent(gridPane);
        setSearchBox();
    }

    public void setCustomerSignInController(CustomerSignInController controller) {
        this.customerSignInController = controller;
    }

    public void productGridPane() {
        // Clear existing content in GridPane
        gridPane.getChildren().clear();

        // Adjust grid dimensions and spacing for the 1280x720 screen
        gridPane.setPrefWidth(1235);
        gridPane.setPrefHeight(399);
        gridPane.setHgap(20); // Horizontal spacing between columns
        gridPane.setVgap(20); // Vertical spacing between rows

        for (int i = 0; i < User.getInventory().size(); i++) {
            // Fetch product details
            String productName = User.getInventory().get(i).getProductName();
            double productPrice = User.getInventory().get(i).getProductPrice();
            String productDescription = User.getInventory().get(i).getProductDescription();
            String category = String.valueOf(User.getInventory().get(i).getCategory());
            String imagePath = User.getInventory().get(i).getImageFilepath();

            // Create UI elements for the product
            Label nameLabel = createLabel("Name: \n " + productName);
            Label priceLabel = createLabel("Price: \n Rs. " + productPrice);
            Label descriptionLabel = createLabel("Description: \n " + productDescription);
            Label categoryLabel = createLabel("Category:   " + category);
            Button addToCartButton = createButton("Add to Cart");
            Button viewDetails = createButton("View Details");
            ImageView imageView = loadProductImage(imagePath);

            // Style the buttons with rounded corners and Roboto font
            String buttonStyle = "-fx-font-family: 'Roboto'; -fx-font-size: 14px; -fx-background-color: #4CAF50; -fx-text-fill: white; "
                    + "-fx-background-radius: 25px; -fx-border-radius: 25px; -fx-padding: 10px;";
            addToCartButton.setStyle(buttonStyle);
            viewDetails.setStyle(buttonStyle);

            // Style labels with Roboto font for better readability
            nameLabel.setStyle("-fx-font-family: 'Roboto'; -fx-font-size: 16px;");
            priceLabel.setStyle("-fx-font-family: 'Roboto'; -fx-font-size: 14px;");
            descriptionLabel.setStyle("-fx-font-family: 'Roboto'; -fx-font-size: 14px;");
            categoryLabel.setStyle("-fx-font-family: 'Roboto'; -fx-font-size: 14px;");

            // Apply rounded corners to the image
            if (imageView != null) {
                Rectangle clip = new Rectangle(imageView.getFitWidth(), imageView.getFitHeight());
                clip.setArcWidth(30); // Adjust to control rounding
                clip.setArcHeight(30);
                imageView.setClip(clip);
            }

            // Attach actions to buttons
            int finalI = i;
            addToCartButton.setOnAction(event -> handleAddToCart(finalI));
            viewDetails.setOnAction(event -> showProductDetails(finalI, productDescription, productPrice, category));

            // Add UI elements to the grid
            int row = i;
            gridPane.add(imageView, 0, row);
            gridPane.add(nameLabel, 1, row);
            gridPane.add(priceLabel, 2, row);
            gridPane.add(descriptionLabel, 3, row);
            gridPane.add(categoryLabel, 4, row);
            gridPane.add(addToCartButton, 5, row);
            gridPane.add(viewDetails, 6, row);

            // Set button dimensions
            addToCartButton.setPrefWidth(135);
            addToCartButton.setPrefHeight(50);
            viewDetails.setPrefWidth(135);
            viewDetails.setPrefHeight(50);

            // Apply consistent margins with reduced button margins
            Insets defaultMargin = new Insets(15);
            Insets reducedButtonMargin = new Insets(10, 8, 10, 8); // Top, Right, Bottom, Left reduced margins for buttons

            GridPane.setMargin(imageView, defaultMargin);
            GridPane.setMargin(nameLabel, defaultMargin);
            GridPane.setMargin(priceLabel, defaultMargin);
            GridPane.setMargin(descriptionLabel, defaultMargin);
            GridPane.setMargin(categoryLabel, defaultMargin);
            GridPane.setMargin(addToCartButton, reducedButtonMargin);
            GridPane.setMargin(viewDetails, reducedButtonMargin);
        }
    }
    public void setSearchBox() {
        searchBox.setEditable(true);

        ObservableList<String> products = FXCollections.observableArrayList();
        for (int i = 0; i < User.getInventory().size(); i++) {
            products.add(User.getInventory().get(i).getProductName());
        }

        searchBox.setItems(products);
        searchBox.setVisibleRowCount(5);

        searchBox.setStyle("-fx-font-size: 16px;");
        searchBox.getEditor().textProperty().addListener((observableValue, oldValue, newValue) -> {
            ObservableList<String> filteredList = FXCollections.observableArrayList();
            for (String product : products) {
                if (product.toLowerCase().contains(newValue.toLowerCase())) {
                    filteredList.add(product);
                }
            }

            searchBox.setItems(filteredList);

            if (!newValue.isEmpty()) {
                searchBox.show();
            } else {
                searchBox.hide();
            }
        });
    }


    private Label createLabel(String text) {
        Label label = new Label(text);
        label.setWrapText(true);
        return label;
    }

    private Button createButton(String text) {
        Button button = new Button(text);
        button.setMaxWidth(Double.MAX_VALUE);
        return button;
    }

    private ImageView loadProductImage(String imagePath) {
        ImageView imageView = new ImageView();
        try {
            Image image = new Image(imagePath);
            imageView.setImage(image);
            imageView.setFitWidth(100);
            imageView.setFitHeight(100);
        } catch (Exception e) {
            System.out.println("Error loading image: " + imagePath);
            imageView.setImage(new Image("file:C:/Users/HP/IdeaProjects/TestingwithFX/src/main/resources/Not Found.png"));
        }
        return imageView;
    }

    private void handleAddToCart(int productIndex) {
        Stage quantityStage = new Stage();
        quantityStage.initModality(Modality.APPLICATION_MODAL);
        quantityStage.setTitle("Enter Quantity");
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        Label quantityLabel = new Label("Enter Quantity:");
        TextField quantityField = new TextField();
        Button confirmButton = new Button("Confirm");
        Button cancelButton = new Button("Cancel");
        HBox buttonBox = new HBox(10, confirmButton, cancelButton);
        buttonBox.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(quantityLabel, quantityField, buttonBox);
        confirmButton.setOnAction(confirmEvent -> {
            try {
                int quantity = Integer.parseInt(quantityField.getText());
                if (quantity <= 0) {
                    showError("Please enter a positive number");
                    throw new NumberFormatException("Quantity must be positive.");

                }

                if (User.getInventory().get(productIndex).getProductStock() < quantity) {
                    showError("Insufficient stock available.");
                    return;
                }

                customer.addProductToCart(User.getInventory().get(productIndex).getProductName(), quantity);
                showSuccess("Product added to cart.");
                quantityStage.close();
            } catch (NumberFormatException e) {
                showError("Please enter a valid positive integer for quantity.");
            }
        });
        cancelButton.setOnAction(cancelEvent -> quantityStage.close());

        Scene scene = new Scene(layout, 300, 150);
        quantityStage.setScene(scene);
        quantityStage.showAndWait();
    }

    private void showProductDetails(int productIndex, String productDescription, double productPrice, String category) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Product Details");

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        String productName = User.getInventory().get(productIndex).getProductName();
        Label nameLabel = createLabel("Product Name: " + productName);
        Label descriptionLabel = createLabel("Description: " + productDescription);
        Label priceLabel = createLabel("Price: $" + productPrice);
        Label categoryLabel = createLabel("Category: " + category);

        VBox reviewsSection = new VBox(10);
        reviewsSection.setPadding(new Insets(10));
        Label reviewsHeader = new Label("Reviews:");
        reviewsHeader.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        reviewsSection.getChildren().add(reviewsHeader);

        ArrayList<Review> reviews = customer.loadReviewsFromFile(productName);

        if (reviews.isEmpty()) {
            Label noReviewsLabel = new Label("No reviews yet for this product.");
            reviewsSection.getChildren().add(noReviewsLabel);
        } else {
            for (Review review : reviews) {
                reviewsSection.getChildren().add(createReviewBox(review));
            }
        }

        Button addReviewButton = new Button("Add Review");
        addReviewButton.setOnAction(e -> {
            boolean alreadyReviewed = reviews.stream()
                    .anyMatch(review -> review.getReviewerName().equals(customerSignInController.getCustomerUsername()));
            if (alreadyReviewed) {
                showAlert("Error", "You have already reviewed this product!");
            } else {
                openAddReviewWindow(productName, reviews, reviewsSection);
            }
        });

        Button deleteReviewButton = new Button("Delete Review");
        deleteReviewButton.setOnAction(e -> {
            Review reviewToDelete = reviews.stream()
                    .filter(review -> review.getReviewerName().equals(customerSignInController.getCustomerUsername()))
                    .findFirst()
                    .orElse(null);

            if (reviewToDelete == null) {
                showAlert("Error", "You haven't reviewed this product yet!");
            } else {

                customer.deleteReview(productName, customerSignInController.getCustomerUsername());
                reviews.remove(reviewToDelete);
                reviewsSection.getChildren().clear();
                reviewsSection.getChildren().add(reviewsHeader);
                for (Review review : reviews) {
                    reviewsSection.getChildren().add(createReviewBox(review));
                }

                showAlert("Success", "Review deleted successfully!");
            }

        });

        layout.getChildren().addAll(nameLabel, descriptionLabel, priceLabel, categoryLabel, reviewsSection, addReviewButton, deleteReviewButton);
        Scene scene = new Scene(layout, 400, 600);
        window.setScene(scene);
        window.showAndWait();
    }


    private VBox createReviewBox(Review review) {
        Label reviewerLabel = new Label("Reviewer: " + review.getReviewerName());
        Label ratingLabel = new Label("Rating: " + review.getReviewRating());
        Label reviewTextLabel = new Label("Review: " + review.getReviewText());
        VBox reviewBox = new VBox(5, reviewerLabel, ratingLabel, reviewTextLabel);
        reviewBox.setStyle("-fx-border-color: gray; -fx-border-width: 1; -fx-padding: 5;");
        return reviewBox;
    }

    private void openAddReviewWindow(String productName, ArrayList<Review> reviews, VBox reviewsSection) {
        Stage reviewWindow = new Stage();
        reviewWindow.setTitle("Add Review");
        reviewWindow.initModality(Modality.APPLICATION_MODAL);

        TextField scoreInputField = new TextField();
        scoreInputField.setPromptText("Enter review score (0-5)");

        TextArea reviewTextArea = new TextArea();
        reviewTextArea.setPromptText("Enter your review text");

        Button submitButton = new Button("Submit Review");
        submitButton.setOnAction(submit -> {
            try {
                double reviewScore = Double.parseDouble(scoreInputField.getText());
                if (reviewScore < 0 || reviewScore > 5) {
                    showAlert("Error", "Score must be between 0 and 5!");
                    return;
                }
                String reviewText = reviewTextArea.getText();
                Review newReview = new Review(customerSignInController.getCustomerUsername(), reviewScore, reviewText);
                reviews.add(newReview);
                customer.addReview(customerSignInController.getCustomerUsername(), productName, reviewScore, reviewText);reviewsSection.getChildren().clear();
                reviewsSection.getChildren().add(new Label("Reviews:"));
                for (Review review : reviews) {
                    reviewsSection.getChildren().add(createReviewBox(review));
                }

                showAlert("Success", "Review added successfully!");
                reviewWindow.close();
            } catch (NumberFormatException ex) {
                showAlert("Error", "Invalid score! Please enter a valid number.");
            }
        });

        VBox reviewLayout = new VBox(10, scoreInputField, reviewTextArea, submitButton);
        reviewLayout.setPadding(new Insets(10));
        Scene reviewScene = new Scene(reviewLayout, 300, 300);
        reviewWindow.setScene(reviewScene);
        reviewWindow.show();
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.showAndWait();
        return;
    }

    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.showAndWait();
    }

    public void setViewShoppingCart() {
        viewShoppingCart.setOnAction(e -> {
            Stage cartWindow = new Stage();
            cartWindow.initModality(Modality.APPLICATION_MODAL);
            cartWindow.setTitle("Shopping Cart");

            VBox cartLayout = new VBox(10);
            cartLayout.setPrefWidth(300);

            // Method to update the layout content based on current cart
            updateCartLayout(cartLayout, cartWindow);

            Scene cartScene = new Scene(cartLayout, 400, 300);
            cartWindow.setScene(cartScene);
            cartWindow.showAndWait();
        });
    }

    // Helper method to update cart layout with current shopping cart content
    private void updateCartLayout(VBox cartLayout, Stage cartWindow) {
        cartLayout.getChildren().clear();  // Clear the old content of cartLayout before adding updated content

        for (int j = 0; j < customer.getShoppingCart().size(); j++) {
            String productName = customer.getShoppingCart().get(j).getProductName();
            int productQuantity = customer.getShoppingCart().get(j).getProductQuantity();
            double productPrice = customer.getShoppingCart().get(j).getProductPrice();
            double totalPrice = productPrice * productQuantity;

            TextField productTextField = new TextField("- " + productName + " | Qty: " + productQuantity
                    + " | Price: Rs. " + totalPrice);
            productTextField.setEditable(false);  // Make it read-only

            Button removeProduct = new Button("Remove");
            int finalJ = j;
            removeProduct.setOnAction(removeEvent -> {
                // Remove the selected product from the cart
                customer.removeProductFromCart(productName);
                // Update the cart layout after removal without closing the window
                updateCartLayout(cartLayout, cartWindow);  // Refresh the displayed items in the cart
            });

            cartLayout.getChildren().addAll(productTextField, removeProduct);
        }
    }

    public void setCheckout() {
        checkout.setOnAction(e -> {
            if (customer.getShoppingCart().isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Empty Cart", "Your shopping cart is empty. Please add items before checkout.");
                return;
            }
            Stage window = new Stage();
            window.setTitle("Checkout");
            window.initModality(Modality.APPLICATION_MODAL);

            TextArea textArea = new TextArea();
            textArea.setEditable(false);

            StringBuilder stringBuilder = new StringBuilder("Shopping Cart\n");
            stringBuilder.append(String.format("%-20s %-10s %-10s %-10s\n", "Product Name", "Quantity", "Unit Price", "Total"));

            double sum;
            for (int i = 0; i < customer.getShoppingCart().size(); i++) {
                Product item = customer.getShoppingCart().get(i);  // Get the product from cart
                String productName = item.getProductName();
                int quantity = item.getProductQuantity();
                double unitPrice = item.getProductPrice();
                double totalPrice = unitPrice * quantity;
                stringBuilder.append(String.format("%-20s %-10d %-10.2f %-10.2f\n", productName, quantity, unitPrice, totalPrice));
            }
            sum = customer.calculateBill(customer.getShoppingCart());

            stringBuilder.append("\n__________________________\n")
                    .append(String.format("Total Bill: $%.2f", sum));

            textArea.setText(stringBuilder.toString());

            Button confirmButton = new Button("Confirm");
            Button cancelButton = new Button("Cancel");
            CheckBox discountCheckBox = new CheckBox("Apply Discount");

            confirmButton.setOnAction(f -> {
                Stage checkoutWindow = new Stage();
                checkoutWindow.setTitle("Checkout Window");
                checkoutWindow.initModality(Modality.APPLICATION_MODAL);

                TextField cardNoBox = new TextField();
                TextField CVVBox = new TextField();
                TextField Year = new TextField();

                ComboBox<String> monthComboBox = new ComboBox<>();
                monthComboBox.getItems().addAll(
                        String.valueOf(Month.JANUARY),
                        String.valueOf(Month.FEBRUARY),
                        String.valueOf(Month.MARCH),
                        String.valueOf(Month.APRIL),
                        String.valueOf(Month.MAY),
                        String.valueOf(Month.JUNE),
                        String.valueOf(Month.JULY),
                        String.valueOf(Month.AUGUST),
                        String.valueOf(Month.SEPTEMBER),
                        String.valueOf(Month.OCTOBER),
                        String.valueOf(Month.NOVEMBER),
                        String.valueOf(Month.DECEMBER)
                );

                Button confirmPayment = new Button("Confirm Payment");

                confirmPayment.setOnAction(g -> {
                    String cardNo = cardNoBox.getText();
                    int CVV = Integer.parseInt(CVVBox.getText());
                    String month = monthComboBox.getValue();
                    int year = Integer.parseInt(Year.getText());

                    boolean paymentSuccess = processPayment(cardNo, CVV, month, year);

                    if (paymentSuccess) {
                        showAlert(Alert.AlertType.INFORMATION, "Payment Successful", "Your payment was processed successfully.");

                        ArrayList<Orders> orderList = customer.createOrder(customerSignInController.getCustomerUsername());
                        double originalBill = customer.calculateBill(customer.getShoppingCart());
                        if (discountCheckBox.isSelected()) {
                            customer.discountPrice(customerSignInController.getCustomerUsername(), originalBill);
                        }
                        else {
                            customer.convertBilltoPoints(customerSignInController.getCustomerUsername());
                        }
                        customer.stockSubtraction();
                        checkoutWindow.close();
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Payment Failed", "There was an error with your payment details.");
                    }
                });

                VBox layout = new VBox(10);
                layout.getChildren().addAll(
                        new Label("Card Number:"), cardNoBox,
                        new Label("CVV:"), CVVBox,
                        new Label("Expiration Month:"), monthComboBox,
                        new Label("Expiration Year:"), Year,
                        confirmPayment
                );
                layout.setStyle("-fx-padding: 10; -fx-alignment: center;");

                Scene scene = new Scene(layout, 400, 300);
                checkoutWindow.setScene(scene);
                checkoutWindow.showAndWait();

            });

            cancelButton.setOnAction(f -> window.close());

            discountCheckBox.setOnAction(f -> {
                if (discountCheckBox.isSelected()) {
                    String username = customerSignInController.getCustomerUsername();
                    double bill = customer.calculateBill(customer.getShoppingCart());
                    int shoppingPoints = 0;
                    String fileName = "customers/" + username + ".txt";
                    try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
                        String line;
                        int lineNumber = 0;
                        while ((line = br.readLine()) != null) {
                            if (lineNumber == 7) { // Assuming shopping points are on line 7
                                shoppingPoints = Integer.parseInt(line.trim());
                                break;
                            }
                            lineNumber++;
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }

                    // Check shopping points
                    if (shoppingPoints > 0) {
                        double discountedPrice = ReturnDiscountedPrice(username, bill);
                        stringBuilder.append("\nFinal Price after Discount: Rs. ")
                                .append(String.format("%.2f", discountedPrice));
                        textArea.setText(stringBuilder.toString());
                    } else {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Insufficient Points");
                        alert.setHeaderText(null);
                        alert.setContentText("Your shopping points are insufficient to apply the discount.");
                        alert.showAndWait();

                        // Reset and disable the checkbox
                        discountCheckBox.setSelected(false);
                        discountCheckBox.setDisable(true);
                    }
                }
            });

            VBox layout = new VBox(10);
            layout.setStyle("-fx-padding: 10; -fx-alignment: center;");
            layout.getChildren().addAll(
                    textArea, discountCheckBox, new HBox(10, confirmButton, cancelButton)
            );

            Scene scene = new Scene(layout, 400, 300);
            window.setScene(scene);
            window.showAndWait();
        });
    }


    public boolean processPayment(String cardNo, int CVV, String month, int year) {
        if (cardNo == null || cardNo.length() != 16) {
            showAlert(Alert.AlertType.ERROR, "Invalid Card Number", "Please enter a valid 16-digit card number.");
            return false;
        }

        if (CVV <= 99 || CVV > 999) {
            showAlert(Alert.AlertType.ERROR, "Invalid CVV", "Please enter a valid 3-digit CVV.");
            return false;
        }

        if (month == null || month.isEmpty() || !isValidMonth(month)) {
            showAlert(Alert.AlertType.ERROR, "Invalid Month", "Please select a valid expiration month.");
            return false;
        }

        if (year <= 0 || year < 2023) {
            showAlert(Alert.AlertType.ERROR, "Invalid Year", "Please enter a valid expiration year.");
            return false;
        }

        return true;
    }

    private boolean isValidMonth(String month) {
        return month.equals("JANUARY") || month.equals("FEBRUARY") || month.equals("MARCH") ||
                month.equals("APRIL") || month.equals("MAY") || month.equals("JUNE") ||
                month.equals("JULY") || month.equals("AUGUST") || month.equals("SEPTEMBER") ||
                month.equals("OCTOBER") || month.equals("NOVEMBER") || month.equals("DECEMBER");
    }



public void setViewCustomerDetails() {
    Stage window = new Stage();
    window.setTitle("Customer Details");
    window.initModality(Modality.APPLICATION_MODAL);

    // Get customer ID
    String customerID = customerSignInController.getCustomerUsername();

    // TextArea to display customer details
    TextArea customerDetailsArea = new TextArea();
    customerDetailsArea.setEditable(false);
    String filePath = "customers/" + customerID + ".txt";
    File file = new File(filePath);

    if (file.exists()) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

            String line;
            ArrayList<String> userData =new ArrayList<>();
            int i=0;
            while((line=reader.readLine())!=null){
                userData.add(line);
            }
            userData.remove(3);
            String fileContent="Name: "+ userData.get(0)+"\n"+
                               "Email: "+ userData.get(1)+"\n"+
                               "Username: "+ userData.get(2)+"\n"+
                               "Address: "+ userData.get(3)+"\n"+
                               "Town: "+ userData.get(4)+"\n"+
                               "City: "+ userData.get(5)+"\n"+
                               "Shopping Points: "+ userData.get(6);
            customerDetailsArea.setText(fileContent);
        } catch (IOException ex) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error reading the customer file.");
        }
    } else {
        customerDetailsArea.setText("Customer file not found: " + filePath);
    }

    // Change Address Button
    Button changeAddressButton = new Button("Change Address");
    changeAddressButton.setOnAction(e -> {
        Stage changeAddressWindow = new Stage();
        changeAddressWindow.setTitle("Change Address");
        changeAddressWindow.initModality(Modality.APPLICATION_MODAL);

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setText(customerID); // Autofill current username
        TextField addressNoField = new TextField();
        addressNoField.setPromptText("Address No.");
        TextField townField = new TextField();
        townField.setPromptText("Town");
        TextField cityField = new TextField();
        cityField.setPromptText("City");

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");

        // Submit Button
        Button submitButton = new Button("Update Address");
        submitButton.setOnAction(action -> {
            if (usernameField.getText().isEmpty() ||
                    addressNoField.getText().isEmpty() ||
                    townField.getText().isEmpty() ||
                    cityField.getText().isEmpty()) {
                errorLabel.setText("All fields are required!");
                return;
            }

            Customer customer = new Customer();
            customer.changeAddress(
                    usernameField.getText(),
                    addressNoField.getText(),
                    townField.getText(),
                    cityField.getText()
            );

            changeAddressWindow.close();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Address updated successfully!");
        });

        VBox changeAddressLayout = new VBox(10);
        changeAddressLayout.getChildren().addAll(usernameField, addressNoField, townField, cityField, errorLabel, submitButton);
        changeAddressLayout.setPadding(new Insets(10));

        Scene changeAddressScene = new Scene(changeAddressLayout, 300, 250);
        changeAddressWindow.setScene(changeAddressScene);
        changeAddressWindow.showAndWait();
    });

    // Change Password Button
    Button changePasswordButton = new Button("Change Password");
    changePasswordButton.setOnAction(event -> {
        Stage passwordWindow = new Stage();
        passwordWindow.setTitle("Change Password");
        passwordWindow.initModality(Modality.APPLICATION_MODAL);

        VBox passwordLayout = new VBox(10);
        passwordLayout.setPadding(new Insets(10));

        Label titleLabel = new Label("Update Password");
        titleLabel.setFont(Font.font("Times New Roman Bold", 16));
        //getteing question from file
        File file1 = new File("customers/"+customerID+".txt");
        String line;
        ArrayList<String> Data=new ArrayList<>();
        try(BufferedReader reader=new BufferedReader(new FileReader(file1))){
            while((line=reader.readLine())!=null){
                Data.add(line);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if(Data.size()<10){
            showAlert(Alert.AlertType.ERROR,"Error","Set Private Question First");
            passwordWindow.close();
        }else {
            Label privateQusetion = new Label(Data.get(8));
            TextField privateAnswer = new TextField();
            privateAnswer.setPromptText("Enter Answer");
            TextField passwordField = new TextField();
            passwordField.setPromptText("New Password");

            Button updatePasswordButton = new Button("Update Password");
            updatePasswordButton.setOnAction(e -> {
                if (passwordField.getText().isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Password cannot be empty.");
                } else if (passwordField.getText().length() < 6) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Password must be at least 6 characters.");
                } else {
                    Customer customer = new Customer();
                    boolean success = customer.updatePassword(customerID, privateAnswer.getText(), passwordField.getText());
                    if (success) {
                        showAlert(Alert.AlertType.INFORMATION, "Success", "Password updated successfully.");
                        passwordWindow.close();
                    }
                }
            });

            passwordLayout.getChildren().addAll(titleLabel, privateQusetion, privateAnswer, passwordField, updatePasswordButton);
            Scene passwordScene = new Scene(passwordLayout, 300, 200);
            passwordWindow.setScene(passwordScene);
            passwordWindow.showAndWait();
        }
    });

    //Set private Question Button
    Label label =new Label("Set a private question that should be asked to update password");
    Button setPrivateQusetion= new Button("Set private Question");
    setPrivateQusetion.setOnAction(a->{
        showAlert(Alert.AlertType.INFORMATION,"Warning","Enter the question and answer carefully because you will not be able to change it later\nIt will be used to update your password");
        Stage setPrivateQuestionWindow = new Stage();
        setPrivateQuestionWindow.setTitle("Set Private Question");
        setPrivateQuestionWindow.initModality(Modality.APPLICATION_MODAL);

        // Set Private Question Window  layout
        VBox setPrivateQuestionWindowLayout = new VBox(10);
        setPrivateQuestionWindowLayout.setPadding(new Insets(10));

        Label titleLabel = new Label("Enter your private Qustion and its Answer");
        titleLabel.setFont(Font.font("Times New Roman Bold", 14));

        TextField questionField = new TextField();
        questionField.setPromptText("Question");
        TextField answerField = new TextField();
        answerField.setPromptText("Answer");

        Button save = new Button("Save");
        save.setOnAction(q->{
            if(questionField.getText().isEmpty()||answerField.getText().isEmpty()){
                showAlert(Alert.AlertType.ERROR,"Error","All Fields must be filled");
            }else {
                showAlert(Alert.AlertType.CONFIRMATION,"Setting Private Question","Your will not be able to change it later! Do you want to proceed?");
                boolean success = customer.savePrivateQusetion(customerID, questionField.getText(), answerField.getText());
                if(success){
                    showAlert(Alert.AlertType.INFORMATION,"Setting Private Question","Your Question and Answer has been saved suucessfully");
                    setPrivateQuestionWindow.close();
                    setPrivateQusetion.setDisable(true);
                }else
                    showAlert(Alert.AlertType.ERROR,"Setting Private Question","Save Failed");
            }
        });

        setPrivateQuestionWindowLayout.getChildren().addAll(titleLabel, questionField, answerField,save);
        Scene privateQuestionWindowScene = new Scene(setPrivateQuestionWindowLayout, 300, 200);
        setPrivateQuestionWindow.setScene(privateQuestionWindowScene);
        setPrivateQuestionWindow.showAndWait();
    });

    if(customer.checkIfPrivateQuestionExists(customerID)){
        setPrivateQusetion.setDisable(true);
    }

    // Bottom layout for buttons
        VBox buttonLayout = new VBox(10);
        buttonLayout.getChildren().addAll(changeAddressButton, changePasswordButton, setPrivateQusetion);

    buttonLayout.setPadding(new Insets(10));
    // Main layout
    VBox layout = new VBox(10);
    layout.getChildren().addAll(customerDetailsArea, buttonLayout);
    layout.setPadding(new Insets(10));

    // Scene setup
    Scene scene = new Scene(layout, 400, 300);
    window.setScene(scene);
    window.showAndWait();
}

    public void setLogOut() {
        logOut.setOnAction(e -> {
            try {
                this.customerSignInController.setCustomerUsername(null);
                Parent root = FXMLLoader.load(getClass().getResource("StartPage.fxml"));
                Stage stage = (Stage) logOut.getScene().getWindow();
                stage.setScene(new Scene(root));
            } catch (IOException ex) {
                throw new RuntimeException("Failed to load AdminSignInPage.fxml", ex);
            }

        });
    }

    public void setViewHistory() {
        viewHistory.setOnAction(e -> {
            Stage window = new Stage();
            window.initModality(Modality.APPLICATION_MODAL);

            TextArea orderHistoryTextArea = new TextArea();
            VBox modalLayout = new VBox(10);

            orderHistoryTextArea.setEditable(false);
            orderHistoryTextArea.setPrefWidth(400);
            orderHistoryTextArea.setPrefHeight(300);

           orderHistoryTextArea.setText(customer.viewOrderHistory(customerSignInController.getCustomerUsername()));
            Button closeBtn = new Button("Close");
            closeBtn.setOnAction(ev -> window.close());

            modalLayout.getChildren().addAll(orderHistoryTextArea, closeBtn);

            // Set scene and show the modal window
            Scene modalScene = new Scene(modalLayout, 450, 400);
            window.setScene(modalScene);
            window.setTitle("Order History - "+customerSignInController.getCustomerUsername());
            window.show();
        });
    }



    public void searchinInventoryLogic() {
        Admin admin = new Admin();

        searchProduct.setOnAction(e -> {
            String selectedProduct = searchBox.getEditor().getText().trim();

            if (selectedProduct.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Product name cannot be empty.");
                return;
            }

            if (admin.searchProductInInventory(selectedProduct)) {
                showAlert(Alert.AlertType.INFORMATION, "Product Found", "The product exists in the inventory.");

                Stage productWindow = new Stage();
                productWindow.setTitle("Product Details");

                ScrollPane scrollPane = new ScrollPane();
                GridPane searchResultsGridPane = new GridPane();
                scrollPane.setContent(searchResultsGridPane);
                scrollPane.setFitToWidth(true);

                ArrayList<Product> inventory = User.getInventory();
                searchResultsGridPane.getChildren().clear();
                for (int i = 0; i < inventory.size(); i++) {
                    Product product = inventory.get(i);
                    if (product.getProductName().equalsIgnoreCase(selectedProduct)) {
                        int finalI = i;
                        String productName = product.getProductName();
                        double productPrice = product.getProductPrice();
                        String productDescription = product.getProductDescription();
                        String category = String.valueOf(product.getCategory());
                        String imagePath = product.getImageFilepath();

                        Label nameLabel = createLabel("Name: " + productName);
                        Label priceLabel = createLabel("Price: Rs. " + productPrice);
                        Label descriptionLabel = createLabel("Description: " + productDescription);
                        Label categoryLabel = createLabel("Category: " + category);
                        Button addToCartButton = createButton("Add to Cart");
                        Button viewDetails = createButton("View Details");

                        ImageView imageView = loadProductImage(imagePath);
                        addToCartButton.setOnAction(event -> handleAddToCart(finalI));
                        viewDetails.setOnAction(event -> showProductDetails(finalI, productDescription, productPrice, category));

                        int row = 0; // Add to the first row since only one product is searched
                        searchResultsGridPane.add(imageView, 0, row);
                        searchResultsGridPane.add(nameLabel, 1, row);
                        searchResultsGridPane.add(priceLabel, 2, row);
                        searchResultsGridPane.add(descriptionLabel, 3, row);
                        searchResultsGridPane.add(categoryLabel, 4, row);
                        searchResultsGridPane.add(addToCartButton, 5, row);
                        searchResultsGridPane.add(viewDetails, 6, row);

                        GridPane.setMargin(imageView, new Insets(5));
                        GridPane.setMargin(nameLabel, new Insets(5));
                        GridPane.setMargin(priceLabel, new Insets(5));
                        GridPane.setMargin(descriptionLabel, new Insets(5));
                        GridPane.setMargin(categoryLabel, new Insets(5));
                        GridPane.setMargin(addToCartButton, new Insets(5));
                        GridPane.setMargin(viewDetails, new Insets(5));

                        break;
                    }
                }

                Scene scene = new Scene(scrollPane, 721, 147);
                productWindow.setScene(scene);
                productWindow.showAndWait();
            } else {
                showAlert(Alert.AlertType.ERROR, "Not Found", "No such product is present in the inventory.");
            }
        });
    }


    public double ReturnDiscountedPrice (String username, double totalBill) {
        String filePath = "customers/" + username + ".txt";
        ArrayList<String> userData = new ArrayList<>();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                userData.add(line);
            }

            if (userData.size() < 8) {
                throw new RuntimeException("Invalid file format for user data.");
            }

            int shoppingPoints = Integer.parseInt(userData.get(7));
            customer.setShoppingPoints(shoppingPoints);
            double discountValue = shoppingPoints * 10;

            double discountedPrice = totalBill - discountValue;

            if (discountedPrice < 0) {
                int leftoverPoints = (int) Math.abs(discountedPrice) / 10;
                userData.set(7, String.valueOf(leftoverPoints));
                discountedPrice = 0;
                return discountedPrice;
            } else {
                userData.set(7, "0");
                return discountedPrice;
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("User file not found: " + username, e);
        } catch (IOException e) {
            throw new RuntimeException("Error reading or writing user file.", e);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Invalid shopping points format in file.", e);
        }
    }



    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

}

