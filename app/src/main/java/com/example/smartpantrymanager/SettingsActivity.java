
package com.example.smartpantrymanager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {
    //variables
    private CheckBox checkExpiryAlerts;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        checkExpiryAlerts = findViewById(R.id.checkExpiryAlerts);

        preferences = getSharedPreferences("SmartPantrySettings", MODE_PRIVATE);

        //load the previously saved setting
        boolean alertsEnabled = preferences.getBoolean("expiry_alerts_enabled", false);
        checkExpiryAlerts.setChecked(alertsEnabled);

        //save the setting
        findViewById(R.id.buttonSaveSettings).setOnClickListener(v -> {
            preferences.edit().putBoolean("expiry_alerts_enabled", checkExpiryAlerts.isChecked()).apply();
            Toast.makeText(this, "Settings saved.", Toast.LENGTH_SHORT).show();
        });

        //return to the previous screen
        findViewById(R.id.buttonBackToPantry).setOnClickListener(v -> finish());
    }
}