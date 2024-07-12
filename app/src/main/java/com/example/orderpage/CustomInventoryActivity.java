package com.example.orderpage;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CustomInventoryActivity extends AppCompatActivity {

    private String[] itemNames = {"Brown hen", "Hamsphire", "Broiler", "Ancona", "Country Chicken Egg", "Turkey"};
    private Spinner itemNameSpinner;
    private EditText quantityEditText;
    private ImageView incrementImageView;
    private ImageView decrementImageView;
    private ConnectionHelper connectionHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_inventory);

        itemNameSpinner = findViewById(R.id.itemNameSpinner);
        quantityEditText = findViewById(R.id.quantityEditText);
        incrementImageView = findViewById(R.id.incrementImageView);
        decrementImageView = findViewById(R.id.decrementImageView);
        connectionHelper = new ConnectionHelper();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, itemNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        itemNameSpinner.setAdapter(adapter);

        incrementImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateCount(true);
                animateImageView(v);
            }
        });

        decrementImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateCount(false);
                animateImageView(v);
            }
        });
    }

    private void updateCount(boolean isIncrement) {
        String itemName = itemNameSpinner.getSelectedItem().toString();
        int quantity = Integer.parseInt(quantityEditText.getText().toString());

        // Adjust quantity to increment or decrement
        quantity = isIncrement ? quantity : -quantity;

        try {
            Connection connection = connectionHelper.connectionclass();
            if (connection != null) {
                // Prepare SQL statement to update count
                String query = "UPDATE Inventory SET Count = Count + ? WHERE ItemName = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1, quantity);
                preparedStatement.setString(2, itemName);

                // Execute SQL statement
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    // Update successful
                    Log.d("Update Count", "Rows affected: " + rowsAffected);
                    showToast("Value updated successfully");
                } else {
                    // Update failed
                    Log.e("Update Count", "No rows affected");
                    showToast("Failed to update value");
                }

                // Close connection and resources
                preparedStatement.close();
                connection.close();
            } else {
                Log.e("Update Count", "Connection is null");
                showToast("Failed to connect to database");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Log.e("Update Count", "SQL Error: " + e.getMessage());
            showToast("SQL Error: " + e.getMessage());
        } catch (NumberFormatException e) {
            e.printStackTrace();
            Log.e("Update Count", "Invalid quantity: " + e.getMessage());
            showToast("Invalid quantity: " + e.getMessage());
        }
    }

    private void animateImageView(View view) {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.image_click_anim);
        view.startAnimation(animation);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
