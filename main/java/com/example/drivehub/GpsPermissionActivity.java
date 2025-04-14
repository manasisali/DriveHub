package com.example.drivehub;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import com.google.android.gms.maps.model.Polyline;
public class GpsPermissionActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private AutoCompleteTextView etFromLocation; // "From" field to show user's current location
    private AutoCompleteTextView etToLocation; // "To" field for destination
    private Button btnSubmitLocations; // Submit button

    private LatLng currentLatLng; // User's current location
    private LatLng destinationLatLng; // Destination location

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps_permission);

        etFromLocation = findViewById(R.id.etFromLocation);
        etToLocation = findViewById(R.id.etToLocation);
        btnSubmitLocations = findViewById(R.id.btnSubmitLocations);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        requestGpsPermission();

        // Set up button click listener for the "Go" button
        btnSubmitLocations.setOnClickListener(v -> performGoAction());

        // Set up "Enter" key to trigger the "Go" button functionality and hide the keyboard
        etToLocation.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_DONE ||
                    (event != null && event.getKeyCode() == android.view.KeyEvent.KEYCODE_ENTER)) {
                performGoAction(); // Trigger the same functionality as the "Go" button
                hideKeyboard();   // Dismiss the keyboard
                return true;      // Consume the event so it doesn't move to the next line
            }
            return false;
        });

    }


    // Method to hide the keyboard
    private void hideKeyboard() {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if (imm != null && getCurrentFocus() != null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        } catch (Exception e) {
            e.printStackTrace(); // Log the exception for debugging
        }
    }

//    private void performGoAction() {
//        String destination = etToLocation.getText().toString().trim();
//        if (!destination.isEmpty() && currentLatLng != null) {
//            getLocationFromAddress(destination); // Get destination coordinates
//        } else {
//            Toast.makeText(this, "Please enter a destination.", Toast.LENGTH_SHORT).show();
//        }
//}

private void performGoAction() {
    String source = etFromLocation.getText().toString().trim();
    String destination = etToLocation.getText().toString().trim();

    if (!source.isEmpty() && !destination.isEmpty()) {
        Intent intent = new Intent(GpsPermissionActivity.this, FareActivity.class);
        intent.putExtra("source", source);
        intent.putExtra("destination", destination);
        startActivity(intent);
    } else {
        Toast.makeText(this, "Please enter both source and destination.", Toast.LENGTH_SHORT).show();
    }
}

    // Request GPS (location) permission
    private void requestGpsPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            // Permission already granted
            fetchUserLocation();
        }
    }

    // Handle permission request results
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                fetchUserLocation();
            } else {
                // Permission denied
                Toast.makeText(this, "GPS permission denied.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Fetch user's current location
    private void fetchUserLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    // Get user's current location
                    currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());

                    // Place a marker at the user's location on the map
                    if (mMap != null) {
                        mMap.addMarker(new MarkerOptions().position(currentLatLng).title("Your Location"));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15));
                    }

                    // Fetch and display the address in the "From" field
                    getAddressFromLocation(currentLatLng);
                } else {
                    Toast.makeText(GpsPermissionActivity.this, "Unable to fetch location.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Convert latitude and longitude to a readable address
//    private void getAddressFromLocation(LatLng latLng) {
//        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
//        try {
//            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
//            if (addresses != null && !addresses.isEmpty()) {
//                String address = addresses.get(0).getAddressLine(0); // Full address
//                etFromLocation.setText(address); // Set the address in the "From" field
//            } else {
//                etFromLocation.setText(String.format(Locale.getDefault(), "%f, %f", latLng.latitude, latLng.longitude));
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            etFromLocation.setText(String.format(Locale.getDefault(), "%f, %f", latLng.latitude, latLng.longitude));
//        }
//    }

    private void getAddressFromLocation(LatLng latLng) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                String locationName = "";

                // Try to get the most relevant name (sub-locality or locality)
                if (address.getSubLocality() != null) {
                    locationName = address.getSubLocality(); // Get area name (e.g., Dadar West)
                } else if (address.getLocality() != null) {
                    locationName = address.getLocality(); // Get city name (e.g., Thane)
                }

                // Remove "West", "East", "North", "South" if present
                if (locationName != null) {
                    locationName = locationName.replaceAll("\\b(West|East|North|South)\\b", "").trim();
                }

                etFromLocation.setText(locationName); // Set the cleaned area name
            } else {
                etFromLocation.setText(String.format(Locale.getDefault(), "%f, %f", latLng.latitude, latLng.longitude));
            }
        } catch (IOException e) {
            e.printStackTrace();
            etFromLocation.setText(String.format(Locale.getDefault(), "%f, %f", latLng.latitude, latLng.longitude));
        }
    }



    // Convert the destination address to LatLng
    private void getLocationFromAddress(String address) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocationName(address, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address destinationAddress = addresses.get(0);
                destinationLatLng = new LatLng(destinationAddress.getLatitude(), destinationAddress.getLongitude());

                // Place a marker at the destination
                mMap.addMarker(new MarkerOptions().position(destinationLatLng).title("Destination"));

                // Draw route (polyline) from source to destination
                drawRoute();
            } else {
                Toast.makeText(this, "Destination not found.", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Unable to get destination location.", Toast.LENGTH_SHORT).show();
        }
    }

    private Polyline currentPolyline;
    private void drawRoute() {
        if (currentLatLng != null && destinationLatLng != null) {
            // Remove the existing polyline if it exists
            if (currentPolyline != null) {
                currentPolyline.remove();
            }

            // Draw a new blue line (polyline) between the source and destination
            PolylineOptions polylineOptions = new PolylineOptions()
                    .add(currentLatLng)
                    .add(destinationLatLng)
                    .width(10)
                    .color(getResources().getColor(android.R.color.holo_blue_dark));
            currentPolyline = mMap.addPolyline(polylineOptions); // Save reference to the new polyline

            // Move camera to show the route
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(destinationLatLng, 15));
        }
    }




    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

//        // Default location (e.g., Mumbai) if user location is not available
//        LatLng defaultLocation = new LatLng(19.07, 72.87);
//        mMap.addMarker(new MarkerOptions().position(defaultLocation).title("Default Location"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 12));

        // Check if permission is already granted, and if so, fetch user location
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            fetchUserLocation();
        }
    }
}

