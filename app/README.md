# Smart Pantry Manager

## 1. Project Overview
Smart Pantry Manager is an Android application developed using Java in Android Studio. It is made to help users manage pantry ingredients, 
track quantities and expiry dates, and discover recipes based on ingredients available in their pantry.
The application uses a local SQLite database to store ingredient and recipe information.

## 2. Features
- Pantry Management: Add, view, edit, and delete pantry ingredients.
- Ingredient Tracking: Store ingredient names, quantities, measurement units, and expiry dates.
- Suggested Recipes: Display recipes that match ingredients currently available in the pantry.
- Strict Recipe Matching: Suggest recipes only when all required ingredients are available in sufficient quantities.
- Recipe Details: View recipe descriptions, cooking times, ingredients, and preparation instructions.
- Settings:** Save and restore the user's expiry-alert preference.
- Local Database: Store pantry and recipe data using SQLite.
- User Interface: Provide ingredient and recipe lists using RecyclerView.

## 3. Technologies Used
- Java
- Android Studio
- Android SDK
- SQLite
- SharedPreferences
- RecyclerView
- XML for layout design
- Git and GitHub for version control

## 4. Requirements
The following are needed to open, build, and run the project:
- Android Studio
- Java Development Kit (JDK) compatible with the project's Gradle configuration
- Android SDK and required build tools
- Gradle, using the wrapper included in the project
- An Android emulator or compatible Android device

An internet connection may be required when building for the first time to download Gradle or dependencies.

## 5. Installation and Setup
1. Download or clone this repository.
2. Open Android Studio.
3. Select 'Open' and choose the project folder.
4. Allow Gradle to sync and download any required dependencies.
5. Connect an Android device with USB debugging enabled or start an Android emulator.
6. Select the app configuration and click 'Run'.

The application creates its local SQLite database when needed and seeds the recipe collection when the recipe table is empty.

## 6. Database
SQLite is used to store information locally on the device. The database manages pantry ingredients, recipes, 
and recipe ingredient requirements. The database is created at runtime. It is not necessary to provide a pre-existing 
database file for a fresh installation, provided the application creates and initializes the database successfully.

## 7. Settings Limitation
The expiry-alert setting is saved using Android SharedPreferences. In the current version, the preference is stored 
and restored, but actual expiry notifications are not yet implemented.

## 8. Version Control
Git and GitHub are used to track project development and changes. The repository contains the source code and project files.