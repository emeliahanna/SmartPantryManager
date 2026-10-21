package com.example.smartpantrymanager;

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
    private RecyclerView recyclerViewRecipes;
    private TextView textNoRecipes;
    private PantryDatabaseHelper databaseHelper;
    private RecipeAdapter recipeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_suggested_recipes);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        recyclerViewRecipes = findViewById(R.id.recyclerViewRecipes);
        databaseHelper = new PantryDatabaseHelper(this);
        recyclerViewRecipes.setLayoutManager(new LinearLayoutManager(this));
        findViewById(R.id.buttonBackToPantry).setOnClickListener(v -> {
            finish();
        });
        //seed the database if recipes have not been added yet.
        RecipeSeeder recipeSeeder = new RecipeSeeder(databaseHelper);
        recipeSeeder.seedRecipes();

        loadRecipes();
    }


    private void loadRecipes() {
        ArrayList<Recipe> recipes = databaseHelper.getSuggestedRecipes();
        recipeAdapter = new RecipeAdapter(recipes, recipe -> {
            android.content.Intent intent = new android.content.Intent( SuggestedRecipesActivity.this, RecipeDetailActivity.class);
            intent.putExtra("recipe_id", recipe.getId());
            startActivity(intent);
        });
        recyclerViewRecipes.setAdapter(recipeAdapter);
    }
}