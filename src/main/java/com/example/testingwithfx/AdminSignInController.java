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

import java.io.IOException;

public class AdminSignInController {
    @FXML
    private TextField ID;
    @FXML
    private PasswordField password;
    @FXML
    private ComboBox<AdminRank> role;
    @FXML
    private TextField email;
    @FXML
    private Button signIn;
    @FXML
    private Button back;
    @FXML
    private Button deleteProfile;

    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void initialize() {
        ObservableList<AdminRank> ranks = FXCollections.observableArrayList(AdminRank.values());
        role.setItems(ranks);
    }

    public void signInLogic() {
        signIn.setOnAction(e -> {
            Admin admin = new Admin();
            username = ID.getText();
            setUsername(username);
            String pw = password.getText();
            String adminRole = String.valueOf(role.getValue());
            String mail = email.getText();

            boolean success = admin.signIn(username, pw, mail, adminRole);

            if (success) {
                showAlert(AlertType.INFORMATION, "Sign-In Successful", null, "Welcome, " + username + "!");
                try {
                    System.out.println("Logging in with Username: " + ID + ", Password: " + pw + "email" + email + ", Role: " + adminRole);

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("AdminMainPage.fxml"));
                    Parent root = loader.load();

                    AdminMainPage controller = loader.getController();
                    controller.setAdminSignInController(this);

                    Stage stage = (Stage) signIn.getScene().getWindow();
                    stage.setScene(new Scene(root));
                    System.out.println("Logging in with Username: " + ID + ", Password: " + pw + "email" + email + ", Role: " + adminRole);


                } catch (IOException ex) {
                    throw new RuntimeException("Failed to load AdminMainPage.fxml", ex);
                }
            } else {
                System.out.println("Logging in with Username: " + ID + ", Password: " + pw + "email" + email + ", Role: " + adminRole);


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
