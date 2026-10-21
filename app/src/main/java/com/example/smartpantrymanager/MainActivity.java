package com.example.smartpantrymanager;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerViewIngredients;
    private PantryDatabaseHelper databaseHelper;
    private IngredientAdapter ingredientAdapter;
    private ArrayList<Ingredient> ingredients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerViewIngredients = findViewById(R.id.recyclerViewIngredients);
        databaseHelper = new PantryDatabaseHelper(this);
        recyclerViewIngredients.setLayoutManager(new LinearLayoutManager(this));

        loadIngredients();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (databaseHelper != null) {
            loadIngredients();
        }
    }

    private void loadIngredients() {
        ingredients = databaseHelper.getAllIngredients();
        ingredientAdapter = new IngredientAdapter(ingredients);
        recyclerViewIngredients.setAdapter(ingredientAdapter);
    }
}