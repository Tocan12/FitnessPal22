package com.example.fitnesspal;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;


public class DBUtils {
    public static void changeScene(ActionEvent event, String fxmlFile, String title, String username, String favSport) {
        Parent root = null;
        try {
            FXMLLoader loader = new FXMLLoader(DBUtils.class.getResource(fxmlFile));
            root = loader.load();

            if (username != null && favSport != null) {
                // If the target controller supports setLoggedInUsername method
                if (loader.getController() instanceof CreateProfileController) {
                    CreateProfileController createProfileController = loader.getController();
                    createProfileController.setLoggedInUsername(username);
                }
            }

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle(title);
            stage.setScene(new Scene(root, 600, 400));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void changeSceneMain(ActionEvent event, String fxmlFile, String title, String username) {
        Parent root = null;
        try {
            FXMLLoader loader = new FXMLLoader(DBUtils.class.getResource(fxmlFile));
            root = loader.load();

            if (username != null) {
                if (loader.getController() instanceof MainPageController) {
                    MainPageController mainPageController = loader.getController();
                    mainPageController.setUserInformation(username);
                }
            }

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle(title);
            stage.setScene(new Scene(root, 600, 400));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void changeScenelog(ActionEvent event, String fxmlFile, String title, String username, String favSport) {
        Parent root = null;
        try {
            FXMLLoader loader = new FXMLLoader(DBUtils.class.getResource(fxmlFile));
            root = loader.load();

            if (username != null && favSport != null) {

                if (loader.getController() instanceof LoggedInController) {
                    LoggedInController loggedInController = loader.getController();
                    loggedInController.setUserInformation(username,favSport);
                }
            }

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle(title);
            stage.setScene(new Scene(root, 1200, 700));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void saveUserProfile(int userId, String fullName, String gender, java.sql.Date birthdate, double height, double weight) {
        Connection connection = null;
        PreparedStatement psCheckProfileExists = null;
        PreparedStatement psInsertProfile = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5433/Fitness", "postgres", "darknebula22");

            // Check if a profile already exists for the user
            psCheckProfileExists = connection.prepareStatement("SELECT * FROM profile WHERE user_id = ?");
            psCheckProfileExists.setInt(1, userId);
            resultSet = psCheckProfileExists.executeQuery();

            if (resultSet.isBeforeFirst()) {
                System.out.println("Profile already exists for the user!");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Profile already exists for the user");
                alert.show();
            } else {
                // Insert the new profile
                psInsertProfile = connection.prepareStatement("INSERT INTO profile (user_id, full_name, gender, birthdate, height, weight) VALUES (?, ?, ?, ?, ?, ?)");
                psInsertProfile.setInt(1, userId);
                psInsertProfile.setString(2, fullName);
                psInsertProfile.setString(3, gender);
                psInsertProfile.setDate(4, birthdate);
                psInsertProfile.setDouble(5, height);
                psInsertProfile.setDouble(6, weight);
                psInsertProfile.executeUpdate();

            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error saving profile. Please try again.");
        } finally {
            closeResultSet(resultSet);
            closeStatement(psCheckProfileExists);
            closeStatement(psInsertProfile);

        }
    }
    private static void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }



    public static void signUpUser(ActionEvent event, String username, String password, String favSport) {
        Connection connection = null;
        PreparedStatement psInsert = null;
        PreparedStatement psCheckUserExists = null;
        ResultSet resultSet = null;

        try {

            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5433/Fitness", "postgres", "darknebula22");
            psCheckUserExists = connection.prepareStatement("SELECT * FROM users WHERE username=?");
            psCheckUserExists.setString(1, username);
            resultSet = psCheckUserExists.executeQuery();
            if (resultSet.isBeforeFirst()) {
                System.out.println("User already exists!");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("You cannot use this username");
                alert.show();
            } else {
                psInsert = connection.prepareStatement("INSERT INTO users(username,password,favsport) VALUES(?, ?, ?)");
                psInsert.setString(1, username);
                psInsert.setString(2, password);
                psInsert.setString(3, favSport);
                psInsert.executeUpdate();
                changeScenelog(event, "logged-in.fxml", "Welcome!", username, favSport);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (psCheckUserExists != null) {
                try {
                    psCheckUserExists.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (psInsert != null) {
                try {
                    psInsert.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static int getUserIdByUsername(Connection connection, String username) {
        PreparedStatement psGetUserId = null;
        ResultSet resultSet = null;
        int userId = -1;

        try {
            psGetUserId = connection.prepareStatement("SELECT user_id FROM users WHERE username = ?");
            psGetUserId.setString(1, username);
            resultSet = psGetUserId.executeQuery();

            if (resultSet.next()) {
                userId = resultSet.getInt("user_id");
            } else {
                System.out.println("User not found for username: " + username);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResultSet(resultSet);
            closeStatement(psGetUserId);
        }

        return userId;
    }

    private static void closeResultSet(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static void closeStatement(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public static void createProfile(ActionEvent event, String username) {
        changeScene(event, "create-profile.fxml", "Create Profile", username, null);
    }
    public static void logInUser(ActionEvent event, String username, String password) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5433/Fitness", "postgres", "darknebula22");
            preparedStatement = connection.prepareStatement("SELECT user_id, password, favSport FROM users WHERE username = ?");
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int userId = resultSet.getInt("user_id");
                String retrievedPassword = resultSet.getString("password");
                String retrievedSport = resultSet.getString("favsport");

                if (retrievedPassword.equals(password)) {
                    // Set the user ID in the UserSession
                    UserSession.getInstance().setLoggedInUserId(userId);

                    // Check if the user has a profile
                    if (hasProfile(connection, username)) {
                        // Redirect to main_page.fxml
                        changeSceneMain(event, "main_page.fxml", "Main Page", username);
                    } else {
                        // Redirect to create-profile.fxml
                        createProfile(event, username);
                    }
                } else {
                    showAlert("Passwords did not match!");
                }
            } else {
                showAlert("User not found in the database!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResultSet(resultSet);
            closeStatement(preparedStatement);
        }
    }

    public static void addFood(String foodName, String calories, String protein, String carbs, String fat) throws SQLException {
        Connection connection = null;
        PreparedStatement pst = null;

        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5433/Fitness", "postgres", "darknebula22");

            String sql = "INSERT INTO foods (food_name, calories_per_serving, protein, carbohydrates, fat) VALUES (?, ?, ?, ?, ?)";
            pst = connection.prepareStatement(sql);
            pst.setString(1, foodName);
            pst.setString(2, calories);
            pst.setString(3, protein);
            pst.setString(4, carbs);
            pst.setString(5, fat);
            pst.executeUpdate();
        } finally {
            closeStatement(pst);

        }
    }
    public static void addWorkoutExercise(int workoutId, String exerciseName, int sets, int reps) throws SQLException {
        Connection connection = null;
        PreparedStatement pst = null;

        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5433/Fitness", "postgres", "darknebula22");

            String sql = "INSERT INTO workout_exercises (workout_id, exercise_name, sets, reps) VALUES (?, ?, ?, ?)";
            pst = connection.prepareStatement(sql);
            pst.setInt(1, workoutId);
            pst.setString(2, exerciseName);
            pst.setInt(3, sets);
            pst.setInt(4, reps);
            pst.executeUpdate();
        } finally {
            closeStatement(pst);
        }
    }

    public static void Edit(String foodName, String calories, String protein, String carbs, String fat) throws SQLException {
        Connection connection = null;
        PreparedStatement pst = null;

        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5433/Fitness", "postgres", "darknebula22");
            String sql = "UPDATE foods SET calories_per_serving=?, protein=?, carbohydrates=?, fat=? WHERE food_name=?";
            pst = connection.prepareStatement(sql);
            pst.setString(1, calories);
            pst.setString(2, protein);
            pst.setString(3, carbs);
            pst.setString(4, fat);
            pst.setString(5, foodName);
            pst.executeUpdate();
        } finally {
            closeStatement(pst);
        }
    }

    public static ObservableList<food> getFoodData() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        ObservableList<food> list = FXCollections.observableArrayList();

        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5433/Fitness", "postgres", "darknebula22");
            preparedStatement = connection.prepareStatement("SELECT * FROM foods");
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                list.add(new food(
                        Integer.parseInt(resultSet.getString("food_id")),
                        resultSet.getString("food_name"),
                        resultSet.getString("calories_per_serving"),
                        resultSet.getString("protein"),
                        resultSet.getString("carbohydrates"),
                        resultSet.getString("fat")));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResultSet(resultSet);
            closeStatement(preparedStatement);

        }

        return list;
    }
    public static ObservableList<Exercise> getWorkoutExerciseData(String workoutName) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        ObservableList<Exercise> list = FXCollections.observableArrayList();

        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5433/Fitness", "postgres", "darknebula22");
            preparedStatement = connection.prepareStatement("SELECT * FROM workout_exercises WHERE workout_id = (SELECT workout_id FROM workouts WHERE workout_name = ?)");
            preparedStatement.setString(1, workoutName);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                list.add(new Exercise(
                        Integer.parseInt(resultSet.getString("workout_exercise_id")),
                        resultSet.getString("exercise_name"),
                        resultSet.getString("sets"),
                      resultSet.getString("reps")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResultSet(resultSet);
            closeStatement(preparedStatement);
        }

        return list;
    }

    public static int getUserIdByUsername(String username) {
        Connection connection = null;
        PreparedStatement psGetUserId = null;
        ResultSet resultSet = null;
        int userId = -1;

        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5433/Fitness", "postgres", "darknebula22");
            psGetUserId = connection.prepareStatement("SELECT user_id FROM users WHERE username = ?");
            psGetUserId.setString(1, username);
            resultSet = psGetUserId.executeQuery();

            if (resultSet.next()) {
                userId = resultSet.getInt("user_id");
            } else {
                System.out.println("User not found for username: " + username);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResultSet(resultSet);
            closeStatement(psGetUserId);
        }

        return userId;
    }

    private static boolean hasProfile(Connection connection, String username) throws SQLException {
        PreparedStatement psCheckProfileExists = connection.prepareStatement("SELECT * FROM profile WHERE user_id = (SELECT user_id FROM users WHERE username = ?)");
        psCheckProfileExists.setString(1, username);
        ResultSet resultSet = psCheckProfileExists.executeQuery();

        return resultSet.isBeforeFirst();
    }
    public static void Delete(int foodId) throws SQLException {
        Connection connection = null;
        PreparedStatement pst = null;

        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5433/Fitness", "postgres", "darknebula22");
            String sql = "DELETE FROM foods WHERE food_id = ?";
            pst = connection.prepareStatement(sql);
            pst.setInt(1, foodId); // Set the parameter as an integer
            pst.executeUpdate();
        } finally {
            closeStatement(pst);
        }
    }
    public static void deleteWorkoutExercise(int workoutExerciseId) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5433/Fitness", "postgres", "darknebula22");

            String sql = "DELETE FROM workout_exercises WHERE workout_exercise_id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, workoutExerciseId); // Set the parameter as an integer
            preparedStatement.executeUpdate();
        } finally {
            closeStatement(preparedStatement);
        }
    }

    public static void Update(int foodId, String foodName, String calories, String protein, String carbs, String fat) throws SQLException {
        Connection connection = null;
        PreparedStatement pst = null;

        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5433/Fitness", "postgres", "darknebula22");
            String sql = "UPDATE foods SET food_name=?, calories_per_serving=?, protein=?, carbohydrates=?, fat=? WHERE food_id=?";
            pst = connection.prepareStatement(sql);
            pst.setString(1, foodName);
            pst.setString(2, calories);
            pst.setString(3, protein);
            pst.setString(4, carbs);
            pst.setString(5, fat);
            pst.setInt(6, foodId);
            pst.executeUpdate();
        } finally {
            closeStatement(pst);
        }
    }
    public static void updateWorkoutExercise(int workoutExerciseId, String exerciseName, int sets, int reps) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5433/Fitness", "postgres", "darknebula22");

            String sql = "UPDATE workout_exercises SET exercise_name=?, sets=?, reps=? WHERE workout_exercise_id=?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, exerciseName);
            preparedStatement.setInt(2, sets);
            preparedStatement.setInt(3, reps);
            preparedStatement.setInt(4, workoutExerciseId);

            preparedStatement.executeUpdate();
        } finally {
            closeStatement(preparedStatement);
        }
    }



    public static void addFoodToMeal(String mealName, int foodId) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5433/Fitness", "postgres", "darknebula22");

            // Retrieve the meal_id based on the meal name
            int mealId = getMealIdByName(mealName);

            if (mealId != -1) {
                // Get the calories of the food being added
                int foodCalories = getFoodCaloriesById(foodId);

                // Add the food item to the meal_items table
                String sql = "INSERT INTO meal_items (meal_id, food_id, quantity) VALUES (?, ?, ?)";
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, mealId);
                preparedStatement.setInt(2, foodId);
                preparedStatement.setInt(3, 1);
                preparedStatement.executeUpdate();

                // Update the total calories of the meal
                updateMealTotalCalories(mealId, foodCalories);
            } else {
                // Handle the case where the meal does not exist
                System.out.println("Meal not found.");
            }
        } finally {
            closeStatement(preparedStatement);
        }
    }
    public static void addExerciseToWorkout(String workoutName, int exerciseId) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5433/Fitness", "postgres", "darknebula22");

