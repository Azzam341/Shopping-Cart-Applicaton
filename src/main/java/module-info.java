module com.example.testingwithfx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.desktop;

    opens com.example.testingwithfx to javafx.fxml;
    exports com.example.testingwithfx;
}