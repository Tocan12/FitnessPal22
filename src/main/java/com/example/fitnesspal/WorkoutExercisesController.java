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

import static com.example.fitnesspal.DBUtils.getOrCreateWorkoutIdByName;
import static com.example.fitnesspal.DBUtils.getUserIdByUsername;


public class WorkoutExercisesController implements Initializable {
    @FXML
    private ComboBox<String> combo_workouts;

    @FXML
    private TableView<Exercise> table_exercises;

    @FXML
    private TextField txt_exerciseName;

    @FXML
    private TextField txt_sets;

    @FXML
    private TextField txt_reps;

    @FXML
    private TextField txt_idExercise;

    @FXML
    private TextField filterFieldExercise;

    @FXML
    private TextField txt_workoutName;

    @FXML
    private Button btn_addExercise;

    @FXML
    private Button btn_updateExercise;

    @FXML
    private Button btn_deleteExercise;

    @FXML
    private Button btn_createWorkout;

    @FXML
    private Button btn_workouts;

    @FXML
    private TableColumn<Exercise, Integer> col_idExercise;

    @FXML
    private TableColumn<Exercise, String> col_exerciseName;

    @FXML
    private TableColumn<Exercise, Integer> col_sets;

    @FXML
    private TableColumn<Exercise, Integer> col_reps;

        private ObservableList<Exercise> listM;
    private ObservableList<Exercise> dataList;

    String loggedInUsername = UserSession.getInstance().getLoggedInUsername();

    public void AddExercise() {
        try {
            int workoutId = getOrCreateWorkoutIdByName(combo_workouts.getValue());
            DBUtils.addWorkoutExercise(workoutId, txt_exerciseName.getText(), Integer.parseInt(txt_sets.getText()), Integer.parseInt(txt_reps.getText()));
            System.out.println("Exercise Add success");
            UpdateExerciseTable();
            search_exercise();  // Assuming you want to include a search functionality
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void UpdateExerciseTable() {
        col_idExercise.setCellValueFactory(new PropertyValueFactory<>("workoutExerciseId"));
        col_exerciseName.setCellValueFactory(new PropertyValueFactory<>("exerciseName"));
        col_sets.setCellValueFactory(new PropertyValueFactory<>("sets"));
        col_reps.setCellValueFactory(new PropertyValueFactory<>("reps"));

        listM = DBUtils.getWorkoutExerciseData(combo_workouts.getValue());
        table_exercises.setItems(listM);

        table_exercises.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                // Single click detected
                handleSingleClickExercise();
            }
        });

