
        package com.example.fitnesspal;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static com.example.fitnesspal.DBUtils.getOrCreateMealIdByName;
import static com.example.fitnesspal.DBUtils.getUserIdByUsername;

        public class NutritionController implements Initializable {
    @FXML
    private ComboBox<String> combo_meals;
    @FXML
    private TableView<food> table_food;
    @FXML
    private TextField txt_mealName;
    @FXML
    private Button btn_back;

@FXML
private Button btn_meals;
    @FXML
    private TableColumn<food, Integer> col_id;

    @FXML
    private TableColumn<food, String> col_food;

    @FXML
    private TableColumn<food, String> col_cal;

    @FXML
    private TableColumn<food, String> col_carbs;

    @FXML
    private TableColumn<food, String> col_prot;

    @FXML
    private TableColumn<food, String> col_fat;

    @FXML
    private TextField txt_id;
    @FXML
    private TextField txt_food;

    @FXML
    private TextField txt_cal;

    @FXML
    private TextField txt_protein;

    @FXML
    private TextField txt_carbs;

    @FXML
    private TextField txt_fat;

    @FXML
    private TextField filterField;

    private ObservableList<food> listM;
    private ObservableList<food> dataList;

            String loggedInUsername = UserSession.getInstance().getLoggedInUsername();
    public void Add_food() {
        try {
            DBUtils.addFood(txt_food.getText(), txt_cal.getText(), txt_protein.getText(), txt_carbs.getText(), txt_fat.getText());
            System.out.println("Food Add success");
            UpdateFoodTable();
            search_food();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void UpdateFoodTable() {
        col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_food.setCellValueFactory(new PropertyValueFactory<>("food_name"));
        col_cal.setCellValueFactory(new PropertyValueFactory<>("calories"));
        col_prot.setCellValueFactory(new PropertyValueFactory<>("protein"));
        col_carbs.setCellValueFactory(new PropertyValueFactory<>("carbs"));
        col_fat.setCellValueFactory(new PropertyValueFactory<>("fat"));

        listM = DBUtils.getFoodData();
        table_food.setItems(listM);

        table_food.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                // Single click detected
                handleSingleClick();
            }
        });

        // Add a double-click listener to the table
        table_food.setOnMousePressed(event -> {
            if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                // Double click detected
                handleDoubleClick();
            }
        });
    }

            private void handleSingleClick() {
                // Get the selected food item
                food selectedFood = table_food.getSelectionModel().getSelectedItem();

                if (selectedFood != null) {
                    // Populate the text fields with the selected food's information
                    txt_id.setText(String.valueOf(selectedFood.getId()));
                    txt_food.setText(selectedFood.getFood_name());
                    txt_cal.setText(selectedFood.getCalories());
                    txt_protein.setText(selectedFood.getProtein());
                    txt_carbs.setText(selectedFood.getCarbs());
                    txt_fat.setText(selectedFood.getFat());
                }
            }

            private void handleDoubleClick() {
                // Get the selected food item
                food selectedFood = table_food.getSelectionModel().getSelectedItem();

                if (selectedFood != null) {
                    // Get the selected meal from the ComboBox
                    String selectedMeal = combo_meals.getValue();

                    // Add the selected food item to the selected meal
                    try {
                        DBUtils.addFoodToMeal(selectedMeal, selectedFood.getId());

                        // Show an alert indicating that the food has been added to the meal
                        showFoodAddedToMealAlert();


                        UpdateFoodTable();
                        search_food();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
    public void UpdateFood () {
        try {
            // Retrieve information from text fields
            int foodId = Integer.parseInt(txt_id.getText());
            String foodName = txt_food.getText();
            String calories = txt_cal.getText();
            String protein = txt_protein.getText();
            String carbs = txt_carbs.getText();
            String fat = txt_fat.getText();

            // Call the Update method
            DBUtils.Update(foodId, foodName, calories, protein, carbs, fat);
            System.out.println("Food Update success");
            UpdateFoodTable(); // Refresh the table after update
            search_food();
        } catch (NumberFormatException | SQLException e) {
            e.printStackTrace();
        }
    }



    public void DeleteFood() {
        // Retrieve the food ID from the text field and delete the corresponding record
        String foodIdStr = txt_id.getText();

        try {
            // Convert the foodIdStr to an integer
            int foodId = Integer.parseInt(foodIdStr);

            // Call the Delete method with the correct data type
            DBUtils.Delete(foodId);

            System.out.println("Food Delete success");
            UpdateFoodTable(); // Refresh the table after deletion
            search_food();
        } catch (NumberFormatException e) {
            e.printStackTrace();
            // Handle the case where foodIdStr is not a valid integer
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void search_food() {
        col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_food.setCellValueFactory(new PropertyValueFactory<>("food_name"));
        col_cal.setCellValueFactory(new PropertyValueFactory<>("calories"));
        col_prot.setCellValueFactory(new PropertyValueFactory<>("protein"));
        col_carbs.setCellValueFactory(new PropertyValueFactory<>("carbs"));
        col_fat.setCellValueFactory(new PropertyValueFactory<>("fat"));

        dataList = DBUtils.getFoodData();
        table_food.setItems(dataList);

        FilteredList<food> filteredData = new FilteredList<>(dataList, b -> true);
        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(food -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                return food.getFood_name().toLowerCase().contains(lowerCaseFilter)
                        || food.getProtein().toLowerCase().contains(lowerCaseFilter)
                        || food.getCarbs().toLowerCase().contains(lowerCaseFilter)
                        || food.getFat().toLowerCase().contains(lowerCaseFilter)
                        || String.valueOf(food.getCalories()).contains(lowerCaseFilter);
            });
        });

        SortedList<food> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(table_food.comparatorProperty());
        table_food.setItems(sortedData);
    }
            public void Add_food_to_meal() {
                try {
                    // Retrieve the meal name from the input field
                    String mealName = txt_mealName.getText();

                    // Check if the meal name is empty
                    if (mealName.isEmpty()) {
                        return;
                    }

                    // Use the meal name to get or create the meal_id
                    int mealId = getOrCreateMealIdByName(mealName);
                    int selectedFoodId = Integer.parseInt(txt_id.getText());
                    DBUtils.addFoodToMeal(mealName, selectedFoodId);

                    // Show an alert indicating that the meal has been created
                    showMealCreatedAlert();

                    // Update the UI or perform other necessary actions
                    UpdateFoodTable();
                    search_food();

                    // Refresh the meal ComboBox
                    refreshMealComboBox();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            private void refreshMealComboBox() {
                populateMealComboBox();
            }



                private void populateMealComboBox() {
                    String loggedInUsername = UserSession.getInstance().getLoggedInUsername();
                    int userId = getUserIdByUsername(loggedInUsername);

                    ObservableList<String> mealNames = DBUtils.getMealNamesForUser(userId);
                    combo_meals.setItems(mealNames);
                }




            @Override
    public void initialize(URL url, ResourceBundle rb) {
        UpdateFoodTable();
        search_food();
        populateMealComboBox();
        btn_meals.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeSceneMain(event,"meals_page.fxml","Meals!",loggedInUsername);
            }
        });
        btn_back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeSceneMain(event,"main_page.fxml",null,null);
            }
        });

    }
            public void showMealCreatedAlert() {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Meal Created");
                alert.setHeaderText(null);
                alert.setContentText("Meal has been created successfully!");
                alert.showAndWait();
            }
            private void showFoodAddedToMealAlert() {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Food Added to Meal");
                alert.setHeaderText(null);
                alert.setContentText("Food has been added to the meal successfully!");
                alert.showAndWait();
            }

        }