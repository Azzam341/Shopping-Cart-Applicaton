package com.example.testingwithfx;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;


public class SuperAdminMainPageController {


    superAdmin admin = new superAdmin();

    @FXML
    private Button logOut;

    @FXML
    private Button hireAdmin;
    @FXML
    private Button fireAdmin;
    @FXML
    private Button updateAdminRank;
    @FXML
    private Button changePassword;



    public void setLogOut()
    {
        logOut.setOnAction(e -> {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("StartPage.fxml"));
                Stage stage = (Stage) logOut.getScene().getWindow();
                stage.setScene(new Scene(root));
            } catch (IOException ex) {
                throw new RuntimeException("Failed to load StartPage.fxml", ex);
            }
        });
    }

    @FXML
    public void hireAdmin() {
        hireAdmin.setOnAction(e -> {
            Stage modalStage = new Stage();
            modalStage.setTitle("Add Admin");
            VBox root = new VBox(10);
            root.setPadding(new Insets(10));
            Label nameLabel = new Label("Name:");
            TextField nameField = new TextField();

            Label emailLabel = new Label("Email:");
            TextField emailField = new TextField();

            Label idLabel = new Label("User ID:");
            TextField idField = new TextField();

            Label passwordLabel = new Label("Password:");
            PasswordField passwordField = new PasswordField();

            Label addressLabel = new Label("Address:");
            TextField addressField = new TextField();

            Label townLabel = new Label("Town:");
            TextField townField = new TextField();

            Label cityLabel = new Label("City:");
            TextField cityField = new TextField();

            Button saveButton = new Button("Save");
            Button cancelButton = new Button("Cancel");

            // Add components to the layout
            root.getChildren().addAll(
                    nameLabel, nameField,
                    emailLabel, emailField,
                    idLabel, idField,
                    passwordLabel, passwordField,
                    addressLabel, addressField,
                    townLabel, townField,
                    cityLabel, cityField,
                    saveButton, cancelButton
            );

            // Handle Save button click
            saveButton.setOnAction(event -> {
                if (validateInputs(nameField, emailField, idField, passwordField, addressField, townField, cityField)) {
                    String username = idField.getText();
                    if (checkFileExists(idField.getText())) {
                        showAlert(Alert.AlertType.ERROR, "File Already Exists", "A user with this username already exists.");
                    } else {
                        admin.signUp(
                                nameField.getText(),
                                emailField.getText(),
                                username,
                                passwordField.getText(),
                                addressField.getText(),
                                townField.getText(),
                                cityField.getText()
                        );
                        showAlert(Alert.AlertType.INFORMATION, "Success", "Admin added successfully!");
                        modalStage.close();
                    }
                }
            });

            cancelButton.setOnAction(event -> modalStage.close());

            Scene scene = new Scene(root, 400, 500);
            modalStage.setScene(scene);
            modalStage.initOwner(hireAdmin.getScene().getWindow());
            modalStage.initModality(Modality.WINDOW_MODAL);
            modalStage.showAndWait();
        });
    }

    @FXML
    public void fireAdmin() {
        fireAdmin.setOnAction(e -> {
            // Create a new Stage for the modal
            Stage modalStage = new Stage();
            modalStage.setTitle("Fire Admin");

            // Define modal elements
            TextField usernameField = new TextField();
            usernameField.setPromptText("Enter Username");

            Button deleteButton = new Button("Delete");
            Button cancelButton = new Button("Cancel");

            // Layout for modal (use VBox, HBox, or other as needed)
            VBox layout = new VBox(10);
            layout.getChildren().addAll(
                    new Label("Enter the username of the Admin to delete:"),
                    usernameField,
                    deleteButton,
                    cancelButton
            );
            layout.setPadding(new Insets(20));

            // Scene for modal
            Scene modalScene = new Scene(layout);
            modalStage.setScene(modalScene);
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.show();

            // Define button actions
            deleteButton.setOnAction(event -> {
                String username = usernameField.getText().trim();

                if (username.isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Input Error", "Username cannot be empty!");
                    return;
                }

                File file = new File("admins/" + username + ".txt");
                if (file.exists()) {
                    boolean success = admin.fireAdmin(username);
                    if (success) {
                        showAlert(Alert.AlertType.INFORMATION, "Success", "Admin fired successfully!");
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Error", "Failed to fire Admin!");
                    }
                } else {
                    showAlert(Alert.AlertType.ERROR, "Not Found", "No Admin with this username exists.");
                }

                modalStage.close();
            });

            cancelButton.setOnAction(event -> modalStage.close());
        });
    }
    public void updateAdminRank() {
        updateAdminRank.setOnAction(e -> {
            Stage modal = new Stage();
            modal.setTitle("Update Admin Rank");
            modal.initOwner(updateAdminRank.getScene().getWindow());

            VBox layout = new VBox(10);
            layout.setPadding(new Insets(10));
            layout.setAlignment(Pos.CENTER);

            TextField usernameInput = new TextField();
            usernameInput.setPromptText("Enter username");
            usernameInput.setPrefWidth(250);

            // Create and populate ComboBox with enum names
            ComboBox<String> rankComboBox = new ComboBox<>();
            for (AdminRank rank : AdminRank.values()) {
                rankComboBox.getItems().add(rank.name());  // Use the name of the enum constant
            }
            rankComboBox.setPromptText("Select a rank");

            // Buttons
            Button saveButton = new Button("Update Rank");
            Button cancelButton = new Button("Cancel");

            // Horizontal box for buttons
            HBox buttonBox = new HBox(10, saveButton, cancelButton);
            buttonBox.setAlignment(Pos.CENTER);

            // Add elements to layout
            layout.getChildren().addAll(
                    new Label("Update Admin Rank"),
                    usernameInput,
                    rankComboBox,
                    buttonBox
            );

            // Set scene and display modal
            Scene modalScene = new Scene(layout, 300, 200);
            modal.setScene(modalScene);
            modal.show();

            // Event handling for 'Save' button
            saveButton.setOnAction(event -> {
                String username = usernameInput.getText();
                String selectedRankString = rankComboBox.getValue();

                if (selectedRankString != null && !username.isEmpty()) {
                    try {
                        AdminRank selectedRank = AdminRank.valueOf(selectedRankString);

                        // Process selected rank if it is valid
                        File file = new File("admins/" + username + ".txt");
                        if (file.exists()) {
                            admin.updateAdminRank(username, selectedRank);
                            modal.close();
                            showAlert(Alert.AlertType.INFORMATION, "Update Success", "Rank updated successfully!");
                        } else {
                            showAlert(Alert.AlertType.ERROR, "Error", "Admin with this username does not exist!");
                        }
                    } catch (IllegalArgumentException ex) {
                        showAlert(Alert.AlertType.ERROR, "Invalid Rank", "Selected rank is invalid.");
                    }
                } else {
                    showAlert(Alert.AlertType.ERROR, "Validation Error", "Please fill all fields.");
                }
            });

            // Close modal on 'Cancel'
            cancelButton.setOnAction(event -> modal.close());
        });
    }


    // Method to show alerts
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(gmail|yahoo)\\.com$";
        return email.matches(emailRegex);
    }

    public boolean checkFileExists(String username) {
        String filePath = "admins/" + username + ".txt";

        File file = new File(filePath);
        return file.exists();
    }

    private boolean validateInputs(TextField name, TextField email, TextField id, TextField password, TextField address, TextField town, TextField city) {
        if (name.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Name cannot be empty.");
            return false;
        }

        if (email.getText().isEmpty() || !isValidEmail(email.getText())) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Invalid email format.");
            return false;
        }

        if (id.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "User ID cannot be empty.");
            return false;
        }

        if (password.getText().isEmpty() || password.getText().length() < 6) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Password must be at least 6 characters.");
            return false;
        }

        if (address.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Address cannot be empty.");
            return false;
        }

        if (town.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Town cannot be empty.");
            return false;
        }

        if (city.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "City cannot be empty.");
            return false;
        }

        return true;
    }
    public void setChangePassword() {
        changePassword.setOnAction(e -> {
            try {
                superAdmin admin = new superAdmin();
                String username = "Azzam1618";


                if (!admin.checkIfPrivateQuestionExists(username)) {
                    showAlert(Alert.AlertType.INFORMATION, "Security", "For security set private question & its answer first. But Be Careful you will not be able to change it later.");

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
                    save.setOnAction(q -> {
                        if (questionField.getText().isEmpty() || answerField.getText().isEmpty()) {
                            showAlert(Alert.AlertType.ERROR, "Error", "All Fields must be filled");
                        } else {
                            showAlert(Alert.AlertType.CONFIRMATION, "Setting Private Question", "You will not be able to change it later! Do you want to proceed?");
                            boolean success = admin.savePrivateQusetion(username, questionField.getText(), answerField.getText());
                            if (success) {
                                showAlert(Alert.AlertType.INFORMATION, "Setting Private Question", "Your Question and Answer has been saved suucessfully");
                                setPrivateQuestionWindow.close();
                            } else
                                showAlert(Alert.AlertType.ERROR, "Setting Private Question", "Save Failed");
                        }
                    });

                    setPrivateQuestionWindowLayout.getChildren().addAll(titleLabel, questionField, answerField, save);
                    Scene privateQuestionWindowScene = new Scene(setPrivateQuestionWindowLayout, 300, 200);
                    setPrivateQuestionWindow.setScene(privateQuestionWindowScene);
                    setPrivateQuestionWindow.showAndWait();
                }

                //Change Password
                Stage primaryStage = new Stage();
                // Create a Pane and VBox for layout
                VBox vbox = new VBox(10); // 10px spacing between elements
                vbox.setPrefSize(400, 250);
                vbox.setAlignment(Pos.CENTER);

                // Label: Update Password
                Label titleLabel = new Label("Update Password");
                titleLabel.setFont(Font.font("Times New Roman", 22));
                //Getting private Question
                String line;
                ArrayList<String> userData = new ArrayList<>();
                File file = new File("superadmin/" + username + ".txt");
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    while ((line = reader.readLine()) != null) {
                        userData.add(line);
                    }
                    reader.close();
                } catch (IOException ioException) {
                    ioException.getMessage();
                }
                // Label: Private Question
                Label privateQestionLabel = new Label(userData.get(2));
                privateQestionLabel.setFont(Font.font("System", 12));

                // Label: Enter new Password
                Label passwordLabel = new Label("Enter new Password:");
                passwordLabel.setFont(Font.font("System", 12));

                // TextField: Private Answer
                TextField privateAnswerField = new TextField();
                privateAnswerField.setPromptText("Answer");

                // TextField: New Password
                TextField newpasswordField = new TextField();
                newpasswordField.setPromptText("New Password");

                // Button: Update
                Button updateButton = new Button("Update");
                updateButton.setOnAction(event -> {
                    if (privateAnswerField.getText().isEmpty() || newpasswordField.getText().isEmpty()) {
                        showAlert(Alert.AlertType.ERROR, "Error", "All Fields must be Filled Correctly");
                    } else if (newpasswordField.getText().length() < 6) {
                        showAlert(Alert.AlertType.ERROR, "Error", "Password must be at least 6 characters");
                    } else {
                        boolean success = admin.updatePassword(username,privateAnswerField.getText(), newpasswordField.getText());
                        if (success) {
                            showAlert(Alert.AlertType.INFORMATION, "Password Updated", "Password Updated Successfully");
                        }
                    }
                });

                // Button: Cancel
                Button cancelButton = new Button("Cancel");
                cancelButton.setOnAction(event -> primaryStage.close());

                // Add all components to VBox
                vbox.getChildren().addAll(titleLabel, privateQestionLabel, privateAnswerField, passwordLabel, newpasswordField, updateButton, cancelButton);

                // Setting the Scene and Stage
                Scene scene = new Scene(vbox);
                primaryStage.setScene(scene);
                primaryStage.setTitle("Super Admin Change Password");
                primaryStage.initModality(Modality.APPLICATION_MODAL);
                primaryStage.show();

                }catch (Exception exception) {
                exception.printStackTrace();
            }
        });
    }


}

