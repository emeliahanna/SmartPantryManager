
package com.example.smartpantrymanager;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.app.DatePickerDialog;
import java.util.Calendar;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;

public class AddIngredientActivity extends AppCompatActivity {
    //variables
    private EditText editIngredientName;
    private EditText editQuantity;
    private EditText editUnit;
    private EditText editExpiryDate;
    private PantryDatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ingredient);

        editIngredientName = findViewById(R.id.editIngredientName);
        editQuantity = findViewById(R.id.editQuantity);
        editUnit = findViewById(R.id.editUnit);
        editExpiryDate = findViewById(R.id.editExpiryDate);

        Button buttonSaveIngredient = findViewById(R.id.buttonSaveIngredient);
        Button buttonCancel = findViewById(R.id.buttonCancel);

        databaseHelper = new PantryDatabaseHelper(this);

        editExpiryDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();

            DatePickerDialog datePicker = new DatePickerDialog(
                    AddIngredientActivity.this,
                    (view, year, month, dayOfMonth) -> {
                        String selectedDate = String.format(
                                Locale.UK, "%04d-%02d-%02d", year, month + 1, dayOfMonth);
                        editExpiryDate.setText(selectedDate);
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );

            datePicker.show();
        });

        buttonSaveIngredient.setOnClickListener(v -> saveIngredient());

        buttonCancel.setOnClickListener(v -> finish());
    }

    private void saveIngredient() {
        String name = editIngredientName.getText().toString().trim();
        String quantityText = editQuantity.getText().toString().trim();
        String unit = editUnit.getText().toString().trim();
        String expiryDate = editExpiryDate.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            editIngredientName.setError("Enter an ingredient name");
            editIngredientName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(quantityText)) {
            editQuantity.setError("Enter a quantity");
            editQuantity.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(unit)) {
            editUnit.setError("Enter a unit");
            editUnit.requestFocus();
            return;
        }

        double quantity;

        try {
            quantity = Double.parseDouble(quantityText);
            if (Double.isNaN(quantity) || Double.isInfinite(quantity) || quantity <= 0) {
                editQuantity.setError("Quantity must be greater than zero");
                editQuantity.requestFocus();
                return;
            }
        } catch (NumberFormatException e) {
            editQuantity.setError("Enter a valid number");
            editQuantity.requestFocus();
            return;
        }

        Ingredient ingredient = new Ingredient(name, quantity, unit, TextUtils.isEmpty(expiryDate) ? null : expiryDate);

        long result = databaseHelper.addIngredient(ingredient);

        if (result != -1) {
            Toast.makeText(this, "Ingredient saved successfully", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
        } else {
            Toast.makeText(this, "Could not save ingredient", Toast.LENGTH_SHORT).show();
        }
    }
}
