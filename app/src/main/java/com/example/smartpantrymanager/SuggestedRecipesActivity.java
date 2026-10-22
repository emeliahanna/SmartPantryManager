package com.example.smartpantrymanager;

//imports
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SuggestedRecipesActivity extends AppCompatActivity {
    //variables for recipe list and db
    private RecyclerView recyclerViewRecipes;
    private TextView textNoRecipes;
    private PantryDatabaseHelper databaseHelper;
    private RecipeAdapter recipeAdapter;

    @Override //called when activity is first created
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_suggested_recipes);
        //adjust screen padding to avoid system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        recyclerViewRecipes = findViewById(R.id.recyclerViewRecipes); //connect recipe list to layout
        databaseHelper = new PantryDatabaseHelper(this); //db helper
        recyclerViewRecipes.setLayoutManager(new LinearLayoutManager(this)); //display rcipes in vertical list
        findViewById(R.id.buttonBackToPantry).setOnClickListener(v -> {
            finish(); //back to pantry main screen
        });
        //seed the database if recipes have not been added yet.
        RecipeSeeder recipeSeeder = new RecipeSeeder(databaseHelper);
        recipeSeeder.seedRecipes();

        loadRecipes(); //load recipes from db
    }

    //return suggested recipes
    private void loadRecipes() {
        ArrayList<Recipe> recipes = databaseHelper.getSuggestedRecipes();
        textNoRecipes = findViewById(R.id.textNoRecipes);

        if (recipes.isEmpty()) { //empty message if no matching recipes found
            textNoRecipes.setVisibility(View.VISIBLE);
            recyclerViewRecipes.setVisibility(View.GONE);
        } else { //recipes list when matching exists
            textNoRecipes.setVisibility(View.GONE);
            recyclerViewRecipes.setVisibility(View.VISIBLE);
        }

        //handle clicks on each recipe card
        recipeAdapter = new RecipeAdapter(recipes, recipe -> {
            android.content.Intent intent = new android.content.Intent(SuggestedRecipesActivity.this, RecipeDetailActivity.class);
            intent.putExtra("recipe_id", recipe.getId());
            startActivity(intent);
        });
        recyclerViewRecipes.setAdapter(recipeAdapter);
    }
}