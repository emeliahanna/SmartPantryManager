package com.example.smartpantrymanager;

import android.os.Bundle;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class RecipeDetailActivity extends AppCompatActivity {
    private PantryDatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recipe_detail);

        //initialize database helper
        databaseHelper = new PantryDatabaseHelper(this);

        //connect text views to layout elements
        TextView textRecipeName = findViewById(R.id.textDetailRecipeName);
        TextView textDescription = findViewById(R.id.textDetailDescription);
        TextView textCookingTime = findViewById(R.id.textDetailCookingTime);
        TextView textIngredients = findViewById(R.id.textDetailIngredients);
        TextView textInstructions = findViewById(R.id.textDetailInstructions);

        //retrieve selected recipe id passed through intent
        int recipeId = getIntent().getIntExtra("recipe_id", -1);
        Recipe selectedRecipe = null;

        //retrieve all saved recipes
        ArrayList<Recipe> recipes = databaseHelper.getAllRecipes();

        //find recipe matching selected id
        for (Recipe recipe : recipes) {
            if (recipe.getId() == recipeId) {
                selectedRecipe = recipe;
                break;
            }
        }

        //show message and close screen if recipe is unavailable
        if (selectedRecipe == null) {
            android.widget.Toast.makeText(this, "Recipe not found.", android.widget.Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        //display selected recipe details
        textRecipeName.setText(selectedRecipe.getName());
        textDescription.setText(selectedRecipe.getDescription());
        textCookingTime.setText("Cooking Time: " + selectedRecipe.getCookingTime() + " minutes");
        textInstructions.setText(selectedRecipe.getInstructions());

        //retrieve required ingredients for selected recipe
        ArrayList<String> ingredients = databaseHelper.getRecipeIngredients(recipeId);
        StringBuilder ingredientText = new StringBuilder();

        //format ingredients as a bulleted list
        for (String ingredient : ingredients) {
            ingredientText.append("• ").append(ingredient).append("\n");
        }

        //display formatted ingredient list
        textIngredients.setText(ingredientText.toString());

        //return to suggested recipes screen
        findViewById(R.id.buttonBackToRecipes).setOnClickListener(v -> {
            finish();
        });
    }
}