package com.example.testingwithfx;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.swing.text.LabelView;
import java.io.*;
import java.util.ArrayList;
import java.util.Queue;


public class AdminMainPage {
    private AdminSignInController adminSignInController;

    public void setAdminSignInController(AdminSignInController controller) {
        this.adminSignInController = controller;
    }

    Admin admin = new Admin();
    @FXML
    private Button addtoInventory;

    @FXML
    private Button removefromInventory;

    @FXML
    private Button searchinInventory;

    @FXML
    private Button updateProductDetails;

    @FXML
    private Button viewInventory;

    @FXML
    private Button adminDetails;

    @FXML
    private Button logOut;

    @FXML
    private Button restock;
    @FXML
    private TextField maxSpace;
    @FXML
    private TextField spaceTaken;
    @FXML
    private Button discardStock;

    String imagePath;

    public void initialize() {
        addtoInventoryLogic();
        removefromInventoryLogic();
        searchinInventoryLogic();
        updateProductDetailsLogic();
        viewInventory();
        adminDetails();
        restockLogic();
        setLogOut();
        populateMaxInventoryTextfields();
        populateSpaceTaken();
    }

    public void addtoInventoryLogic() {
        addtoInventory.setOnAction(e -> {
            Stage window = new Stage();
            window.setTitle("Add Product to Inventory");
            window.initModality(Modality.APPLICATION_MODAL);

            TextField productNameBox = new TextField();
            productNameBox.setPromptText("Enter the product name");

            TextField productPriceBox = new TextField();
            productPriceBox.setPromptText("Enter the product price");


            TextField productStockBox = new TextField();
            productStockBox.setPromptText("Enter the stock of products");


            TextField productDescription = new TextField();
            productDescription.setPromptText("Enter description of products");

            ComboBox<String> categoryComboBox = new ComboBox<>();
            categoryComboBox.getItems().addAll(
                    String.valueOf(Category.FRUITS),
                    String.valueOf(Category.VEGETABLES),
                    String.valueOf(Category.BAKERY_ITEMS),
                    String.valueOf(Category.DAIRY),
                    String.valueOf(Category.BEVERAGES),
                    String.valueOf(Category.DESSERTS),
                    String.valueOf(Category.FAST_FOOD),
                    String.valueOf(Category.MEAT),
                    String.valueOf(Category.SNACKS),
                    String.valueOf(Category.SEAFOOD)

            );

            Button addImage = new Button("Add Image");
            addImage.setOnAction(f -> {
                FileChooser fileChooser = new FileChooser();
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));
                File selectedFile = fileChooser.showOpenDialog(window);
                if (selectedFile != null) {
                    imagePath = "file:"+selectedFile.getAbsolutePath();
                } else {
                    imagePath = "file:C:/Users/HP/IdeaProjects/TestingwithFX/src/main/resources/Not Found.png";
                }
            });


            Button addButton = new Button("Add Product");
            addButton.setOnAction(event -> {
                String name = productNameBox.getText().trim();
                String priceText = productPriceBox.getText().trim();
                String stockText = productStockBox.getText().trim();
                String categoryText = categoryComboBox.getValue();
                String description = productDescription.getText().trim();


                if (name.isEmpty() || priceText.isEmpty() || stockText.isEmpty() || categoryText == null || description.isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please fill in all fields correctly.");
                    return;
                }

                try {
                    double price = Double.parseDouble(priceText);
                    int stock = Integer.parseInt(stockText);
                    if (price < 0 || stock < 0) {
                        showAlert(Alert.AlertType.ERROR, "Negative stock or price", "Please enter positive field");
                        return;

                    }

                    if (price > 100000) {
                        showAlert(Alert.AlertType.ERROR, "Invalid Price", "Price must be less than 100000");
                        return;

                    }
                    Category category;
                    try {
                        category = Category.valueOf(categoryText.toUpperCase());
                    } catch (IllegalArgumentException ex) {
                        showAlert(Alert.AlertType.ERROR, "Invalid Category", "Selected category is invalid.");
                        return;
                    }

                    if (admin.searchProductInInventory(name)) {
                        showAlert(Alert.AlertType.ERROR, "Duplicate Product", "A product with this name already exists in the inventory.");
                        return;
                    }
                    int currentSpaceTaken = Integer.parseInt(spaceTaken.getText());
                    int maxAvailableSpace = Integer.parseInt(maxSpace.getText());

                    if (stock + currentSpaceTaken > maxAvailableSpace) {
                        showAlert(Alert.AlertType.ERROR, "Exceeded Stock", "You are exceeding stock value");
                        return;
                    }

                    spaceTaken.setText(String.valueOf(currentSpaceTaken + stock)); // Update space taken
                    admin.addProductToInventory(name, price, stock, category, description, imagePath);

                    showAlert(Alert.AlertType.INFORMATION, "Success", "Product added successfully!");
                    window.close();

                } catch (NumberFormatException exception)
                {
                    exception.getMessage();
                    showAlert(Alert.AlertType.ERROR, "Wrong number format", "Please note stock must be integer value");
                }
            });

            VBox layout = new VBox(15);
            layout.setPadding(new Insets(15));
            layout.getChildren().addAll(
                    new Label("Product Name:"), productNameBox,
                    new Label("Product Price:"), productPriceBox,
                    new Label("Product Stock:"), productStockBox,
                    new Label("Product Category:"), categoryComboBox,
                    new Label("Product Description:"), productDescription,
                    addImage, addButton
            );

            Scene scene = new Scene(layout, 800, 600);
            window.setScene(scene);
            window.showAndWait();
        });
    }

    // Remove product from inventory logic
    public void removefromInventoryLogic() {
        removefromInventory.setOnAction(e -> {
            Stage window = new Stage();
            window.setTitle("Remove Product from Inventory");
            window.initModality(Modality.APPLICATION_MODAL);

            VBox layout = new VBox(10);
            layout.setPadding(new Insets(10));
            layout.setAlignment(Pos.CENTER);

            Label instructionLabel = new Label("Enter the name of the product you want to remove:");
            TextField productNameBox = new TextField();
            productNameBox.setPromptText("Product Name");

            Button removeButton = new Button("Remove");

            Button cancelButton = new Button("Cancel");

            layout.getChildren().addAll(instructionLabel, productNameBox, removeButton, cancelButton);

            Scene scene = new Scene(layout, 400, 200);
            window.setScene(scene);
            window.show();

            removeButton.setOnAction(ev -> {
                String name = productNameBox.getText().trim();

                if (name.isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Invalid Input", "Product name cannot be empty.");
                    return;
                }
                int index = 0;
                boolean productFound = false;
                for (int i = 0; i < Admin.getInventory().size(); i++) {
                    if (name.equalsIgnoreCase(Admin.getInventory().get(i).getProductName())) {
                        try {
                            index = i;
                            spaceTaken.setText(String.valueOf(Integer.parseInt(spaceTaken.getText()) - User.getInventory().get(index).getProductStock()));
                            admin.removeProductFromInventory(name);
                        } catch (FileNotFoundException ex) {
                            throw new RuntimeException(ex);
                        }
                        productFound = true;
                        break;
                    }
                }

                if (productFound) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Product removed successfully.");
                    window.close();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Not Found", "No such product is present in the inventory.");

                }
            });

            cancelButton.setOnAction(ev -> window.close());
        });
    }

    // Search product in inventory logic
    public void searchinInventoryLogic() {
        searchinInventory.setOnAction(e -> {
            Stage window = new Stage();
            window.setTitle("Search Product in Inventory");
            window.initModality(Modality.APPLICATION_MODAL);

            VBox layout = new VBox(10);
            layout.setPadding(new Insets(10));
            layout.setAlignment(Pos.CENTER);

            Label instructionLabel = new Label("Enter the name of the product to search:");
            TextField productNameBox = new TextField();
            productNameBox.setPromptText("Product Name");

            Button searchButton = new Button("Search");

            Button cancelButton = new Button("Cancel");

            layout.getChildren().addAll(instructionLabel, productNameBox, searchButton, cancelButton);

            Scene scene = new Scene(layout, 400, 200);
            window.setScene(scene);
            window.show();

            searchButton.setOnAction(ev -> {
                String name = productNameBox.getText().trim();

                if (name.isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Invalid Input", "Product name cannot be empty.");
                    return;
                }

                if (admin.searchProductInInventory(name)) {
                    showAlert(Alert.AlertType.INFORMATION, "Product Found", "The product exists in the inventory.");

                } else {
                    showAlert(Alert.AlertType.ERROR, "Not Found", "No such product is present in the inventory.");

                }
            });

            cancelButton.setOnAction(ev -> window.close());
        });
    }

    public void updateProductDetailsLogic() {
        updateProductDetails.setOnAction(e -> {
            Stage window = new Stage();
            window.setTitle("Update Product Details");
            window.initModality(Modality.APPLICATION_MODAL);

            VBox layout = new VBox(10);
            layout.setPadding(new Insets(10));
            layout.setAlignment(Pos.CENTER);

            Label instructionLabel = new Label("Enter product name to update:");
            TextField productNameBox = new TextField();
            productNameBox.setPromptText("Product Name");

            Label fieldLabel = new Label("Enter field number to update (1-4):");
            Label priceLabel = new Label("1: Price (e.g., 2.5)");
            Label categoryLabel = new Label("2: Category (select from dropdown)");
            Label descriptionLabel = new Label("3: Description (e.g., Fresh apples from the farm)");
            Label imagePathLabel = new Label("4: Image Path (choose a file)");

            TextField fieldBox = new TextField();
            fieldBox.setPromptText("Field Number");

            Label newValueLabel = new Label("Enter new value:");
            TextField newValueBox = new TextField();
            newValueBox.setPromptText("New Value");

            // ComboBox for Category
            Label categoryInstruction = new Label("Category Selection:");
            ComboBox<Category> categoryComboBox = new ComboBox<>();
            categoryComboBox.getItems().addAll(Category.values());
            categoryComboBox.setPromptText("Choose a category");

            // FileChooser for Image Selection
            Button imageChooseButton = new Button("Choose Image");
            TextField imagePathField = new TextField();
            imagePathField.setPromptText("Selected image file path");
            imagePathField.setEditable(false);
            imageChooseButton.setOnAction(ev -> {
                FileChooser fileChooser = new FileChooser();
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
                File selectedFile = fileChooser.showOpenDialog(window);
                if (selectedFile != null) {
                    String imagePath = "file:" + selectedFile.getAbsolutePath();
                    imagePathField.setText(imagePath);
                    newValueBox.setText(imagePath); // Update newValueBox with the selected image path
                }
            });

            // Event Listener for ComboBox Selection
            categoryComboBox.setOnAction(ev -> {
                if (categoryComboBox.getValue() != null) {
                    newValueBox.setText(categoryComboBox.getValue().name()); // Update newValueBox with selected category
                }
            });

            Button updateButton = new Button("Update");
            Button cancelButton = new Button("Cancel");

            layout.getChildren().addAll(instructionLabel, productNameBox, fieldLabel, priceLabel,
                    categoryLabel, categoryInstruction, categoryComboBox,
                    descriptionLabel, imagePathLabel, imageChooseButton, imagePathField,
                    fieldBox, newValueLabel, newValueBox, updateButton, cancelButton);

            Scene scene = new Scene(layout, 400, 500);
            window.setScene(scene);
            window.show();

            updateButton.setOnAction(ev -> {
                String productName = productNameBox.getText().trim();
                String fieldText = fieldBox.getText().trim();
                String newValue = newValueBox.getText().trim();

                if (productName.isEmpty() || fieldText.isEmpty() || newValue.isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please fill in all fields correctly.");
                    return;
                }

                int field;
                try {
                    field = Integer.parseInt(fieldText);

                    // Category validation
                    if (field == 2 && categoryComboBox.getValue() == null) {
                        showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please select a category.");
                        return;
                    }

                    // Image validation
                    if (field == 4 && !newValue.startsWith("file:")) {
                        showAlert(Alert.AlertType.ERROR, "Invalid Image Path", "Image path must start with 'file:'.");
                        return;
                    }

                    // Price validation
                    if (field == 1) {
                        double price = Double.parseDouble(newValue);
                        if (price <= 0) {
                            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Price must be greater than 0.");
                            return;
                        }
                    }

                } catch (NumberFormatException ex) {
                    showAlert(Alert.AlertType.ERROR, "Invalid Field", "Please enter a valid field number.");
                    return;
                }

                try {
                    boolean found = admin.searchProductInInventory(productName);
                    if (found) {
                        admin.updateProductInInventory(productName, field, newValue);
                        showAlert(Alert.AlertType.INFORMATION, "Success", "Product updated successfully!");
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Not Found", "Product not found in the inventory.");
                    }
                } catch (Exception ex) {
                    showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while updating the product: " + ex.getMessage());
                }
            });

            cancelButton.setOnAction(ev -> window.close());
        });
    }




    // Restock product logic
    public void restockLogic() {
        restock.setOnAction(e -> {
            Stage window = new Stage();
            window.setTitle("Restock Product");
            window.initModality(Modality.APPLICATION_MODAL);

            VBox layout = new VBox(10);
            layout.setPadding(new Insets(10));
            layout.setAlignment(Pos.CENTER);

            Label instructionLabel = new Label("Enter product name and new stock amount:");
            TextField productNameBox = new TextField();
            productNameBox.setPromptText("Product Name");

            TextField productNewStockBox = new TextField();
            productNewStockBox.setPromptText("New Stock Quantity");

            Button restockButton = new Button("Restock");
            Button cancelButton = new Button("Cancel");

            layout.getChildren().addAll(instructionLabel, productNameBox, productNewStockBox, restockButton, cancelButton);

            Scene scene = new Scene(layout, 400, 200);
            window.setScene(scene);
            window.show();

            restockButton.setOnAction(ev -> {
                String name = productNameBox.getText().trim();
                String newStockText = productNewStockBox.getText().trim();

                if (name.isEmpty() || newStockText.isEmpty() || Integer.parseInt(newStockText) < 0) {
                    showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please fill in all fields correctly.");
                    return;
                }

                try {
                    int newStock = Integer.parseInt(newStockText);
                    if(newStock <= 0)
                    {
                        showAlert(Alert.AlertType.ERROR, "Invalid Input", "Negative Value is not allowed.");
                        return;
                    }

                    int currentSpace = Integer.parseInt(spaceTaken.getText());
                    if (currentSpace + newStock > 2000) {
                        showAlert(Alert.AlertType.ERROR, "Space Limit Exceeded", "Adding this stock exceeds the maximum space limit of 2000.");
                        return;
                    }

                    boolean found = admin.searchProductInInventory(name);
                    if (found) {
                        admin.restock(name, newStock);
                        showAlert(Alert.AlertType.INFORMATION, "Success", "Product found successfully!");
                        populateSpaceTaken();
                        window.close();

                    }
                    else if (!found) {
                        showAlert(Alert.AlertType.ERROR, "Not Found", "Product not found in the inventory.");
                        return;
                    }

                    window.close();
                } catch (NumberFormatException ex) {
                    showAlert(Alert.AlertType.ERROR, "Invalid Input", "Stock quantity must be a valid number.");
                    return;
                }
            });

            cancelButton.setOnAction(ev -> window.close());
        });
    }

    public void discardStockLogic() {
        discardStock.setOnAction(e -> {
            Stage window = new Stage();
            window.setTitle("Discard Product");
            window.initModality(Modality.APPLICATION_MODAL);

            VBox layout = new VBox(10);
            layout.setPadding(new Insets(10));
            layout.setAlignment(Pos.CENTER);

            Label instructionLabel = new Label("Enter product name and amount you want to discard amount:");
            TextField productNameBox = new TextField();
            productNameBox.setPromptText("Product Name");

            TextField productNewStockBox = new TextField();
            productNewStockBox.setPromptText("Amount to discard:");

            Button discardButton = new Button("Discard");
            Button cancelButton = new Button("Cancel");

            layout.getChildren().addAll(instructionLabel, productNameBox, productNewStockBox, discardButton, cancelButton);

            Scene scene = new Scene(layout, 400, 200);
            window.setScene(scene);
            window.show();

            discardButton.setOnAction(ev -> {
                String productName = productNameBox.getText().trim();
                String newStockText = productNewStockBox.getText().trim();

                // Input validation for product name and new stock
                if (productName.isEmpty() || newStockText.isEmpty() || Integer.parseInt(newStockText) < 0) {
                    showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please fill in all fields correctly.");
                    return;
                }

                try {
                    int discardValue = Integer.parseInt(newStockText);

                    // Ensure discard value is valid
                    if (discardValue <= 0) {
                        showAlert(Alert.AlertType.ERROR, "Invalid Input", "Discard value must be greater than 0.");
                        return;
                    }

                    boolean productFound = false;
                    for (int i = 0; i < User.getInventory().size(); i++) {
                        // Check if the product name matches the one in the inventory
                        if (User.getInventory().get(i).getProductName().equalsIgnoreCase(productName)) {
                            int currentStock = User.getInventory().get(i).getProductStock();

                            // Check if the discard value is greater than the current stock
                            if (discardValue > currentStock) {
                                showAlert(Alert.AlertType.ERROR, "Invalid Input",
                                        "Discard value is greater than the current stock of " + currentStock + ".");
                                return;
                            }

                            // If the stock is valid, restock
                            int newStock = currentStock - discardValue;
                            productFound = true;

                            // Proceed to update stock for the product
                            admin.restock(productName, -discardValue);  // Restock logic (discarding products)
                            showAlert(Alert.AlertType.INFORMATION, "Success", "Product discarded successfully!");
                            populateSpaceTaken();  // Update the space taken field
                            window.close();
                            break;
                        }
                    }

                    if (!productFound) {
                        showAlert(Alert.AlertType.ERROR, "Not Found", "Product not found in the inventory.");
                        return;
                    }
                } catch (NumberFormatException ex) {
                    showAlert(Alert.AlertType.ERROR, "Invalid Input", "Stock quantity must be a valid number.");
                    return;
                }
            });

            cancelButton.setOnAction(ev -> window.close());
        });
    }


    public void viewInventory() {
        viewInventory.setOnAction(e -> {
            Stage window = new Stage();
            window.setTitle("Inventory");
            window.initModality(Modality.APPLICATION_MODAL);

            TextArea textArea = new TextArea();
            textArea.setEditable(false);

            StringBuilder inventoryDetails = new StringBuilder();
                inventoryDetails.append("Name -  Price  - Stock - Category \n");
            for (int i = 0; i < User.getInventory().size(); i++) {
                inventoryDetails.append(User.getInventory().get(i).getProductName())
                        .append(" - ")
                        .append(User.getInventory().get(i).getProductPrice())
                        .append(" - ")
                        .append(User.getInventory().get(i).getProductStock())
                        .append(" - ")
                        .append(User.getInventory().get(i).getCategory())
                        .append("\n");
            }

            textArea.setText(inventoryDetails.toString());
            Scene scene = new Scene(textArea, 400, 300);
            window.setScene(scene);
            window.showAndWait();
        });
    }

