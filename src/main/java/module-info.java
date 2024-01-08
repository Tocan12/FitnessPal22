module com.example.fitnesspal {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.fitnesspal to javafx.fxml;
    exports com.example.fitnesspal;
}