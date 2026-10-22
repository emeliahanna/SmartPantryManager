package com.example.smartpantrymanager;

//import classes for database operations and application context
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class PantryDatabaseHelper extends SQLiteOpenHelper {

    //declare database constants and ingredient table column names
    private static final String DATABASE_NAME = "smart_pantry.db";
    private static final int DATABASE_VERSION = 2;
    private static final String TABLE_INGREDIENTS = "ingredients";
    private static final String COL_ID = "id";
    private static final String COL_NAME = "name";
    private static final String COL_QUANTITY = "quantity";
    private static final String COL_UNIT = "unit";
    private static final String COL_EXPIRY_DATE = "expiry_date";

    //initialize database helper
    public PantryDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //create database tables when database is first created
    @Override
    public void onCreate(SQLiteDatabase db) {

        //create ingredients table with required fields and optional expiry date
        String createTable = "CREATE TABLE " + TABLE_INGREDIENTS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NAME + " TEXT NOT NULL, " +
                COL_QUANTITY + " REAL NOT NULL, " +
                COL_UNIT + " TEXT NOT NULL, " +
                COL_EXPIRY_DATE + " TEXT)";
        db.execSQL(createTable);

        //create recipes table to store recipe details
        String createRecipesTable = "CREATE TABLE recipes (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "description TEXT, " +
                "instructions TEXT NOT NULL, " +
                "cooking_time INTEGER NOT NULL)";
        db.execSQL(createRecipesTable);

        //create table linking recipes to their required ingredients and quantities
        String createRecipeIngredientsTable =
                "CREATE TABLE recipe_ingredients (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "recipe_id INTEGER NOT NULL, " +
                        "ingredient_name TEXT NOT NULL, " +
                        "required_quantity REAL NOT NULL, " +
                        "unit TEXT NOT NULL, " +
                        "FOREIGN KEY(recipe_id) REFERENCES recipes(id) ON DELETE CASCADE)";
        db.execSQL(createRecipeIngredientsTable);
    }

    //add new tables when upgrading an older database version
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //create recipe tables when upgrading from a version below 2
        if (oldVersion < 2) {
            String createRecipesTable = "CREATE TABLE recipes (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name TEXT NOT NULL, " +
                    "description TEXT, " +
                    "instructions TEXT NOT NULL, " +
                    "cooking_time INTEGER NOT NULL)";
            db.execSQL(createRecipesTable);

            //create table for recipe ingredient requirements
            String createRecipeIngredientsTable =
                    "CREATE TABLE recipe_ingredients (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            "recipe_id INTEGER NOT NULL, " +
                            "ingredient_name TEXT NOT NULL, " +
                            "required_quantity REAL NOT NULL, " +
                            "unit TEXT NOT NULL, " +
                            "FOREIGN KEY(recipe_id) REFERENCES recipes(id) ON DELETE CASCADE)";
            db.execSQL(createRecipeIngredientsTable);
        }
    }

    //insert new ingredient into database
    public long addIngredient(Ingredient ingredient) {
        SQLiteDatabase db = this.getWritableDatabase();

        //store ingredient values before inserting the record
        ContentValues values = new ContentValues();
        values.put(COL_NAME, ingredient.getName());
        values.put(COL_QUANTITY, ingredient.getQuantity());
        values.put(COL_UNIT, ingredient.getUnit());
        values.put(COL_EXPIRY_DATE, ingredient.getExpiryDate());

        //insert ingredient and return new row id or -1 if insertion fails
        long result = db.insert(TABLE_INGREDIENTS, null, values);
        db.close();

        return result;
    }

    //return all ingredients ordered alphabetically
    public ArrayList<Ingredient> getAllIngredients() {
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        //query database to retrieve ingredient records
        Cursor cursor = db.query(
                TABLE_INGREDIENTS,
                null,
                null,
                null,
                null,
                null,
                COL_NAME + " ASC"
        );

        try {
            //move through each database row
            while (cursor.moveToNext()) {
                //read ingredient values from current row
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COL_NAME));
                double quantity = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_QUANTITY));
                String unit = cursor.getString(cursor.getColumnIndexOrThrow(COL_UNIT));
                String expiryDate = cursor.getString(cursor.getColumnIndexOrThrow(COL_EXPIRY_DATE));

                //create ingredient object and add it to the list
                ingredients.add(new Ingredient(id, name, quantity, unit, expiryDate));
            }
        } finally {
            //close cursor and database after reading records
            cursor.close();
            db.close();
        }
        return ingredients;
    }

    //edit existing ingredient using its id
    public int updateIngredient(Ingredient ingredient) {
        SQLiteDatabase db = this.getWritableDatabase();

        //store updated ingredient values
        ContentValues values = new ContentValues();
        values.put(COL_NAME, ingredient.getName());
        values.put(COL_QUANTITY, ingredient.getQuantity());
        values.put(COL_UNIT, ingredient.getUnit());
        values.put(COL_EXPIRY_DATE, ingredient.getExpiryDate());

        //update matching database row using parameterized selection
        int rowsUpdated = db.update(TABLE_INGREDIENTS, values, COL_ID + " = ?", new String[]{String.valueOf(ingredient.getId())});
        db.close();
        return rowsUpdated;
    }

    //remove ingredient matching supplied id
    public int deleteIngredient(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        //delete matching row and return number of rows removed
        int rowsDeleted = db.delete(TABLE_INGREDIENTS, COL_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
        return rowsDeleted;
    }

    //insert new recipe and return its generated id
    public long addRecipe(Recipe recipe) {
        SQLiteDatabase db = this.getWritableDatabase();

        //store recipe details for insertion
        ContentValues values = new ContentValues();
        values.put("name", recipe.getName());
        values.put("description", recipe.getDescription());
        values.put("instructions", recipe.getInstructions());
        values.put("cooking_time", recipe.getCookingTime());

        //insert recipe into database
        long recipeId = db.insert("recipes", null, values);
        db.close();

        return recipeId;
    }

    //return all recipes ordered alphabetically
    public ArrayList<Recipe> getAllRecipes() {
        ArrayList<Recipe> recipes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        //query all recipe records
        Cursor cursor = db.query(
                "recipes",
                null,
                null,
                null,
                null,
                null,
                "name ASC"
        );

        try {
            //read each recipe from the result set
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                String instructions = cursor.getString(cursor.getColumnIndexOrThrow("instructions"));
                int cookingTime = cursor.getInt(cursor.getColumnIndexOrThrow("cooking_time"));

                //create recipe object and add it to the list
                recipes.add(new Recipe(id, name, description, instructions, cookingTime));
            }
        } finally {
            //release database resources
            cursor.close();
            db.close();
        }

        return recipes;
    }

    //add required ingredient details for a recipe
    public long addRecipeIngredient(int recipeId, String ingredientName, double requiredQuantity, String unit) {
        SQLiteDatabase db = this.getWritableDatabase();

        //store recipe id, ingredient name, quantity and unit
        ContentValues values = new ContentValues();
        values.put("recipe_id", recipeId);
        values.put("ingredient_name", ingredientName.trim());
        values.put("required_quantity", requiredQuantity);
        values.put("unit", unit.trim());

        //insert recipe ingredient requirement
        long result = db.insert("recipe_ingredients", null, values);
        db.close();
        return result;
    }

    //return ingredient requirements for selected recipe
    public ArrayList<String> getRecipeIngredients(int recipeId) {
        ArrayList<String> ingredients = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        //query ingredient requirements belonging to selected recipe
        Cursor cursor = db.query(
                "recipe_ingredients",
                new String[]{"ingredient_name", "required_quantity", "unit"},
                "recipe_id = ?",
                new String[]{String.valueOf(recipeId)},
                null,
                null,
                "ingredient_name ASC"
        );

        try {
            //read each required ingredient and format it for display
            while (cursor.moveToNext()) {
                String name = cursor.getString(0);
                double quantity = cursor.getDouble(1);
                String unit = cursor.getString(2);

                ingredients.add(name + " - " + quantity + " " + unit);
            }
        } finally {
            //close cursor and database after reading requirements
            cursor.close();
            db.close();
        }
        return ingredients;
    }

    //return recipes that can be made with current pantry ingredients
    public ArrayList<Recipe> getSuggestedRecipes() {
        ArrayList<Ingredient> pantry = getAllIngredients();
        ArrayList<Recipe> allRecipes = getAllRecipes();
        ArrayList<Recipe> suggestions = new ArrayList<>();

        //check each recipe against available pantry ingredients
        for (Recipe recipe : allRecipes) {
            if (canMakeRecipe(recipe, pantry)) {
                suggestions.add(recipe);
            }
        }
        return suggestions;
    }

    //check whether every required ingredient is available in sufficient quantity
    private boolean canMakeRecipe(Recipe recipe, ArrayList<Ingredient> pantry) {
        SQLiteDatabase db = this.getReadableDatabase();

        //retrieve all ingredient requirements for selected recipe
        Cursor cursor = db.query(
                "recipe_ingredients",
                new String[]{"ingredient_name", "required_quantity", "unit"},
                "recipe_id = ?",
                new String[]{String.valueOf(recipe.getId())},
                null, null, null
        );
        boolean hasIngredients = false;

        try {
            //check every required ingredient
            while (cursor.moveToNext()) {
                hasIngredients = true;

                //normalize required ingredient name before comparison
                String requiredName = normalizeIngredientName(cursor.getString(0));

                double requiredQuantity = cursor.getDouble(1);
                String requiredUnit = cursor.getString(2);

                //identify measurement type and convert requirement to base quantity
                String requiredUnitType = getUnitType(requiredUnit);
                double requiredBaseQuantity = toBaseQuantity(requiredQuantity, requiredUnit);

                //track total available quantity in compatible units
                double availableBaseQuantity = 0;

                //check all pantry ingredients for matching name and unit type
                for (Ingredient ingredient : pantry) {
                    String pantryName = normalizeIngredientName(ingredient.getName());
                    String pantryUnitType = getUnitType(ingredient.getUnit());

                    //add quantity when ingredient name and measurement type match
                    if (requiredName.equals(pantryName) && requiredUnitType.equals(pantryUnitType)) {
                        availableBaseQuantity += toBaseQuantity(ingredient.getQuantity(), ingredient.getUnit());
                    }
                }
                //reject recipe if ingredient is missing or quantity is insufficient
                if (availableBaseQuantity + 0.000001 < requiredBaseQuantity) {
                    return false;
                }
            }
        } finally {
            //release cursor and database resources
            cursor.close();
            db.close();
        }

        //reject recipes that have no ingredient requirements
        return hasIngredients;
    }

    //normalize ingredient names to improve matching between singular and plural forms
    private String normalizeIngredientName(String name) {
        String normalized = name.trim().toLowerCase(java.util.Locale.ROOT).replaceAll("[^a-z0-9 ]", "").replaceAll("\\s+", " ");

        //handle irregular plural forms
        switch (normalized) {
            case "tomatoes":
                return "tomato";
            case "potatoes":
                return "potato";
            case "leaves":
                return "leaf";
            case "loaves":
                return "loaf";
        }

        //convert words ending in ies to y
        if (normalized.endsWith("ies") && normalized.length() > 3) {
            return normalized.substring(0, normalized.length() - 3) + "y";
        }

        //remove trailing s for common plural forms
        if (normalized.endsWith("s") &&
                !normalized.endsWith("ss") &&
                normalized.length() > 1) {
            return normalized.substring(0, normalized.length() - 1);
        }
        return normalized;
    }

    //identify compatible measurement types
    private String getUnitType(String unit) {
        String normalized = unit.trim().toLowerCase(java.util.Locale.ROOT);

        //group grams and kilograms as mass units
        switch (normalized) {
            case "g":
            case "gram":
            case "grams":
            case "kg":
            case "kilogram":
            case "kilograms":
                return "mass";

            //group millilitres and litres as volume units
            case "ml":
            case "millilitre":
            case "millilitres":
            case "milliliter":
            case "milliliters":
            case "l":
            case "litre":
            case "litres":
            case "liter":
            case "liters":
                return "volume";

            //group common count units
            case "piece":
            case "pieces":
            case "pc":
            case "pcs":
            case "item":
            case "items":
                return "count";

            //keep slices separate from other count units
            case "slice":
            case "slices":
                return "slice";

            //treat unknown units as their own unit type
            default:
                return normalized;
        }
    }

    //convert kilograms to grams and litres to millilitres
    private double toBaseQuantity(double quantity, String unit) {
        String normalized = unit.trim().toLowerCase(java.util.Locale.ROOT);

        switch (normalized) {
            //convert kilograms to grams
            case "kg":
            case "kilogram":
            case "kilograms":
                return quantity * 1000.0;

            //convert litres to millilitres
            case "l":
            case "litre":
            case "litres":
            case "liter":
            case "liters":
                return quantity * 1000.0;

            //return quantity unchanged for units already in base form
            default:
                return quantity;
        }
    }
}