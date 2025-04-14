//package com.example.drivehub;
//
//import android.os.Bundle;
//import android.widget.TextView;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.util.ArrayList;
//import java.util.List;
//
//public class FareActivity extends AppCompatActivity {
//    private RecyclerView recyclerView;
//    private FareAdapter fareAdapter;
//    private List<Fare> fareList;
//    private TextView tvSource, tvDestination;  // Added TextView for Source and Destination
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_fare);
//
//        // Initialize Views
//        tvSource = findViewById(R.id.tvSource);
//        tvDestination = findViewById(R.id.tvDestination);
//        recyclerView = findViewById(R.id.recyclerView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//        fareList = new ArrayList<>();
//
//        // Get data from intent
//        String source = getIntent().getStringExtra("source");
//        String destination = getIntent().getStringExtra("destination");
//
//        // Set Source and Destination at the top
//        tvSource.setText("Source: " + source);
//        tvDestination.setText("Destination: " + destination);
//
//        // Find fare from CSV file
//        findFare(source, destination);
//    }
//
//    private void findFare(String source, String destination) {
//        InputStream inputStream = getResources().openRawResource(R.raw.cab_fare);
//        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
//        String line;
//
//        try {
//            boolean found = false;
//            while ((line = reader.readLine()) != null) {
//                String[] row = line.split(",");
//                if (row.length == 5) { // CSV should have 5 columns
//                    String service = row[0].trim();      // Ola / Uber
//                    String cabType = row[1].trim();      // Mini / Sedan / Auto / SUV
//                    String csvSource = row[2].trim();    // Ghatkopar, etc.
//                    String csvDestination = row[3].trim(); // Marine Lines, etc.
//                    String fare = row[4].trim();         // ₹450, etc.
//
//                    if (csvSource.equalsIgnoreCase(source) && csvDestination.equalsIgnoreCase(destination)) {
//                        // Only add Service, Cab Type, and Fare (Source & Destination are already displayed on top)
//                        fareList.add(new Fare(service, cabType, fare));
//                        found = true;
//                    }
//                }
//            }
//            if (!found) {
//                fareList.add(new Fare("Ola", "Not Available", "N/A"));
//                fareList.add(new Fare("Uber", "Not Available", "N/A"));
//            }
//            fareAdapter = new FareAdapter(fareList);
//            recyclerView.setAdapter(fareAdapter);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}

package com.example.drivehub;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FareActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FareAdapter fareAdapter;
    private List<Fare> fareList;
    private TextView tvSource, tvDestination;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fare);

        // Initialize Views
        tvSource = findViewById(R.id.tvSource);
        tvDestination = findViewById(R.id.tvDestination);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fareList = new ArrayList<>();

        // Get data from intent
        String source = getIntent().getStringExtra("source");
        String destination = getIntent().getStringExtra("destination");

        // Set Source and Destination at the top
        tvSource.setText("Source: " + source);
        tvDestination.setText("Destination: " + destination);

        // Find fare from CSV file
        findFare(source, destination);
    }

    private void findFare(String source, String destination) {
        InputStream inputStream = getResources().openRawResource(R.raw.cab_fare);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;

        try {
            boolean found = false;
            while ((line = reader.readLine()) != null) {
                String[] row = line.split(",");
                if (row.length == 5) { // CSV should have 5 columns
                    String service = row[0].trim();      // Ola / Uber
                    String cabType = row[1].trim();      // Mini / Sedan / Auto / SUV
                    String csvSource = row[2].trim();    // Ghatkopar, etc.
                    String csvDestination = row[3].trim(); // Marine Lines, etc.
                    String fare = row[4].trim();         // ₹450, etc.

                    if (csvSource.equalsIgnoreCase(source) && csvDestination.equalsIgnoreCase(destination)) {
                        fareList.add(new Fare(service, cabType, fare));
                        found = true;
                    }
                }
            }

            if (!found) {
                fareList.add(new Fare("Ola", "Not Available", "N/A"));
                fareList.add(new Fare("Uber", "Not Available", "N/A"));
            } else {
                // ✅ Sort fareList by price (Ignoring "₹" symbol and handling "N/A")
                Collections.sort(fareList, new Comparator<Fare>() {
                    @Override
                    public int compare(Fare f1, Fare f2) {
                        int price1 = extractFareAmount(f1.getFareAmount());
                        int price2 = extractFareAmount(f2.getFareAmount());
                        return Integer.compare(price1, price2); // Ascending Order
                    }
                });
            }

            fareAdapter = new FareAdapter(FareActivity.this, fareList);
            recyclerView.setAdapter(fareAdapter);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ✅ Function to extract integer value from fare string
    private int extractFareAmount(String fare) {
        try {
            // Remove ₹ symbol and non-numeric characters, then parse to integer
            return Integer.parseInt(fare.replaceAll("[^0-9]", ""));
        } catch (NumberFormatException e) {
            return Integer.MAX_VALUE; // If fare is "N/A", place it at the bottom
        }
    }
}

