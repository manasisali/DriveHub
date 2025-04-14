//package com.example.drivehub;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Handler;
//import androidx.appcompat.app.AppCompatActivity;
//
//public class SplashActivity extends AppCompatActivity {
//
//    private static final int SPLASH_DISPLAY_LENGTH = 2000; //  1seconds
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_splash);
//
//        // Delay for 10 seconds, then start the main activity
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                // Start GpsPermissionActivity after the splash screen
//                Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
//                startActivity(mainIntent);
//                finish(); // Close the SplashActivity
//            }
//        }, SPLASH_DISPLAY_LENGTH);
//    }
//
//}

package com.example.drivehub;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DISPLAY_LENGTH = 2000; // 2 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Add animation to the ImageView
        ImageView carImage = findViewById(R.id.carImage); // Ensure the ID matches the one in your layout
        Animation fadeInScale = AnimationUtils.loadAnimation(this, R.transition.fade_in_scale);
        carImage.startAnimation(fadeInScale);

        // Delay for 2 seconds, then start the main activity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Start MainActivity after the splash screen
                Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(mainIntent);
                finish(); // Close the SplashActivity
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
