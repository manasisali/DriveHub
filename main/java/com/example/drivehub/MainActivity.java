package com.example.drivehub;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;


import com.example.drivehub.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Find the Allow and Don't Allow buttons
        Button allowButton = findViewById(R.id.btnAllow);
        Button dontAllowButton = findViewById(R.id.btnDontAllow);

        // Set OnClickListener for Allow Button
        allowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Display a toast and navigate to the GPS Permission screen
                Toast.makeText(MainActivity.this, "Your notification is turned on", Toast.LENGTH_SHORT).show();
                navigateToGpsPermissionScreen();
            }
        });


        // Set OnClickListener for Don't Allow Button
        dontAllowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Display a toast and navigate to the GPS Permission screen
                Toast.makeText(MainActivity.this, "Notifications disabled", Toast.LENGTH_SHORT).show();
                navigateToGpsPermissionScreen();
            }
        });
    }

    // Method to navigate to the GPS permission screen
    private void navigateToGpsPermissionScreen() {


        Intent intent = new Intent(MainActivity.this, GpsPermissionActivity.class);
        startActivity(intent);

    }
}
