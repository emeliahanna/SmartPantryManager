package com.example.smartpantrymanager;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class RecipeSeeder {
    private final PantryDatabaseHelper databaseHelper;

    //initialize database helper
    public RecipeSeeder(PantryDatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    //add default recipes only when recipe table is empty
    public void seedRecipes() {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        //count existing recipes to prevent duplicate seeding
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM recipes", null);
        int count = 0;

        try {
            if (cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }
        } finally {
            //close cursor after reading count
            cursor.close();
        }

        //stop if recipes already exist
        if (count > 0) {
            return;
        }

        //start transaction so recipe data is inserted as one operation
        db.beginTransaction();

        try {
            //insert pancake recipe and its required ingredients
            addRecipe(db, "Pancakes", "Soft homemade pancakes", "Mix the dry ingredients first. Mix the wet ingredients separately. Add wet ingredients to dry ingredients slowly until fully mixed. Pour batter into a hot pan. Cook both sides.",
                    20,
                    new String[]{"flour", "milk", "eggs", "baking powder", "vanilla essence", "oil"},
                    new double[]{200, 250, 2, 2, 5, 100},
                    new String[]{"g", "ml", "pieces", "g", "ml", "ml"});

            //insert scrambled eggs recipe
            addRecipe(db, "Scrambled Eggs",
                    "Quick scrambled eggs",
                    "Beat the eggs. Cook gently in a lightly oiled pan while stirring.",
                    10,
                    new String[]{"eggs", "milk"},
                    new double[]{2, 30},
                    new String[]{"pieces", "ml"});

            //insert tomato pasta recipe
            addRecipe(db, "Tomato Pasta",
                    "Pasta with a simple tomato sauce",
                    "Boil pasta. Cook chopped tomatoes with onion, then combine.",
                    25,
                    new String[]{"pasta", "tomatoes", "onion"},
                    new double[]{200, 2, 1},
                    new String[]{"g", "pieces", "pieces"});

            //insert cheese omelette recipe
            addRecipe(db, "Cheese Omelette",
                    "Omelette filled with cheese",
                    "Beat eggs, cook in a pan, add cheese, and fold.",
                    10,
                    new String[]{"eggs", "cheese"},
                    new double[]{2, 30},
                    new String[]{"pieces", "g"});

            //insert banana smoothie recipe
            addRecipe(db, "Banana Smoothie",
                    "Creamy banana smoothie",
                    "Blend banana and milk until smooth.",
                    5,
                    new String[]{"banana", "milk"},
                    new double[]{1, 250},
                    new String[]{"pieces", "ml"});

            //insert garlic bread recipe
            addRecipe(db, "Garlic Bread",
                    "Toasted garlic bread",
                    "Mix crushed garlic with butter. Spread on bread and toast.",
                    15,
                    new String[]{"bread", "garlic", "butter"},
                    new double[]{2, 1, 20},
                    new String[]{"slices", "pieces", "g"});

            //insert chicken stir-fry recipe
            addRecipe(db, "Chicken Stir-Fry",
                    "Chicken with vegetables",
                    "Cook chicken thoroughly. Add vegetables and stir-fry until tender.",
                    25,
                    new String[]{"chicken", "carrots", "onion"},
                    new double[]{200, 1, 1},
                    new String[]{"g", "pieces", "pieces"});

            //insert mashed potato recipe
            addRecipe(db, "Mashed Potato",
                    "Creamy mashed potatoes",
                    "Boil potatoes until soft, then mash with butter and milk.",
                    25,
                    new String[]{"potatoes", "butter", "milk"},
                    new double[]{300, 20, 50},
                    new String[]{"g", "g", "ml"});

            //insert french toast recipe
            addRecipe(db, "French Toast",
                    "Golden French toast",
                    "Dip bread in beaten eggs and milk. Fry until golden.",
                    15,
                    new String[]{"bread", "eggs", "milk"},
                    new double[]{2, 2, 60},
                    new String[]{"slices", "pieces", "ml"});

            //insert ham and cheese sandwich recipe
            addRecipe(db, "Ham & Cheese Sandwich",
                    "Toasted ham & cheese sandwich",
                    "Place ham and cheese between bread slices and toast until cheese is melted.",
                    5,
                    new String[]{"bread", "ham", "cheese"},
                    new double[]{2, 2, 40},
                    new String[]{"slices", "slices", "g"});

            //insert apple oatmeal recipe
            addRecipe(db, "Apple Oatmeal",
                    "Warm oats with apple",
                    "Cook oats in milk or water. Top with chopped apple.",
                    10,
                    new String[]{"oats", "apple"},
                    new double[]{50, 1},
                    new String[]{"g", "pieces"});

            //insert vegetable soup recipe
            addRecipe(db, "Vegetable Soup",
                    "Simple vegetable soup",
                    "Chop vegetables, simmer in water until soft, and season.",
                    35,
                    new String[]{"carrots", "potatoes", "onion"},
                    new double[]{2, 200, 1},
                    new String[]{"pieces", "g", "pieces"});

            //insert cheese sandwich recipe
            addRecipe(db, "Cheese Sandwich",
                    "Toasted cheese sandwich",
                    "Place cheese between bread slices and toast until cheese is melted.",
                    10,
                    new String[]{"bread", "cheese"},
                    new double[]{2, 40},
                    new String[]{"slices", "g"});

            //insert banana pancakes recipe
            addRecipe(db, "Banana Pancakes",
                    "Pancakes with banana",
                    "Mash banana, mix with eggs and flour, then cook in a pan.",
                    15,
                    new String[]{"banana", "eggs", "flour"},
                    new double[]{1, 2, 100},
                    new String[]{"pieces", "pieces", "g"});

            //insert baked potatoes recipe
            addRecipe(db, "Baked Potatoes",
                    "Oven-baked potatoes",
                    "Wash potatoes, pierce their skins, and bake until tender.",
                    45,
                    new String[]{"potatoes"},
                    new double[]{300},
                    new String[]{"g"});

            //insert coleslaw salad recipe
            addRecipe(db, "Coleslaw Salad",
                    "Fresh coleslaw salad",
                    "Wash and grate carrots, cabbage and lettuce. Mix with mayo.",
                    10,
                    new String[]{"carrots", "cabbage", "lettuce", "mayo"},
                    new double[]{2, 100, 100, 30},
                    new String[]{"pieces", "g", "g", "g"});

            //insert fried rice recipe
            addRecipe(db, "Fried Rice",
                    "Rice stir-fried with egg",
                    "Cook rice. Scramble eggs, add rice, and stir-fry thoroughly.",
                    20,
                    new String[]{"rice", "eggs", "onion"},
                    new double[]{200, 2, 1},
                    new String[]{"g", "pieces", "pieces"});

            //insert apple smoothie recipe
            addRecipe(db, "Apple Smoothie",
                    "Refreshing apple smoothie",
                    "Blend apple and milk until smooth.",
                    5,
                    new String[]{"apple", "milk"},
                    new double[]{1, 200},
                    new String[]{"pieces", "ml"});

            //insert butter pasta recipe
            addRecipe(db, "Butter Pasta",
                    "Pasta tossed with butter",
                    "Boil pasta until tender, drain, and mix with butter.",
                    15,
                    new String[]{"pasta", "butter"},
                    new double[]{200, 20},
                    new String[]{"g", "g"});

            //insert ham and cheese omelette recipe
            addRecipe(db, "Ham & Cheese Omelette",
                    "Egg omelette with ham and cheese",
                    "Beat eggs, add chopped ham and grated cheese, and cook in a pan until set.",
                    12,
                    new String[]{"eggs", "ham", "cheese"},
                    new double[]{2, 1, 40},
                    new String[]{"pieces", "pieces", "g"});

            //mark transaction successful after all recipes are inserted
            db.setTransactionSuccessful();
        } finally {
            //finish transaction and close database
            db.endTransaction();
            db.close();
        }
    }

    //insert one recipe and its ingredient requirements
    private void addRecipe(SQLiteDatabase db, String name, String description, String instructions, int cookingTime, String[] ingredientNames, double[] quantities, String[] units) {
        //store recipe details before insertion
        ContentValues recipeValues = new ContentValues();
        recipeValues.put("name", name);
        recipeValues.put("description", description);
        recipeValues.put("instructions", instructions);
        recipeValues.put("cooking_time", cookingTime);

        //insert recipe and retrieve generated id
        long recipeId = db.insert("recipes", null, recipeValues);

        //stop transaction if recipe insertion fails
        if (recipeId == -1) {
            throw new IllegalStateException("Could not insert recipe: " + name);
        }

        //insert each ingredient linked to the new recipe
        for (int i = 0; i < ingredientNames.length; i++) {
            ContentValues ingredientValues = new ContentValues();
            ingredientValues.put("recipe_id", recipeId);
            ingredientValues.put("ingredient_name", ingredientNames[i]);
            ingredientValues.put("required_quantity", quantities[i]);
            ingredientValues.put("unit", units[i]);

            //insert ingredient requirement into database
            long result = db.insert("recipe_ingredients", null, ingredientValues);

            //stop transaction if ingredient insertion fails
            if (result == -1) {
                throw new IllegalStateException("Could not insert ingredient for: " + name);
            }
        }
    }
}