package com.example.testingwithfx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class SuperAdminSignInController {
    @FXML
    private TextField ID;
    @FXML
    private PasswordField password;
    @FXML
    private Button signIn;
    @FXML
    private Button back;

    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public void signInLogic() {
        signIn.setOnAction(e -> {
            superAdmin superAdmin = new superAdmin();
            username = ID.getText();
            setUsername(username);
            String pw = password.getText();

            boolean success = superAdmin.signIn(username, pw, null,null);

            if (success) {
                showAlert(AlertType.INFORMATION, "Sign-In Successful", null, "Welcome, " + username + "!");
                try {
                    System.out.println("Logging in with Username: " + ID + ", Password: " + pw );

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("SuperAdminMainPage.fxml"));
                    Parent root = loader.load();
                    Stage stage = (Stage) signIn.getScene().getWindow();
                    stage.setScene(new Scene(root));
                    System.out.println("Logging in with Username: " + ID + ", Password: " + pw );


                } catch (IOException ex) {
                    throw new RuntimeException("Failed to load AdminMainPage.fxml", ex);
                }
            } else {
                System.out.println("Logging in with Username: " + ID + ", Password: " + pw + "email");


                showAlert(AlertType.ERROR, "Sign-In Failed", null, "Invalid credentials or role. Please try again.");
            }
        });
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


    private void showAlert(AlertType alertType, String title, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

}
