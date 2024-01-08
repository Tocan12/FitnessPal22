package com.example.fitnesspal;

import javafx.beans.property.*;

public class Meal {

    private final IntegerProperty mealId;
    private final StringProperty mealName;
    private final IntegerProperty totalCalories;

    public Meal(int mealId, String mealName, int totalCalories) {
        this.mealId = new SimpleIntegerProperty(mealId);
        this.mealName = new SimpleStringProperty(mealName);
        this.totalCalories = new SimpleIntegerProperty(totalCalories);
    }

    public int getMealId() {
        return mealId.get();
    }

    public IntegerProperty mealIdProperty() {
        return mealId;
    }

    public String getMealName() {
        return mealName.get();
    }

    public StringProperty mealNameProperty() {
        return mealName;
    }

    public int getTotalCalories() {
        return totalCalories.get();
    }

    public IntegerProperty totalCaloriesProperty() {
        return totalCalories;
    }
}
