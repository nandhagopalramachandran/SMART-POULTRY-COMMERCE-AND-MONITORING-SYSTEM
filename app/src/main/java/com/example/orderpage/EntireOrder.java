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

public class EntireOrder extends AppCompatActivity {

    private ConnectionHelper connectionHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entireorder);

        connectionHelper = new ConnectionHelper();
        displayOrderDetails();
    }

    private void displayOrderDetails() {
        Connection connection = null;
        try {
            connection = connectionHelper.connectionclass();
            if (connection != null) {
                String query = "SELECT OrderID, ItemName, Quantity, Cost, TotalCost FROM OrderDetails";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    ResultSet resultSet = preparedStatement.executeQuery();

                    TableLayout orderTableLayout = findViewById(R.id.orderTableLayout);

                    while (resultSet.next()) {
                        int orderId = resultSet.getInt("OrderID");
                        String itemName = resultSet.getString("ItemName");
                        int quantity = resultSet.getInt("Quantity");
                        int cost = resultSet.getInt("Cost");
                        int totalCost = resultSet.getInt("TotalCost");

                        TableRow row = new TableRow(this);
                        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(
                                TableRow.LayoutParams.WRAP_CONTENT,
                                TableRow.LayoutParams.WRAP_CONTENT
                        );
                        row.setLayoutParams(layoutParams);

                        TextView orderIdTextView = new TextView(this);
                        orderIdTextView.setText(String.valueOf(orderId));
                        orderIdTextView.setPadding(8, 8, 8, 8);
                        orderIdTextView.setTextColor(Color.BLACK);
                        orderIdTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                        row.addView(orderIdTextView);

                        TextView itemNameTextView = new TextView(this);
                        itemNameTextView.setText(itemName);
                        itemNameTextView.setPadding(8, 8, 8, 8);
                        itemNameTextView.setTextColor(Color.BLACK);
                        itemNameTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                        row.addView(itemNameTextView);

                        TextView quantityTextView = new TextView(this);
                        quantityTextView.setText(String.valueOf(quantity));
                        quantityTextView.setPadding(8, 8, 8, 8);
                        quantityTextView.setTextColor(Color.BLACK);
                        quantityTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f)); // Adjust weight to center text
                        quantityTextView.setGravity(Gravity.CENTER); // Align text to center
                        row.addView(quantityTextView);

                        TextView costTextView = new TextView(this);
                        costTextView.setText(String.valueOf(cost));
                        costTextView.setPadding(8, 8, 8, 8);
                        costTextView.setTextColor(Color.BLACK);
                        costTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                        costTextView.setGravity(Gravity.CENTER);
                        row.addView(costTextView);

                        TextView totalCostTextView = new TextView(this);
                        totalCostTextView.setText(String.valueOf(totalCost));
                        totalCostTextView.setPadding(8, 8, 8, 8);
                        totalCostTextView.setTextColor(Color.BLACK);
                        totalCostTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                        totalCostTextView.setGravity(Gravity.CENTER);
                        row.addView(totalCostTextView);

                        orderTableLayout.addView(row);
                    }
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
}
