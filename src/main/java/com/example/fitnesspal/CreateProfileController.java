package com.example.fitnesspal;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class CreateProfileController implements Initializable {

    @FXML
    private ChoiceBox<String> cbGender;
    @FXML
    private Button backButton;
    @FXML
    private Button btnSaveProfile;
    @FXML
    private TextField tfFullName;
    @FXML
    private TextField tfUser;
    @FXML
    private DatePicker dpBirthdate;
    @FXML
    private TextField tfHeight;
    @FXML
    private TextField tfWeight;
    @FXML
    private String loggedInUsername;
    @FXML
    private Label label_profile;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        backButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event,"fitnesslog.fxml","Log in!", null,null);
            }
        });


        btnSaveProfile.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String heightStr = tfHeight.getText();
                String weightStr = tfWeight.getText();
                String fullName = tfFullName.getText();
                if (heightStr.isEmpty() || weightStr.isEmpty()) {
                    showAlert("Height and weight cannot be empty.");
                    return;
                }

                try {
                    double height = Double.parseDouble(heightStr);
                    double weight = Double.parseDouble(weightStr);
                    String gender = (String) cbGender.getValue();
                    Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5433/Fitness", "postgres", "darknebula22");

                    // Get the logged-in user's ID
                    int userId = DBUtils.getUserIdByUsername(connection, tfUser.getText());
                    System.out.println("Logged In Username: " +  tfUser.getText());
                    System.out.println("User ID: " + userId);

                    if (userId != -1) {
                        System.out.println("User ID: " + userId);  // Add this line for debugging
                        DBUtils.saveUserProfile(userId, fullName, gender, Date.valueOf(dpBirthdate.getValue()), height, weight);
                        showAlert("Profile created successfully!");
                        DBUtils.changeScene(event, "logged-in.fxml", "New Page Title", tfUser.getText(), null);
                    } else {
                        showAlert("User not found.");
                    }

                    connection.close();
                } catch (NumberFormatException | SQLException e) {
                    e.printStackTrace();
                    showAlert("Error creating profile. Please check your input.");
                }

            }
        });


        backButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event, "fitnesslog.fxml", "Log in!", null, null);
            }
        });
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setLoggedInUsername(String username) {
        this.loggedInUsername = username;
        label_profile.setText("Create your profile, " + username);
    }}
