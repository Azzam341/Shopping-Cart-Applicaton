package com.example.testingwithfx;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Random;

public class CustomerSignInController {
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private TextField generatedCaptcha;
    @FXML
    private TextField enteredCaptcha;
    @FXML
    private Button signIn;
    @FXML
    private Button back;
    @FXML
    private String captchaString;

    private String customerUsername;

    public String getCustomerUsername() {
        return customerUsername;
    }

    public void setCustomerUsername(String customerUsername) {
        this.customerUsername=customerUsername;
    }

    @FXML
    public void initialize() {
        captchaString = generateCaptcha(10);
        generatedCaptcha.setText(captchaString);
        generatedCaptcha.setEditable(false);
        //generatedCaptcha.addEventFilter(javafx.scene.input.MouseEvent.MOUSE_DRAGGED, event -> event.consume());
        //generatedCaptcha.addEventFilter(javafx.scene.input.MouseEvent.MOUSE_PRESSED, event -> event.consume());

    }

    public void signInLogic() {
        signIn.setOnAction(e -> {
            String ID = username.getText();
            setCustomerUsername(ID);
            String pw = password.getText();
            String EC = enteredCaptcha.getText();

            if (ID.isEmpty() || pw.isEmpty() || EC.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Input Error", null, "All fields are required.");
                return;
            }

            Customer customer = new Customer();
            boolean success = customer.signIn(ID, pw, captchaString, EC);

            if (success) {
                try {

                    showAlert(Alert.AlertType.INFORMATION, "Sign-In Successful", null, "Welcome, " + ID + "!");
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("CustomerMainPage.fxml"));
                    Parent root = loader.load();
                    CustomerMainPage controller = loader.getController();
                    controller.setCustomerSignInController(this);
                    Stage stage = (Stage) signIn.getScene().getWindow();
                    stage.setScene(new Scene(root));

                } catch (IOException ex) {
                    System.out.println("Failed to load CustomerMainPage.fxml");
                    showAlert(Alert.AlertType.ERROR, "Sign-In Failed", null, "Could not load the customer page. Please try again.");
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "Sign-In Failed", null, "Invalid credentials or captcha. Please try again.");
            }
        });
    }


    private String generateCaptcha(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder captcha = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            captcha.append(characters.charAt(random.nextInt(characters.length())));
        }
        return captcha.toString();
    }

    public void setBack()
    {
        back.setOnAction( e ->
        {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("StartPage.fxml"));
                Stage stage = (Stage) back.getScene().getWindow();
                stage.setScene(new Scene(root));
            } catch (IOException ex) {
                throw new RuntimeException("Failed to load AdminSignInPage.fxml", ex);
            }
        });

    }

    private void showAlert(Alert.AlertType alertType, String title, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }
}

