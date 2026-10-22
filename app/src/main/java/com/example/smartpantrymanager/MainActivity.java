package com.example.smartpantrymanager;

//imports to create activity
import android.os.Bundle;
import android.content.Intent;
//imports to manage activity and screen layout
import androidx.appcompat.app.AppCompatActivity;
//imports for recycler view classes
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerViewIngredients; //for list of ingredients in pantry
    private PantryDatabaseHelper databaseHelper; //for accessing and managing sqlite db
    private IngredientAdapter ingredientAdapter; //to connect ingredient data to recycler view
    private ArrayList<Ingredient> ingredients; //to store ingredients retrieved from db

    //called when activity is first created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); //connecting java activity to xml layouts

        recyclerViewIngredients = findViewById(R.id.recyclerViewIngredients); //find recyclerview defined in xml file
        databaseHelper = new PantryDatabaseHelper(this); //create helper object to access pantry db
        recyclerViewIngredients.setLayoutManager(new LinearLayoutManager(this)); //arranges ingredients vertically

        loadIngredients(); //return ingredients

        //get button ids and connect them to java and giving them an action when clicked on
        findViewById(R.id.buttonAddIngredient).setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(MainActivity.this, AddIngredientActivity.class);
            startActivity(intent);
        });
        findViewById(R.id.buttonSuggestedRecipes).setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(MainActivity.this, SuggestedRecipesActivity.class);
            startActivity(intent);
        });
        findViewById(R.id.buttonSettings).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        });
    }

    //called when activity returns to foreground
    @Override
    protected void onResume() {
        super.onResume();

        if (databaseHelper != null) { //check if db helper is initialized
            loadIngredients(); //reload ingredients if needed
        }
    }

    //return ingredients from db and display them
    private void loadIngredients() {
        ingredients = databaseHelper.getAllIngredients(); //return ingredeints from db
        //create adapter and define what happens when ingredient is clicked
        ingredientAdapter = new IngredientAdapter(ingredients, ingredient -> {
            android.content.Intent intent = new android.content.Intent(MainActivity.this, AddIngredientActivity.class); //create intent to open add ingredient screen in edit mode
            intent.putExtra("ingredient_id", ingredient.getId()); //pass selected ingredient's id to identify which record to edit
            startActivity(intent); //open screen with selected ingredient's info
        });
        recyclerViewIngredients.setAdapter(ingredientAdapter); //attach adapter to recyclerview so ingredients can be displayed
    }
}