
package com.example.fitnesspal;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

    public class SignUpController implements Initializable {
        @FXML
        private Button button_log_in;
        @FXML
        private Button button_signup;
        @FXML
        private TextField tf_username;

        @FXML
        private TextField tf_password;

        @FXML
        private RadioButton rb_power;

        @FXML
        private RadioButton rb_cali;

        @FXML
        private RadioButton rb_moving;

        @Override
        public void initialize(URL url, ResourceBundle resourceBundle) {
            ToggleGroup toggleGroup= new ToggleGroup();
            rb_power.setToggleGroup(toggleGroup);
            rb_cali.setToggleGroup(toggleGroup);
            rb_moving.setToggleGroup(toggleGroup);
            rb_power.setSelected(true);


            button_signup.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    String toggleName= ((RadioButton) toggleGroup.getSelectedToggle()).getText();
                    if(!tf_username.getText().trim().isEmpty() && !tf_password.getText().trim().isEmpty())
                    {
                        DBUtils.signUpUser(event,tf_username.getText(), tf_password.getText(),toggleName);
                    }
                    else{
                        System.out.println("Please fill info!");
                        Alert alert= new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Please fill all info to signUp");
                        alert.show();
                    }

                }
            });
            button_log_in.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    DBUtils.changeScene(event,"fitnesslog.fxml","Log in",null, null);

                }
            });
        }

    }