package com.example.fitnesspal;

public class food {
    int id ;
    String food_name, calories, protein , carbs,fat;

    public void setId(int id) {
        this.id = id;
    }

    public void setFood_name(String food_name) {
        this.food_name = food_name;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }

    public void setProtein(String protein) {
        this.protein = protein;
    }

    public void setCarbs(String carbs) {
        this.carbs = carbs;

    }
    public void setFat(String fat) {
        this.fat = fat;

    }


    public int getId() {
        return id;
    }

    public String getFood_name() {
        return food_name;
    }

    public String getCalories() {
        return calories;
    }

    public String getProtein() {
        return protein;
    }

    public String getCarbs() {
        return carbs;
    }
    public String getFat() {
        return fat;
    }
    public food(int id, String food_name, String calories, String protein, String carbs, String fat) {
        this.id = id;
        this.food_name = food_name;
        this.calories = calories;
        this.protein = protein;
        this.carbs = carbs;
        this.fat=fat;
    }
}


