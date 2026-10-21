package com.example.smartpantrymanager;

public class Recipe {
    //variables
    private int id;
    private String name;
    private String description;
    private String instructions;
    private int cookingTime;

    public Recipe(int id, String name, String description, String instructions, int cookingTime) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.instructions = instructions;
        this.cookingTime = cookingTime;
    }

    public Recipe(String name, String description, String instructions, int cookingTime) {
        this.name = name;
        this.description = description;
        this.instructions = instructions;
        this.cookingTime = cookingTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getInstructions() {
        return instructions;
    }

    public int getCookingTime() {
        return cookingTime;
    }
}
