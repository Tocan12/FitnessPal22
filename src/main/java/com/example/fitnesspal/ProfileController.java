package com.example.fitnesspal;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.sql.SQLException;

public class ProfileController {

    @FXML
    private Button btnDeleteProfile;
    @FXML
    private Button btn_back;

    @FXML
    private Label labelFullName;
    @FXML
    private Label labelGender;
    @FXML
    private Label labelBirthdate;
    @FXML
    private Label labelHeight;
    @FXML
    private Label labelWeight;

    @FXML
    void initialize() {
        // Set up event handler for "Delete Profile" button
        btnDeleteProfile.setOnAction(this::handleDeleteProfile);


        // Load user profile information and populate labels
        loadUserProfileInfo();
        btn_back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeSceneMain(event,"main_page.fxml","Hei",null);
            }
        });
    }

    private void loadUserProfileInfo() {
        try {
            // Retrieve user profile information
            UserProfile userProfile = DBUtils.getUserProfile(UserSession.getInstance().getLoggedInUsername());

            // Populate labels with user profile information
            labelFullName.setText(userProfile.getFullName());
            labelGender.setText(userProfile.getGender());
            labelBirthdate.setText(userProfile.getBirthdate().toString());
            labelHeight.setText(String.valueOf(userProfile.getHeight()));
            labelWeight.setText(String.valueOf(userProfile.getWeight()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void handleDeleteProfile(ActionEvent event) {
        try {
            // Delete the user profile
            DBUtils.deleteUserProfile(UserSession.getInstance().getLoggedInUsername());

            // Show an alert indicating successful profile deletion
            showProfileDeletedAlert();


            DBUtils.changeSceneMain(event, "create-profile.fxml", "Create Profile",null);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showProfileDeletedAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Profile Deleted");
        alert.setHeaderText(null);
        alert.setContentText("Your profile has been successfully deleted.");
        alert.showAndWait();
    }
}
