package com.example.fitnesspal;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class MainPageController implements Initializable {
@FXML
private Label lb_user;
@FXML
private Button btn_profile;
    @FXML
    private Button btn_nutrition;
    @FXML
    private Button btn_fitness;
    @FXML
    private Button btn_logout;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
btn_profile.setOnAction(new EventHandler<ActionEvent>() {
    @Override
    public void handle(ActionEvent event) {
DBUtils.changeSceneMain(event,"view_profile.fxml","delete",null);
    }
});
btn_nutrition.setOnAction(new EventHandler<ActionEvent>() {
    @Override
    public void handle(ActionEvent event) {
DBUtils.changeSceneMain(event,"nutrition_page.fxml", null, null);
    }
});
btn_fitness.setOnAction(new EventHandler<ActionEvent>() {
    @Override
    public void handle(ActionEvent event) {
        DBUtils.changeSceneMain(event,"workout_exercises.fxml","wow",null);
    }
});
btn_logout.setOnAction(new EventHandler<ActionEvent>() {
    @Override
    public void handle(ActionEvent event) {
        DBUtils.changeSceneMain(event,"hello-view.fxml",null,null);
    }
});
    }
    public void setUserInformation(String username) {
        UserSession.getInstance().setLoggedInUsername(username);
        lb_user.setText("Welcome to FitnessPal, " + username + "!");
    }

}