            // Retrieve the workout_id based on the workout name
            int workoutId = getWorkoutIdByName(workoutName);

            if (workoutId != -1) {

                String sql = "INSERT INTO workout_exercises (workout_id, exercise_id, sets, reps) VALUES (?, ?, ?, ?)";
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, workoutId);
                preparedStatement.setInt(2, exerciseId);
                preparedStatement.setInt(3, 3); // Example: sets
                preparedStatement.setInt(4, 10); // Example: reps
                preparedStatement.executeUpdate();


            } else {
                // Handle the case where the workout does not exist
                System.out.println("Workout not found.");
            }
        } finally {
            closeStatement(preparedStatement);
        }
    }

    private static int getFoodCaloriesById(int foodId) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int calories = 0;

        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5433/Fitness", "postgres", "darknebula22");
            preparedStatement = connection.prepareStatement("SELECT calories_per_serving FROM foods WHERE food_id = ?");
            preparedStatement.setInt(1, foodId);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                calories = resultSet.getInt("calories_per_serving");
            }
        } finally {
            closeResultSet(resultSet);
            closeStatement(preparedStatement);
        }

        return calories;
    }

    private static void updateMealTotalCalories(int mealId, int foodCalories) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5433/Fitness", "postgres", "darknebula22");

            // Update the total calories of the meal
            String sql = "UPDATE meals SET total_calories = total_calories + ? WHERE meal_id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, foodCalories);
            preparedStatement.setInt(2, mealId);
            preparedStatement.executeUpdate();
        } finally {
            closeStatement(preparedStatement);
        }
    }
    public static ObservableList<String> getMealNamesForUser(int userId) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        ObservableList<String> mealNames = FXCollections.observableArrayList();

        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5433/Fitness", "postgres", "darknebula22");
            preparedStatement = connection.prepareStatement("SELECT meal_name FROM meals WHERE user_id = ?");
            preparedStatement.setInt(1, userId);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                mealNames.add(resultSet.getString("meal_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResultSet(resultSet);
            closeStatement(preparedStatement);
        }

        return mealNames;
    }


    public static int createMeal(String mealName) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int mealId = -1;

        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5433/Fitness", "postgres", "darknebula22");

            // Insert the new meal with user_id
            int userId = getUserIdByUsername(connection, UserSession.getInstance().getLoggedInUsername());
            preparedStatement = connection.prepareStatement("INSERT INTO meals (user_id, meal_name, date, total_calories) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, userId);
            preparedStatement.setString(2, mealName);
            // You might want to set a default date or total calories based on your requirements
            preparedStatement.setDate(3, Date.valueOf(LocalDate.now()));
            preparedStatement.setInt(4, 0);
            preparedStatement.executeUpdate();

            // Retrieve the generated meal_id
            resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                mealId = resultSet.getInt(1);
            }
        } finally {
            closeResultSet(resultSet);
            closeStatement(preparedStatement);
        }

        return mealId;
    }

    public static int getOrCreateMealIdByName(String mealName) throws SQLException {
        int mealId = getMealIdByName(mealName);

        if (mealId == -1) {
            // If the meal does not exist, create it
            mealId = createMeal(mealName);
        }

        return mealId;
    }
    private static int getMealIdByName(String mealName) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int mealId = -1;

        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5433/Fitness", "postgres", "darknebula22");
            int userId = getUserIdByUsername(connection, UserSession.getInstance().getLoggedInUsername());

            preparedStatement = connection.prepareStatement("SELECT meal_id FROM meals WHERE meal_name = ? AND user_id = ?");
            preparedStatement.setString(1, mealName);
            preparedStatement.setInt(2, userId);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                mealId = resultSet.getInt("meal_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResultSet(resultSet);
            closeStatement(preparedStatement);
        }

        return mealId;
    }
    private static int getWorkoutIdByName(String workoutName) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int workoutId = -1;

        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5433/Fitness", "postgres", "darknebula22");
            int userId = getUserIdByUsername(connection, UserSession.getInstance().getLoggedInUsername());

            preparedStatement = connection.prepareStatement("SELECT workout_id FROM workouts WHERE workout_name = ? AND user_id = ?");
            preparedStatement.setString(1, workoutName);
            preparedStatement.setInt(2, userId);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                workoutId = resultSet.getInt("workout_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResultSet(resultSet);
            closeStatement(preparedStatement);
        }

        return workoutId;
    }

    public static void deleteMeal(int mealId) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5433/Fitness", "postgres", "darknebula22");

            // Delete the meal from the meals table
            String deleteMealSQL = "DELETE FROM meals WHERE meal_id = ?";
            preparedStatement = connection.prepareStatement(deleteMealSQL);
            preparedStatement.setInt(1, mealId);
            preparedStatement.executeUpdate();
        } finally {
            closeStatement(preparedStatement);
        }
    }
    public static ObservableList<Meal> getMealsDataForUser(int userId) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        ObservableList<Meal> mealsList = FXCollections.observableArrayList();

        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5433/Fitness", "postgres", "darknebula22");
            preparedStatement = connection.prepareStatement("SELECT * FROM meals WHERE user_id = ?");
            preparedStatement.setInt(1, userId);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int mealId = resultSet.getInt("meal_id");
                String mealName = resultSet.getString("meal_name");
                int totalCalories = resultSet.getInt("total_calories");

                mealsList.add(new Meal(mealId, mealName, totalCalories));
            }
        } finally {
            closeResultSet(resultSet);
            closeStatement(preparedStatement);
        }

        return mealsList;
    }


    public static int createWorkout(String workoutName) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int workoutId = -1;

        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5433/Fitness", "postgres", "darknebula22");

            // Insert the new workout with user_id
            int userId = getUserIdByUsername(connection, UserSession.getInstance().getLoggedInUsername());
            preparedStatement = connection.prepareStatement("INSERT INTO workouts (user_id, workout_name, date) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, userId);
            preparedStatement.setString(2, workoutName);
            // You might want to set a default date based on your requirements
            preparedStatement.setDate(3, Date.valueOf(LocalDate.now()));
            preparedStatement.executeUpdate();

            // Retrieve the generated workout_id
            resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                workoutId = resultSet.getInt(1);
            }
        } finally {
            closeResultSet(resultSet);
            closeStatement(preparedStatement);
        }

        return workoutId;
    }
    public static UserProfile getUserProfile(String username) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5433/Fitness", "postgres", "darknebula22");

            // Retrieve user profile information from the database
            String query = "SELECT full_name, gender, birthdate, height, weight FROM profile " +
                    "WHERE user_id = (SELECT user_id FROM users WHERE username = ?)";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String fullName = resultSet.getString("full_name");
                String gender = resultSet.getString("gender");
                LocalDate birthdate = resultSet.getDate("birthdate").toLocalDate();
                double height = resultSet.getDouble("height");
                double weight = resultSet.getDouble("weight");

                return new UserProfile(fullName, gender, birthdate, height, weight);
            }
        } finally {
            // Close the resources
            if (resultSet != null) {
                resultSet.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }

        return null; // Return null if no profile found
    }

    public static void deleteUserProfile(String username) throws SQLException {
        Connection connection = null;
        PreparedStatement deleteProfileStatement = null;

        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5433/Fitness", "postgres", "darknebula22");

            // Delete user profile information from the database
            String deleteQuery = "DELETE FROM profile WHERE user_id = (SELECT user_id FROM users WHERE username = ?)";
            deleteProfileStatement = connection.prepareStatement(deleteQuery);
            deleteProfileStatement.setString(1, username);
            deleteProfileStatement.executeUpdate();
        } finally {
            // Close the resources
            if (deleteProfileStatement != null) {
                deleteProfileStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    public static ObservableList<String> getWorkoutNamesForUser(int userId) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        ObservableList<String> workoutNames = FXCollections.observableArrayList();

        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5433/Fitness", "postgres", "darknebula22");
            preparedStatement = connection.prepareStatement("SELECT workout_name FROM workouts WHERE user_id = ?");
            preparedStatement.setInt(1, userId);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                workoutNames.add(resultSet.getString("workout_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResultSet(resultSet);
            closeStatement(preparedStatement);
        }

        return workoutNames;
    }

    public static int getOrCreateWorkoutIdByName(String workoutName) throws SQLException {
        int workoutId = getWorkoutIdByName(workoutName);

        if (workoutId == -1) {
            // If the workout does not exist, create it
            workoutId = createWorkout(workoutName);
        }

        return workoutId;
    }
    private static Connection connection;  // Assuming you have a static connection field

    public static Connection getConnection() {
        return connection;
    }

}




