package com.example.fitnesspal;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class LoggedInController implements Initializable {

    @FXML
    private Button button_logOUT;
    @FXML
    private Button button_createProfile;
    @FXML private Label label_welcome;
    @FXML private Label label_style;
    @FXML private Button btNo;

    // Variable to store the username
    private String username;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        button_logOUT.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                DBUtils.changeScene(actionEvent, "fitnesslog.fxml", "Log IN!", null, null );
            }
        });
        button_createProfile.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Pass the username to the createProfile method
                DBUtils.createProfile(event, username);
            }
        });
        btNo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event, "main_page.fxml", "Main", null, null);
            }
        });

    }

    public void setUserInformation(String username, String favSport) {
        // Set the username variable
        this.username = username;

        label_welcome.setText("Welcome " + username + "!");
        label_style.setText("Your fitness style is " + favSport + "!");
    }
}
