package com.example.testingwithfx;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;

public class SignUpController {
    @FXML
    private TextField name;

    @FXML
    private TextField email;

    @FXML
    private TextField ID;

    @FXML
    private TextField password;

    @FXML
    private TextField address;

    @FXML
    private TextField town;

    @FXML
    private TextField city;

    @FXML
    private Button ok;

    @FXML
    private Button back;

    Customer customer = new Customer();

    @FXML
    public void initialize() {
        // Remove role selection, this controller is now just for customer sign up
    }

    @FXML
    public void clickOK() {
        ok.setOnAction(e -> {
            if (validateInputs()) {
                String username = ID.getText();
                if (checkFileExists(username)) {
                    showAlert(AlertType.ERROR, "File Already Exists", "A customer with this username already exists.");
                    return;
                }

                // Proceed with customer sign-up
                User user = customer.signUp(
                        name.getText(),
                        email.getText(),
                        username,
                        password.getText(),
                        address.getText(),
                        town.getText(),
                        city.getText()
                );

                if (user instanceof Customer) {
                    showAlert(AlertType.CONFIRMATION, "Successful Sign Up", "Sign up was successful!");
                    System.out.println("Customer signed up successfully with ID: " + user.getUserID());
                } else {
                    System.out.println("Sign-up failed: Returned user is not a Customer.");
                }
            }
        });
    }

    private boolean validateInputs() {
        if (name.getText().isEmpty()) {
            showAlert(AlertType.ERROR, "Validation Error", "Name cannot be empty.");
            return false;
        }

        if (email.getText().isEmpty() || !isValidEmail(email.getText())) {
            showAlert(AlertType.ERROR, "Validation Error", "Invalid email format.");
            return false;
        }

        if (ID.getText().isEmpty()) {
            showAlert(AlertType.ERROR, "Validation Error", "User ID cannot be empty.");
            return false;
        }

        if (password.getText().isEmpty() || password.getText().length() < 6) {
            showAlert(AlertType.ERROR, "Validation Error", "Password must be at least 6 characters.");
            return false;
        }

        if (address.getText().isEmpty()) {
            showAlert(AlertType.ERROR, "Validation Error", "Address cannot be empty.");
            return false;
        }

        if (town.getText().isEmpty()) {
            showAlert(AlertType.ERROR, "Validation Error", "Town cannot be empty.");
            return false;
        }

        if (city.getText().isEmpty()) {
            showAlert(AlertType.ERROR, "Validation Error", "City cannot be empty.");
            return false;
        }

        return true;
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(gmail|yahoo)\\.com$";
        return email.matches(emailRegex);
    }

    public void setBack() {
        back.setOnAction(e -> {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("StartPage.fxml"));
                Stage stage = (Stage) back.getScene().getWindow();
                stage.setScene(new Scene(root));
            } catch (IOException ex) {
                throw new RuntimeException("Failed to load StartPage.fxml", ex);
            }
        });
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public boolean checkFileExists(String username) {
        String filePath = "customers/" + username + ".txt";

        File file = new File(filePath);
        return file.exists();
    }
}
