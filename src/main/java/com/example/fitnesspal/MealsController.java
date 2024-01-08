package com.example.fitnesspal;


import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class MealsController implements Initializable {

    @FXML
    private TableView<Meal> tableMeals;

    @FXML
    private TableColumn<Meal, Integer> colMealId;

    @FXML
    private TableColumn<Meal, String> colMealName;

    @FXML
    private TableColumn<Meal, Integer> colTotalCalories;
    @FXML
    private Button btn_back;

    private ObservableList<Meal> mealsList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize columns
        colMealId.setCellValueFactory(cellData -> cellData.getValue().mealIdProperty().asObject());
        colMealName.setCellValueFactory(cellData -> cellData.getValue().mealNameProperty());
        colTotalCalories.setCellValueFactory(cellData -> cellData.getValue().totalCaloriesProperty().asObject());

        // Load meals data for the current user
        loadMealsData();
        btn_back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeSceneMain(event,"nutrition_page.fxml",null,null);
            }
        });
    }

    private void loadMealsData() {
        try {
            // Retrieve meals data for the current user from the database
            int userId = UserSession.getInstance().getLoggedInUserId();
            mealsList = DBUtils.getMealsDataForUser(userId);

            // Populate the table with meals data
            tableMeals.setItems(mealsList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void deleteMeal() {
        // Get the selected meal
        Meal selectedMeal = tableMeals.getSelectionModel().getSelectedItem();

        if (selectedMeal != null) {
            try {
                // Delete the selected meal from the database
                DBUtils.deleteMeal(selectedMeal.getMealId());

                // Refresh the table after deletion
                loadMealsData();

                // Show an alert indicating successful deletion
                showDeleteMealAlert();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            // Show an alert if no meal is selected
            showNoMealSelectedAlert();
        }
    }

    private void showDeleteMealAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Meal Deleted");
        alert.setHeaderText(null);
        alert.setContentText("Meal has been deleted successfully!");
        alert.showAndWait();
    }

    private void showNoMealSelectedAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("No Meal Selected");
        alert.setHeaderText(null);
        alert.setContentText("Please select a meal to delete.");
        alert.showAndWait();
    }
}
