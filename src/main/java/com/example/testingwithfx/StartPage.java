package com.example.testingwithfx;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class StartPage {

    @FXML
    private Button adminSignIn;
    @FXML
    private Button customerSignIn;
    @FXML
    private Button signUp;
    @FXML
    private Button superAdminSignIn;
    @FXML
    private Button deleteCustomerProfile;

    Customer customer = new Customer();

    public void goToSignUp() {
        signUp.setOnAction(e -> {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("SignUpPage.fxml"));
                Stage stage = (Stage) signUp.getScene().getWindow();
                stage.setScene(new Scene(root));
            } catch (IOException ex) {
                throw new RuntimeException("Failed to load SignUpPage.fxml", ex);
            }
        });
    }

    public void goToSuperAdminSignIn() {
        superAdminSignIn.setOnAction(e -> {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("SuperAdminSignInPage.fxml"));
                Stage stage = (Stage) superAdminSignIn.getScene().getWindow();
                stage.setScene(new Scene(root));
            } catch (IOException ex) {
                throw new RuntimeException("Failed to load InventoryManagerSignInPage.fxml", ex);
            }
        });
    }


    public void goToAdminSignIn() {
        adminSignIn.setOnAction(e -> {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("AdminSignIn.fxml"));
                Stage stage = (Stage) adminSignIn.getScene().getWindow();
                stage.setScene(new Scene(root));
            } catch (IOException ex) {
                throw new RuntimeException("Failed to load AdminSignInPage.fxml", ex);
            }
        });
    }

    public void goToCustomerSignIn() {
        customerSignIn.setOnAction(e -> {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("CustomerSignIn.fxml"));
                Stage stage = (Stage) customerSignIn.getScene().getWindow();
                stage.setScene(new Scene(root));
            } catch (IOException ex) {
                throw new RuntimeException("Failed to load CustomerSignInPage.fxml", ex);
            }
        });
    }

    public void setDeleteCustomerProfile() {
        deleteCustomerProfile.setOnAction(e -> {
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            TextField usernameField = new TextField();
            usernameField.setPromptText("Username");
            TextField passwordField = new TextField();
            passwordField.setPromptText("Password");
            Button deleteButton = new Button("Delete Profile");
            deleteButton.setOnAction(event -> {
                String enteredUsername = usernameField.getText();
                String enteredPassword = passwordField.getText();
                if (enteredUsername.isEmpty() || enteredPassword.isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Empty fields");
                    alert.setContentText("Fill in all fields correctly");
                    alert.show();
                    System.out.println("Username or password is empty.");
                    return;
                }

                String filePath = "customers/" + enteredUsername + ".txt";
                try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                    String line1 = reader.readLine();
                    String line2 = reader.readLine();
                    String line3 = reader.readLine();
                    String line4 = reader.readLine();
                    reader.close();
                    if (enteredUsername.equals(line3) && enteredPassword.equals(line4)) {
                        customer.deleteCustomerProfile(enteredUsername);
                        System.out.println("Profile deleted successfully.");
                        stage.close();
                    } else {
                        System.out.println("Incorrect username or password.");

                    }
                } catch (IOException ex) {
                    System.out.println("File not found or error reading the file.");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("No profile found");
                    alert.setContentText("No such profile found");
                    alert.show();
                    ex.printStackTrace();
                }
            });
            VBox layout = new VBox(10);
            layout.getChildren().addAll(usernameField, passwordField, deleteButton);
            Scene scene = new Scene(layout, 300, 200);
            stage.setScene(scene);
            stage.setTitle("Delete Customer Profile");
            stage.show();
        });
    }

}

