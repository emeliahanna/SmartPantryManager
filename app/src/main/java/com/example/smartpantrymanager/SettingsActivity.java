
package com.example.smartpantrymanager;
//imports
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {
    //variables
    private CheckBox checkExpiryAlerts;
    private SharedPreferences preferences;

    @Override //called when first created
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        checkExpiryAlerts = findViewById(R.id.checkExpiryAlerts); //checkbox connected

        preferences = getSharedPreferences("SmartPantrySettings", MODE_PRIVATE); //shared preferences for storing settings

        //load the previously saved setting
        boolean alertsEnabled = preferences.getBoolean("expiry_alerts_enabled", false);
        checkExpiryAlerts.setChecked(alertsEnabled);

        //save the setting when button is clicked
        findViewById(R.id.buttonSaveSettings).setOnClickListener(v -> {
            preferences.edit().putBoolean("expiry_alerts_enabled", checkExpiryAlerts.isChecked()).apply();
            Toast.makeText(this, "Settings saved.", Toast.LENGTH_SHORT).show(); //message for confirmation
        });

        //return to the previous screen
        findViewById(R.id.buttonBackToPantry).setOnClickListener(v -> finish());
    }
}