package com.example.smartpantrymanager;

//imports for activity setup and user input
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.app.DatePickerDialog;

//imports for working with dates and formatting them
import java.util.Calendar;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;

public class AddIngredientActivity extends AppCompatActivity {

    //references to the ingredient form's input fields.
    private EditText editIngredientName;
    private EditText editQuantity;
    private EditText editUnit;
    private EditText editExpiryDate;

    //provide access to the sqlite db
    private PantryDatabaseHelper databaseHelper;

    //stores id of the ingredient being edited; -1 means no ingredient was selected
    private int ingredientId = -1;

    //indicate if form is being used to add or edit an ingredient
    private boolean isEditMode = false;

    //called when this activity is created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //connect this activity to its XML layout
        setContentView(R.layout.activity_add_ingredient);

        //connects the Java variables to the input fields in the xml layout
        editIngredientName = findViewById(R.id.editIngredientName);
        editQuantity = findViewById(R.id.editQuantity);
        editUnit = findViewById(R.id.editUnit);
        editExpiryDate = findViewById(R.id.editExpiryDate);

        //finds the Save and Cancel buttons
        Button buttonSaveIngredient = findViewById(R.id.buttonSaveIngredient);
        Button buttonCancel = findViewById(R.id.buttonCancel);

        //creates the db helper for saving and retrieving ingredients
        databaseHelper = new PantryDatabaseHelper(this);

        //returns the ingredient ID passed from MainActivity
        //iff no ID was passed, default value is -1
        ingredientId = getIntent().getIntExtra("ingredient_id", -1);

        //allow edit mode when a valid ingredient ID is received
        isEditMode = ingredientId != -1;

        //if editing, load the selected ingredient's existing information
        if (isEditMode) {
            loadIngredientForEditing();
        }

        //opens the date picker when the expiry-date field is tapped
        editExpiryDate.setOnClickListener(v -> {

            //get the current date to use as the initial date in the picker
            Calendar calendar = Calendar.getInstance();

            //creates a date picker dialog
            DatePickerDialog datePicker = new DatePickerDialog(AddIngredientActivity.this,

                    //runs when the user selects a date
                    (view, year, month, dayOfMonth) -> {
                        //formats the selected date as YYYY-MM-DD.
                        //month value is increased by one because months start at 0
                        String selectedDate = String.format(Locale.UK, "%04d-%02d-%02d", year, month + 1, dayOfMonth);
                        editExpiryDate.setText(selectedDate); //displays the selected date in the expiry-date field
                    },
                    //sets the date picker to today's date initially
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
            //display the date picker dialog
            datePicker.show();
        });
        //save the ingredient when the Save button is clicked
        buttonSaveIngredient.setOnClickListener(v -> saveIngredient());

        //closes the current screen when Cancel is clicked
        buttonCancel.setOnClickListener(v -> finish());
    }

    //validates form and adds or updates the ingredient in the database
    private void saveIngredient() {

        //reads the entered values and removes unnecessary spaces
        String name = editIngredientName.getText().toString().trim();
        String quantityText = editQuantity.getText().toString().trim();
        String unit = editUnit.getText().toString().trim();
        String expiryDate = editExpiryDate.getText().toString().trim();

        //checks that ingredient name is not empty
        if (TextUtils.isEmpty(name)) {
            editIngredientName.setError("Enter an ingredient name");
            editIngredientName.requestFocus();
            return; //stops saving until the field is completed
        }

        //checks that a quantity has been entered
        if (TextUtils.isEmpty(quantityText)) {
            editQuantity.setError("Enter a quantity");
            editQuantity.requestFocus();
            return;
        }

        //checks that a measurement unit has been entered
        if (TextUtils.isEmpty(unit)) {
            editUnit.setError("Enter a unit");
            editUnit.requestFocus();
            return;
        }

        //declares a numeric variable for the ingredient quantity
        double quantity;

        try {
            //convert quantity entered as text into a decimal number
            quantity = Double.parseDouble(quantityText);

            //rejects invalid numeric values and quantities that are zero or negative
            if (Double.isNaN(quantity) || Double.isInfinite(quantity) || quantity <= 0) {
                editQuantity.setError("Quantity must be greater than zero");
                editQuantity.requestFocus();
                return;
            }
        } catch (NumberFormatException e) {
            //displays error if the quantity cannot be converted into a number
            editQuantity.setError("Enter a valid number");
            editQuantity.requestFocus();
            return;
        }
        //create an Ingredient object using the validated form values
        //if no expiry date was entered, null is stored for that field
        Ingredient ingredient = new Ingredient(
                name,
                quantity,
                unit,
                TextUtils.isEmpty(expiryDate) ? null : expiryDate
        );
        //tracks whether the database operation succeeds
        boolean success;

        //updates the existing ingredient when in edit mode
        if (isEditMode) {

            //assigns the original database ID to the ingredient
            ingredient.setId(ingredientId);

            //return value greater than zero indicates that a row was updated
            success = databaseHelper.updateIngredient(ingredient) > 0;

        } else {

            //inserts a new ingredient; -1 indicates that insertion failed
            success = databaseHelper.addIngredient(ingredient) != -1;
        }

        //displays a success message and closes the form if saving worked
        if (success) {
            Toast.makeText(
                    this, isEditMode ? "Ingredient updated successfully" : "Ingredient saved successfully", Toast.LENGTH_SHORT).show();

            //reports successful completion to the calling activity
            setResult(RESULT_OK);

            //closes this screen and returns to the previous activity
            finish();

        } else {

            //inform user if the database operation failed
            Toast.makeText(this, "Could not save ingredient", Toast.LENGTH_SHORT).show();
        }
    }

    //retrieves the selected ingredient and fills in the form for editing
    private void loadIngredientForEditing() {
        //loops through the ingredients currently stored in the database
        for (Ingredient ingredient : databaseHelper.getAllIngredients()) {

            //finds ingredient whose ID matches the ID received from the Intent
            if (ingredient.getId() == ingredientId) {

                //displays existing ingredient details in the form
                editIngredientName.setText(ingredient.getName());
                editQuantity.setText(String.valueOf(ingredient.getQuantity()));
                editUnit.setText(ingredient.getUnit());

                //display expiry date if one was previously saved
                if (ingredient.getExpiryDate() != null) {
                    editExpiryDate.setText(ingredient.getExpiryDate());
                }

                //ensure Save button performs save/update operation
                findViewById(R.id.buttonSaveIngredient)
                        .setOnClickListener(v -> saveIngredient());

                return; //stops searching once ingredient is found
            }
        }
        //display an error and closes screen if ingredient no longer exists
        Toast.makeText(this, "Ingredient not found", Toast.LENGTH_SHORT).show();
        finish();
    }
}