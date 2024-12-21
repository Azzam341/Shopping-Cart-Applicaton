package com.example.testingwithfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
      Parent root = FXMLLoader.load(getClass().getResource("StartPage.fxml"));
      primaryStage.setScene(new javafx.scene.Scene(root));
      primaryStage.setTitle("SHOPPING CART APPLICATION");
      primaryStage.show();
    }

    public static void main(String[] args) {
        Admin admin = new Admin();
        admin.loadInventoryFromFiles();
        Customer customer = new Customer();
        launch(args);
    }
}
