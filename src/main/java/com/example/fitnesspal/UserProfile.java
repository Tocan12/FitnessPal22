

package com.example.fitnesspal;

import java.time.LocalDate;

public class UserProfile {
    private String fullName;
    private String gender;
    private LocalDate birthdate;
    private double height;
    private double weight;

    public UserProfile(String fullName, String gender, LocalDate birthdate, double height, double weight) {
        this.fullName = fullName;
        this.gender = gender;
        this.birthdate = birthdate;
        this.height = height;
        this.weight = weight;
    }

    public String getFullName() {
        return fullName;
    }

    public String getGender() {
        return gender;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public double getHeight() {
        return height;
    }

    public double getWeight() {
        return weight;
    }
}
