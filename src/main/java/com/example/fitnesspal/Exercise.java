package com.example.fitnesspal;

public class Exercise {
    private int id;
    private String exerciseName;
    private String sets;
    private String reps;

    public Exercise(int id, String exerciseName, String sets, String reps) {
        this.id = id;
        this.exerciseName = exerciseName;
        this.sets = sets;
        this.reps = reps;
    }

    public int getId() {
        return id;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public String getSets() {
        return sets;
    }

    public String getReps() {
        return reps;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public void setSets(String sets) {
        this.sets = sets;
    }

    public void setReps(String reps) {
        this.reps = reps;
    }
}
