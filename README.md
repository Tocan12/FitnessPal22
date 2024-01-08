# FitnessPal

My IntelliJ and DBeaver project for FitnessPal.

## FitnessPal Project

### Table of Contents

- [Overview](#overview)
- [Features](#features)
  - [User Authentication](#user-authentication)
  - [Meal Management](#meal-management)
  - [Food Item Management](#food-item-management)
  - [Fitness Tracking (Work in Progress)](#fitness-tracking-work-in-progress)
- [Database](#database)
- [Getting Started](#getting-started)
- [Dependencies](#dependencies)
- [Build and Run](#build-and-run)
- [Usage](#usage)
- [Known Issues](#known-issues)
- [Troubleshooting](#troubleshooting)
- [Future Enhancements](#future-enhancements)
- [Interface](#interface)

## Overview

The FitnessPal application serves as a tool for tracking meals and workout exercises. Users can create, view, and delete meals, add food items to meals, manage their profiles, and record workout exercises.

## Features

### User Authentication

- Users need to log in with a username and password.
- If not logged in, users must sign up.
- Upon successful login, users can create, view, and delete their profiles.

### Meal Management

- Users can create meals, add food items to meals, and view their meals.
- The meal display includes the meal name and the total calories of added food items.
- Users can delete their meals.

### Food Item Management

- Users can view, edit, add, and delete food items.
- Each food item includes the food name, kcal/100g, protein, carbs, and fat.

### Fitness Tracking (Work in Progress)

- Users can add and view workout exercises.
- Users can create workout routines, but this feature is not yet functional.

## Database

- PostgreSQL is used to store user information, meals, food items, and workout exercises.

## Getting Started

Follow these steps to set up and run the FitnessPal project in IntelliJ IDEA:

1. **Install Git:**
   - Ensure that Git is installed on your machine. If not, download and install Git from [https://git-scm.com/](https://git-scm.com/).

2. **Open IntelliJ IDEA:**
   - Open IntelliJ IDEA and make sure the Git plugin is installed. If not, you can install it from the JetBrains Plugin Repository.

3. **Clone the repository:**
   - Click on "Get from Version Control" on the welcome screen or go to VCS > Get from Version Control in the menu.
   - In the "Get from Version Control" dialog, enter the URL of the GitHub repository you want to clone:
     ```
     https://github.com/Tocan12/FitnessPal
     ```

4. **Set up dependencies and configurations for JavaFX and SceneBuilder:**
   - Ensure that you have the necessary dependencies and configurations set up for JavaFX and SceneBuilder in your IntelliJ IDEA environment.
   - Open the project in IntelliJ IDEA.
   - Go to File > Project Structure.
   - In the Project Structure dialog, navigate to Project > Project.
   - Set the Project SDK to a version of Java compatible with your JavaFX project.
   - If JavaFX is not detected automatically, you may need to add it manually:
     - Go to Libraries and add the JavaFX library.
     - Make sure to include the necessary JAR files.

5. **Connect the application to a PostgreSQL database:**
   - Use the provided DBeaver configuration to connect the application to a PostgreSQL database.
   - Update the database connection details in the application configuration if necessary.

6. **Run the application:**
   - Locate the main class of your JavaFX application.
   - Right-click on the main class file and select Run.
   - Now, your FitnessPal project should be up and running in IntelliJ IDEA. If you encounter any issues or have specific dependencies, ensure they are configured correctly in your project.

## Dependencies

- JavaFX
- PostgreSQL JDBC Driver

## Build and Run

Ensure you have JavaFX and PostgreSQL JDBC Driver in your classpath.

## Usage

### Login or Signup:

- Log in with your username and password or sign up for a new account.
- Create your profile, allowing you to personalize your FitnessPal experience.

### Navigate Through Sections:

- Access the main page for an overview.
- Explore meal creation, food item management, and workout tracking sections.

### Track Meals and Nutrition:

- Create and manage your meals, tracking nutritional information.
- View and edit food items to suit your dietary preferences.

### Engage in Fitness Tracking:

- Add and view workout exercises (Work in Progress).
- Soon, create and manage workout routines for a holistic fitness experience.

## Documentaion for the project
### Class diagram:
![image](https://github.com/Tocan12/FitnessPal/assets/153304887/d31aeb07-7c6f-45ce-92ef-a8ebdf8f6be5)

### Database:
![image](https://github.com/Tocan12/FitnessPal/assets/153304887/b7d1ad7f-d4d8-4fa1-8385-e85d5944acde)


## Known Issues

- The workout tracking feature is under development and is not yet fully functional.

## Troubleshooting

- If you encounter database connection issues, double-check your database configuration.
- For other issues, please check the [issue tracker](https://github.com/Tocan12/FitnessPal/issues) or open a new issue.

## Future Enhancements

- Complete and enhance the workout tracking functionality.
- Continuously improve the user interface for a seamless and enjoyable user experience.

## Interface

### Log-in:

![image](https://github.com/Tocan12/FitnessPal/assets/153304887/3179c704-4668-4239-937c-a88310b00b48)

### Sign-up:

![image](https://github.com/Tocan12/FitnessPal/assets/153304887/4a7de7f2-0703-4798-a70a-fee0490cda47)

### Welcome:

![image](https://github.com/Tocan12/FitnessPal/assets/153304887/41ded89b-3cee-4a85-a6b0-bd6708fb342f)

### Create-Profile:

![image](https://github.com/Tocan12/FitnessPal/assets/153304887/a01edfcc-f2f6-44fd-b988-10dede481eec)

### Main Page:

![image](https://github.com/Tocan12/FitnessPal/assets/153304887/8cbbb1f1-039b-4842-99f6-d9e9afe1134a)

### Food Table:

![image](https://github.com/Tocan12/FitnessPal/assets/153304887/c9d30b5d-f93f-4eaf-a464-e7acc8ffbccd)

### Meals Table:

![image](https://github.com/Tocan12/FitnessPal/assets/153304887/b1d21807-4320-4700-95ea-ff8693491ed9)

### Exercises Table:

![image](https://github.com/Tocan12/FitnessPal/assets/153304887/f1e7afde-ba8d-43e7-ae0b-07b6af1d4fd6)

### Workouts Table:

![image](https://github.com/Tocan12/FitnessPal/assets/153304887/4a387d41-85dc-4749-97a7-bafee0bee801)

### Profile View:

![image](https://github.com/Tocan12/FitnessPal/assets/153304887/45c124fb-e941-421c-a754-ea5b2c0a25d6)








