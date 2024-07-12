package com.example.orderpage;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class invent extends AppCompatActivity {

    private ConnectionHelper connectionHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invent);

        connectionHelper = new ConnectionHelper();

        // Fetch inventory items from the database
        fetchInventoryItems();
    }

    private void fetchInventoryItems() {
        Connection connection = null;
        try {
            connection = connectionHelper.connectionclass();
            if (connection != null) {
                String query = "SELECT ItemName, Count FROM Inventory";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    ResultSet resultSet = preparedStatement.executeQuery();

                    ArrayList<InventoryItem> inventoryItems = new ArrayList<>();

                    while (resultSet.next()) {
                        String itemName = resultSet.getString("ItemName");
                        int count = resultSet.getInt("Count");
                        inventoryItems.add(new InventoryItem(itemName, count));
                    }

                    // Display inventory items
                    displayInventoryItems(inventoryItems);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void displayInventoryItems(ArrayList<InventoryItem> inventoryItems) {
        TableLayout inventoryTableLayout = findViewById(R.id.inventoryTableLayout);

        // Loop through the inventory items and add them to the table layout
        for (InventoryItem item : inventoryItems) {
            TableRow row = new TableRow(this);
            TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT
            );
            row.setLayoutParams(layoutParams);

            TextView itemNameTextView = new TextView(this);
            itemNameTextView.setText(item.getItemName());
            itemNameTextView.setPadding(8, 8, 8, 8);
            itemNameTextView.setTextColor(Color.BLACK);
            itemNameTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
            row.addView(itemNameTextView);

            TextView itemCountTextView = new TextView(this);
            itemCountTextView.setText(String.valueOf(item.getCount()));
            itemCountTextView.setPadding(8, 8, 8, 8);
            itemCountTextView.setTextColor(Color.BLACK);
            itemCountTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
            itemCountTextView.setGravity(Gravity.CENTER);
            row.addView(itemCountTextView);

            inventoryTableLayout.addView(row);
        }
    }
}
