package com.example.orderpage;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Monitoring extends AppCompatActivity {

    private DatabaseReference databaseReference;

    private GasGaugeView airQualityGauge;
    private GasGaugeView hydrogenSulphideGauge;
    private GasGaugeView ammoniaGauge;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.monitoring);

        // Initialize GasGaugeViews
        airQualityGauge = findViewById(R.id.airQualityGauge);
        hydrogenSulphideGauge = findViewById(R.id.hydrogenSulphideGauge);
        ammoniaGauge = findViewById(R.id.ammoniaGauge);

        // Set colors for the gauges
        airQualityGauge.setArcColor(Color.GREEN);
        hydrogenSulphideGauge.setArcColor(Color.BLUE);
        ammoniaGauge.setArcColor(Color.YELLOW);

        // Get reference to the Firebase database
        databaseReference = FirebaseDatabase.getInstance().getReference();

        // Retrieve and display Air Quality value and indicator
        retrieveAndDisplayValues("Air_Quality", airQualityGauge);

        // Retrieve and display Hydrogen Sulphide value and indicator
        retrieveAndDisplayValues("Hydrogen_Sulphide", hydrogenSulphideGauge);

        // Retrieve and display Ammonia value and indicator
        retrieveAndDisplayValues("Ammonia", ammoniaGauge);
    }

    private void retrieveAndDisplayValues(String childName, final GasGaugeView gaugeView) {
        DatabaseReference childReference = databaseReference.child(childName);
        childReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.hasChild("value") && dataSnapshot.hasChild("indicator")) {
                    Object value = dataSnapshot.child("value").getValue();
                    Object indicator = dataSnapshot.child("indicator").getValue();
                    if (value instanceof Long && indicator instanceof String) {
                        long gasValue = (long) value;
                        String indicatorText = (String) indicator;
                        gaugeView.setGasValue((int) gasValue);
                        gaugeView.setIndicatorText(indicatorText);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
            }
        });
    }
}