public void adminDetails() {
    adminDetails.setOnAction(e -> {
        Stage window = new Stage();
        window.setTitle("Admin Details");
        window.initModality(Modality.APPLICATION_MODAL);
        String adminID = adminSignInController.getUsername();

        // TextArea to display admin details
        TextArea adminDetailsArea = new TextArea();
        adminDetailsArea.setEditable(false);
        String filePath = "admins/" + adminID + ".txt";
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
                                   "Rank: "+ userData.get(6)+"\n"+
                                   "Salary: "+ userData.get(7);


                adminDetailsArea.setText(fileContent);
            } catch (IOException ex) {
                showAlert(Alert.AlertType.ERROR, "Error", "Error reading the admin file.");
                return;
            }
        } else {
            adminDetailsArea.setText("Admin file not found: " + filePath);
        }

        // Create the "Update Password" button
        Button updatePasswordButton = new Button("Update Password");
        updatePasswordButton.setOnAction(event -> {
            try {
                Stage passwordWindow = new Stage();
                passwordWindow.setTitle("Update Admin Password");
                passwordWindow.initModality(Modality.APPLICATION_MODAL);

                // Password update layout
                VBox passwordLayout = new VBox(10);
                passwordLayout.setPadding(new Insets(10));

                Label titleLabel = new Label("Update Password");
                titleLabel.setFont(Font.font("Times New Roman Bold", 16));

                File file1 = new File("admins/"+adminID+".txt");
                String line;
                ArrayList<String> userData=new ArrayList<>();
                try(BufferedReader reader=new BufferedReader(new FileReader(file1))){
                    while((line=reader.readLine())!=null){
                        userData.add(line);
                    }
                    reader.close();
                    if(userData.size()<11){
                        showAlert(Alert.AlertType.ERROR,"Error","Set Private Question and its Answer First");
                    }
                }
                //Private Question
                Label question=new Label(userData.get(9));
                //Textfield to get Answer of private question
                TextField privateAnswer = new TextField();
                privateAnswer.setPromptText("Answer");

                // Password TextField
                Label enterPassword=new Label("Enter new Password:");
                TextField passwordField = new TextField();
                passwordField.setPromptText("New Password");

                // Update Password Button
                Button updatePasswordBtn = new Button("Update Password");
                updatePasswordBtn.setOnAction(e2 -> {
                    if (passwordField.getText().isEmpty()||privateAnswer.getText().isEmpty()) {
                        showAlert(Alert.AlertType.ERROR, "Error", "All Fields must be filled Correctly");
                    } else if (passwordField.getText().length() < 6) {
                        showAlert(Alert.AlertType.ERROR, "Error", "Password must be at least 6 characters.");
                    } else {

                        User admin = new Admin();
                        boolean success = admin.updatePassword(adminID,privateAnswer.getText(), passwordField.getText());
                        if (success) {
                            showAlert(Alert.AlertType.INFORMATION, "Success", "Password updated successfully.");
                            passwordWindow.close();
                        }
                    }
                });

                passwordLayout.getChildren().addAll(titleLabel,question,privateAnswer,enterPassword, passwordField, updatePasswordBtn);
                Scene passwordScene = new Scene(passwordLayout, 300, 200);
                passwordWindow.setScene(passwordScene);
                passwordWindow.showAndWait();

            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        //setting private question and Answer window
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
                    boolean success = admin.savePrivateQusetion(adminID, questionField.getText(), answerField.getText());
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


        // Layout to hold the TextArea and Update Password Button
        VBox layout = new VBox(10);
        if(!admin.checkIfPrivateQuestionExists(adminID)){
        layout.getChildren().addAll(adminDetailsArea, updatePasswordButton,setPrivateQusetion);
        }else{
            layout.getChildren().addAll(adminDetailsArea, updatePasswordButton);
        }
        layout.setPadding(new Insets(10));

        // Scene setup
        Scene scene = new Scene(layout, 400, 300);
        window.setScene(scene);
        window.showAndWait();
    });
}


    public void setLogOut()
    {
        logOut.setOnAction(e -> {
            try {
                this.adminSignInController.setUsername(null);
                Parent root = FXMLLoader.load(getClass().getResource("StartPage.fxml"));
                Stage stage = (Stage) logOut.getScene().getWindow();
                stage.setScene(new Scene(root));
            } catch (IOException ex) {
                throw new RuntimeException("Failed to load AdminSignInPage.fxml", ex);
            }
        });

    }
    public void populateMaxInventoryTextfields() {
        final int INVENTORY_SPACE = 2000;
        maxSpace.setText(String.valueOf(INVENTORY_SPACE));
        maxSpace.setEditable(false);
        maxSpace.setStyle("-fx-alignment: center; -fx-font-family: 'Roboto'; -fx-font-size: 16;");
    }

    public void populateSpaceTaken() {
        int quantity = 0;
        for (int i = 0; i < User.getInventory().size(); i++) {
            quantity += User.getInventory().get(i).getProductStock();
        }
        spaceTaken.setText(String.valueOf(quantity));
        spaceTaken.setEditable(false);
        spaceTaken.setStyle("-fx-alignment: center; -fx-font-family: 'Roboto'; -fx-font-size: 16;");
    }


    // Method to show alerts
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}