        // Add a double-click listener to the table
        table_exercises.setOnMousePressed(event -> {
            if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                // Double click detected
                handleDoubleClickExercise();
            }
        });
    }
    private void handleSingleClickExercise() {
        // Get the selected exercise item
        Exercise selectedExercise = table_exercises.getSelectionModel().getSelectedItem();

        if (selectedExercise != null) {
            // Populate the text fields with the selected exercise's information
            txt_idExercise.setText(String.valueOf(selectedExercise.getId()));
            txt_exerciseName.setText(selectedExercise.getExerciseName());
            txt_sets.setText(String.valueOf(selectedExercise.getSets()));
            txt_reps.setText(String.valueOf(selectedExercise.getReps()));
        }
    }
    private void handleDoubleClickExercise() {
        // Get the selected exercise item
        Exercise selectedExercise = table_exercises.getSelectionModel().getSelectedItem();

        if (selectedExercise != null) {
            // Get the selected workout from the ComboBox
            String selectedWorkout = combo_workouts.getValue();

            // Add the selected exercise item to the selected workout (implement this method in DBUtils)
            try {
                DBUtils.addExerciseToWorkout(selectedWorkout, selectedExercise.getId());

                // Show an alert indicating that the exercise has been added to the workout
                showExerciseAddedToWorkoutAlert();

                // Update the UI or perform other necessary actions
                UpdateExerciseTable();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void UpdateExercise() {
        try {
            // Retrieve information from text fields
            int workoutExerciseId = Integer.parseInt(txt_idExercise.getText());
            String exerciseName = txt_exerciseName.getText();
            int sets = Integer.parseInt(txt_sets.getText());
            int reps = Integer.parseInt(txt_reps.getText());

            // Call the Update method
            DBUtils.updateWorkoutExercise(workoutExerciseId, exerciseName, sets, reps);
            System.out.println("Exercise Update success");
            UpdateExerciseTable(); // Refresh the table after update
            // Additional logic specific to your use case, if any

        } catch (NumberFormatException | SQLException e) {
            e.printStackTrace();
        }
    }


    public void DeleteExercise() {
        try {
            // Retrieve the exercise ID from the text field and delete the corresponding record
            String workoutExerciseIdStr = txt_idExercise.getText();

            // Convert the workoutExerciseIdStr to an integer
            int workoutExerciseId = Integer.parseInt(workoutExerciseIdStr);

            // Call the Delete method with the correct data type
            DBUtils.deleteWorkoutExercise(workoutExerciseId);

            System.out.println("Exercise Delete success");
            UpdateExerciseTable(); // Refresh the table after deletion
            // Additional logic specific to your use case, if any

        } catch (NumberFormatException | SQLException e) {
            e.printStackTrace();
            // Handle the case where workoutExerciseIdStr is not a valid integer
        }
    }


    public void AddExerciseToWorkout() {
        try {
            // Retrieve the workout name from the input field
            String workoutName = txt_workoutName.getText();

            // Check if the workout name is empty
            if (workoutName.isEmpty()) {
                return;
            }

            // Use the workout name to get or create the workout_id
            int workoutId = getOrCreateWorkoutIdByName(workoutName);

            // Now, you can associate exercise items with the retrieved/created workout_id
            // For demonstration purposes, let's assume you have a selectedExerciseId variable representing the selected exercise
            int selectedExerciseId = Integer.parseInt(txt_idExercise.getText());
            DBUtils.addWorkoutExercise(workoutId, txt_exerciseName.getText(), Integer.parseInt(txt_sets.getText()), Integer.parseInt(txt_reps.getText()));

            // Show an alert indicating that the exercise has been added to the workout
            showExerciseAddedToWorkoutAlert();

            // Update the UI or perform other necessary actions
            UpdateExerciseTable();

            // Refresh the workout ComboBox
            refreshWorkoutComboBox();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void search_exercise() {
        col_idExercise.setCellValueFactory(new PropertyValueFactory<>("workoutExerciseId"));
        col_exerciseName.setCellValueFactory(new PropertyValueFactory<>("exerciseName"));
        col_sets.setCellValueFactory(new PropertyValueFactory<>("sets"));
        col_reps.setCellValueFactory(new PropertyValueFactory<>("reps"));

        dataList = DBUtils.getWorkoutExerciseData(combo_workouts.getValue());
        table_exercises.setItems(dataList);

        FilteredList<Exercise> filteredData = new FilteredList<>(dataList, b -> true);
        filterFieldExercise.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(exercise -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                return exercise.getExerciseName().toLowerCase().contains(lowerCaseFilter)
                        || String.valueOf(exercise.getSets()).contains(lowerCaseFilter)
                        || String.valueOf(exercise.getReps()).contains(lowerCaseFilter);

            });
        });

        SortedList<Exercise> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(table_exercises.comparatorProperty());
        table_exercises.setItems(sortedData);
    }



    private void refreshWorkoutComboBox() {
        populateWorkoutComboBox();
    }

    private void populateWorkoutComboBox() {
        String loggedInUsername = UserSession.getInstance().getLoggedInUsername();
        int userId = getUserIdByUsername(loggedInUsername);

        ObservableList<String> workoutNames = null;
        workoutNames = DBUtils.getWorkoutNamesForUser(userId);
        combo_workouts.setItems(workoutNames);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        populateWorkoutComboBox();
        btn_addExercise.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                AddExercise();
            }
        });

        btn_updateExercise.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                UpdateExercise();
            }
        });

        btn_deleteExercise.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DeleteExercise();
            }
        });

        btn_createWorkout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                AddExerciseToWorkout();
            }
        });

        btn_workouts.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeSceneMain(event, "workouts_page.fxml", "Workouts!", loggedInUsername);
            }
        });

    }
    private void showExerciseAddedToWorkoutAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Exercise Added to Workout");
        alert.setHeaderText(null);
        alert.setContentText("Exercise has been added to the workout successfully!");
        alert.showAndWait();
    }
}
