package com.example.orderpage;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;

public class AddressActivity extends AppCompatActivity implements OnMapReadyCallback {
    private ConnectionHelper connectionHelper;
    private int orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.address);
        connectionHelper = new ConnectionHelper();

        // Get the order ID from the intent
        orderId = getIntent().getIntExtra("orderId", -1); // Default value is -1 if not provided

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Fetch address from database using orderId
        String address = getAddressFromDatabase(orderId);

        if (address != null) {
            // Convert address to LatLng using Geocoding (not implemented here)
            LatLng location = getLocationFromAddress(address);

            // Add a marker at the address location and move the camera
            if (location != null) {
                googleMap.addMarker(new MarkerOptions().position(location).title(address));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
            }
        }
    }

    private String getAddressFromDatabase(int orderId) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String address = null;

        try {
            // Establish a database connection
            connection = connectionHelper.connectionclass();

            // Prepare SQL query
            String query = "SELECT Address FROM ConfirmOrder WHERE OrderID = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, orderId); // Set orderId parameter in the query

            // Execute the query
            resultSet = preparedStatement.executeQuery();

            // Check if a row is returned
            if (resultSet.next()) {
                // Retrieve the address value from the result set
                address = resultSet.getString("Address");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close resources
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return address;
    }

    private LatLng getLocationFromAddress(String address) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        LatLng location = null;
        try {
            addresses = geocoder.getFromLocationName(address, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address fetchedAddress = addresses.get(0);
                location = new LatLng(fetchedAddress.getLatitude(), fetchedAddress.getLongitude());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return location;
    }
}