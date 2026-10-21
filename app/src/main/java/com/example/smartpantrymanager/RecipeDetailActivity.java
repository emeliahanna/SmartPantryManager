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
        databaseHelper = new PantryDatabaseHelper(this);

        TextView textRecipeName = findViewById(R.id.textDetailRecipeName);
        TextView textDescription = findViewById(R.id.textDetailDescription);
        TextView textCookingTime = findViewById(R.id.textDetailCookingTime);
        TextView textIngredients = findViewById(R.id.textDetailIngredients);
        TextView textInstructions = findViewById(R.id.textDetailInstructions);

        int recipeId = getIntent().getIntExtra("recipe_id", -1);
        Recipe selectedRecipe = null;
        ArrayList<Recipe> recipes = databaseHelper.getAllRecipes();

        for (Recipe recipe : recipes) {
            if (recipe.getId() == recipeId) {
                selectedRecipe = recipe;
                break;
            }
        }

        if (selectedRecipe == null) {
            android.widget.Toast.makeText(this, "Recipe not found.", android.widget.Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        textRecipeName.setText(selectedRecipe.getName());
        textDescription.setText(selectedRecipe.getDescription());
        textCookingTime.setText("Cooking time: " + selectedRecipe.getCookingTime() + " minutes");
        textInstructions.setText(selectedRecipe.getInstructions());

        ArrayList<String> ingredients = databaseHelper.getRecipeIngredients(recipeId);
        StringBuilder ingredientText = new StringBuilder();

        for (String ingredient : ingredients) {
            ingredientText.append("• ").append(ingredient).append("\n");
        }
        textIngredients.setText(ingredientText.toString());
        findViewById(R.id.buttonBackToRecipes).setOnClickListener(v -> {
            finish();
        });
    }
}