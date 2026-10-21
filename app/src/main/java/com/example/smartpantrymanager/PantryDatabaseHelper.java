package com.example.smartpantrymanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class PantryDatabaseHelper extends SQLiteOpenHelper {

    //constant values declared
    private static final String DATABASE_NAME = "smart_pantry.db";
    private static final int DATABASE_VERSION = 2;
    private static final String TABLE_INGREDIENTS = "ingredients";
    private static final String COL_ID = "id";
    private static final String COL_NAME = "name";
    private static final String COL_QUANTITY = "quantity";
    private static final String COL_UNIT = "unit";
    private static final String COL_EXPIRY_DATE = "expiry_date";

    public PantryDatabaseHelper(Context context) { //constructor
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override //create function to create the tables
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_INGREDIENTS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NAME + " TEXT NOT NULL, " +
                COL_QUANTITY + " REAL NOT NULL, " +
                COL_UNIT + " TEXT NOT NULL, " +
                COL_EXPIRY_DATE + " TEXT)";
        db.execSQL(createTable);

        String createRecipesTable = "CREATE TABLE recipes (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "description TEXT, " +
                "instructions TEXT NOT NULL, " +
                "cooking_time INTEGER NOT NULL)";
        db.execSQL(createRecipesTable);

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

    @Override //for updating db
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            String createRecipesTable = "CREATE TABLE recipes (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name TEXT NOT NULL, " +
                    "description TEXT, " +
                    "instructions TEXT NOT NULL, " +
                    "cooking_time INTEGER NOT NULL)";
            db.execSQL(createRecipesTable);

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

    //create: add an ingredient
    public long addIngredient(Ingredient ingredient) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COL_NAME, ingredient.getName());
        values.put(COL_QUANTITY, ingredient.getQuantity());
        values.put(COL_UNIT, ingredient.getUnit());
        values.put(COL_EXPIRY_DATE, ingredient.getExpiryDate());

        long result = db.insert(TABLE_INGREDIENTS, null, values);
        db.close();

        return result;
    }

    //read: return all ingredients
    public ArrayList<Ingredient> getAllIngredients() {
        ArrayList<Ingredient> ingredients = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query( //sql query to return rows
                TABLE_INGREDIENTS,
                null,
                null,
                null,
                null,
                null,
                COL_NAME + " ASC"
        );

        try {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID));
                String name = cursor.getString(
                        cursor.getColumnIndexOrThrow(COL_NAME));
                double quantity = cursor.getDouble(
                        cursor.getColumnIndexOrThrow(COL_QUANTITY));
                String unit = cursor.getString(
                        cursor.getColumnIndexOrThrow(COL_UNIT));
                String expiryDate = cursor.getString(
                        cursor.getColumnIndexOrThrow(COL_EXPIRY_DATE));

                ingredients.add(new Ingredient(
                        id, name, quantity, unit, expiryDate));
            }
        } finally {
            cursor.close();
            db.close();
        }

        return ingredients;
    }

    //update: edit an existing ingredient
    public int updateIngredient(Ingredient ingredient) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COL_NAME, ingredient.getName());
        values.put(COL_QUANTITY, ingredient.getQuantity());
        values.put(COL_UNIT, ingredient.getUnit());
        values.put(COL_EXPIRY_DATE, ingredient.getExpiryDate());

        int rowsUpdated = db.update(
                TABLE_INGREDIENTS,
                values,
                COL_ID + " = ?",
                new String[]{String.valueOf(ingredient.getId())}
        );

        db.close();
        return rowsUpdated;
    }

    //delete: remove an ingredient
    public int deleteIngredient(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        int rowsDeleted = db.delete(
                TABLE_INGREDIENTS,
                COL_ID + " = ?",
                new String[]{String.valueOf(id)}
        );

        db.close();
        return rowsDeleted;
    }

    public long addRecipe(Recipe recipe) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", recipe.getName());
        values.put("description", recipe.getDescription());
        values.put("instructions", recipe.getInstructions());
        values.put("cooking_time", recipe.getCookingTime());

        long recipeId = db.insert("recipes", null, values);
        db.close();

        return recipeId;
    }

    public ArrayList<Recipe> getAllRecipes() {
        ArrayList<Recipe> recipes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
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
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                String instructions = cursor.getString(cursor.getColumnIndexOrThrow("instructions"));
                int cookingTime = cursor.getInt(cursor.getColumnIndexOrThrow("cooking_time"));

                recipes.add(new Recipe(id, name, description, instructions, cookingTime));
            }
        } finally {
            cursor.close();
            db.close();
        }
        return recipes;
    }

    public long addRecipeIngredient(int recipeId, String ingredientName, double requiredQuantity, String unit) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("recipe_id", recipeId);
        values.put("ingredient_name", ingredientName.trim());
        values.put("required_quantity", requiredQuantity);
        values.put("unit", unit.trim());

        long result = db.insert("recipe_ingredients", null, values);
        db.close();

        return result;
    }

    public ArrayList<String> getRecipeIngredients(int recipeId) {
        ArrayList<String> ingredients = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
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
            while (cursor.moveToNext()) {
                String name = cursor.getString(0);
                double quantity = cursor.getDouble(1);
                String unit = cursor.getString(2);

                ingredients.add(name + " - " + quantity + " " + unit);
            }
        } finally {
            cursor.close();
            db.close();
        }
        return ingredients;
    }
}
