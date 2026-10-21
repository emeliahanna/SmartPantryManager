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

    //return only recipes that can be made with the current pantry.
    public ArrayList<Recipe> getSuggestedRecipes() {
        ArrayList<Ingredient> pantry = getAllIngredients();
        ArrayList<Recipe> allRecipes = getAllRecipes();
        ArrayList<Recipe> suggestions = new ArrayList<>();

        for (Recipe recipe : allRecipes) {
            if (canMakeRecipe(recipe, pantry)) {
                suggestions.add(recipe);
            }
        }
        return suggestions;
    }

    //check every ingredient required by one recipe.
    private boolean canMakeRecipe(Recipe recipe, ArrayList<Ingredient> pantry) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                "recipe_ingredients",
                new String[]{"ingredient_name", "required_quantity", "unit"},
                "recipe_id = ?",
                new String[]{String.valueOf(recipe.getId())},
                null, null, null
        );
        boolean hasIngredients = false;

        try {
            while (cursor.moveToNext()) {
                hasIngredients = true;

                String requiredName = normalizeIngredientName(cursor.getString(0));

                double requiredQuantity = cursor.getDouble(1);
                String requiredUnit = cursor.getString(2);

                String requiredUnitType = getUnitType(requiredUnit);

                double requiredBaseQuantity = toBaseQuantity(requiredQuantity, requiredUnit);

                double availableBaseQuantity = 0;

                for (Ingredient ingredient : pantry) {
                    String pantryName = normalizeIngredientName(
                            ingredient.getName());

                    String pantryUnitType = getUnitType(
                            ingredient.getUnit());

                    if (requiredName.equals(pantryName) && requiredUnitType.equals(pantryUnitType)) {
                        availableBaseQuantity += toBaseQuantity(ingredient.getQuantity(), ingredient.getUnit());
                    }
                }

                //missing or insufficient ingredients disqualify the recipe.
                if (availableBaseQuantity + 0.000001
                        < requiredBaseQuantity) {
                    return false;
                }
            }
        } finally {
            cursor.close();
            db.close();
        }

        //a recipe without any required ingredients is not suggested.
        return hasIngredients;
    }

    //normalize common singular and plural ingredient names.
    private String normalizeIngredientName(String name) {
        String normalized = name.trim().toLowerCase(java.util.Locale.ROOT).replaceAll("[^a-z0-9 ]", "").replaceAll("\\s+", " ");

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

        if (normalized.endsWith("ies")
                && normalized.length() > 3) {
            return normalized.substring(0,
                    normalized.length() - 3) + "y";
        }

        if (normalized.endsWith("s")
                && !normalized.endsWith("ss")
                && normalized.length() > 1) {
            return normalized.substring(0, normalized.length() - 1);
        }
        return normalized;
    }

    //convert compatible units to a common unit type.
    private String getUnitType(String unit) {
        String normalized = unit.trim().toLowerCase(java.util.Locale.ROOT);

        switch (normalized) {
            case "g":
            case "gram":
            case "grams":
            case "kg":
            case "kilogram":
            case "kilograms":
                return "mass";

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

            case "piece":
            case "pieces":
            case "pc":
            case "pcs":
            case "item":
            case "items":
                return "count";

            case "slice":
            case "slices":
                return "slice";

            default:
                return normalized;
        }
    }

    //convert kg to g and litres to ml.
    private double toBaseQuantity(double quantity, String unit) {
        String normalized = unit.trim().toLowerCase(java.util.Locale.ROOT);

        switch (normalized) {
            case "kg":
            case "kilogram":
            case "kilograms":
                return quantity * 1000.0;

            case "l":
            case "litre":
            case "litres":
            case "liter":
            case "liters":
                return quantity * 1000.0;

            default:
                return quantity;
        }
    }
}
