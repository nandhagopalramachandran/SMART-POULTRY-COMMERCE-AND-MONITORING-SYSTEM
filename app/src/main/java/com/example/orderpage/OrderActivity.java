package com.example.orderpage;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrderActivity extends AppCompatActivity {

    private ConnectionHelper connectionHelper;
    private Calculation shoppingCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        connectionHelper = new ConnectionHelper();
        shoppingCart = Calculation.getInstance();
        displayOrderDetails();

        Button orderConfirmButton = findViewById(R.id.confirmOrderButton);
        orderConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int totalCost = calculateTotalCost(shoppingCart);
                storeOrderInDatabase(shoppingCart, totalCost);
            }
        });
    }

    private void displayOrderDetails() {
        ArrayList<Hen> selectedItems = shoppingCart.getSelectedItems();
        TableLayout orderTableLayout = findViewById(R.id.orderTableLayout);

        int totalCost = 0;

        for (Hen hen : selectedItems) {
            TableRow row = new TableRow(this);
            TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT
            );
            row.setLayoutParams(layoutParams);

            TextView itemNameTextView = new TextView(this);
            itemNameTextView.setText(hen.getName());
            itemNameTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
            itemNameTextView.setPadding(16, 8, 16, 8);
            itemNameTextView.setTextColor(Color.BLACK);
            itemNameTextView.setTypeface(null, Typeface.BOLD);
            row.addView(itemNameTextView);

            TextView quantityTextView = new TextView(this);
            int quantity = shoppingCart.getQuantity(hen);
            quantityTextView.setText(String.valueOf(quantity));
            quantityTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
            quantityTextView.setPadding(16, 8, 16, 8);
            quantityTextView.setTextColor(Color.BLACK);
            quantityTextView.setTypeface(null, Typeface.BOLD);
            row.addView(quantityTextView);

            int itemCost = quantity * Integer.parseInt(hen.getPrice().replace(" ₹", ""));
            totalCost += itemCost;
            TextView costTextView = new TextView(this);
            costTextView.setText("₹" + itemCost);
            costTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
            costTextView.setPadding(16, 8, 16, 8);
            costTextView.setTextColor(Color.BLACK);
            costTextView.setTypeface(null, Typeface.BOLD);
            row.addView(costTextView);

            orderTableLayout.addView(row);
        }

        TextView totalCostTextView = findViewById(R.id.totalCostTextView);
        totalCostTextView.setText("Total Cost: ₹" + totalCost);
        totalCostTextView.setTextColor(Color.BLACK);
        totalCostTextView.setTypeface(null, Typeface.BOLD);
    }

    private int calculateTotalCost(Calculation shoppingCart) {
        int totalCost = 0;
        for (Hen hen : shoppingCart.getSelectedItems()) {
            int quantity = shoppingCart.getQuantity(hen);
            int itemCost = quantity * Integer.parseInt(hen.getPrice().replace(" ₹", ""));
            totalCost += itemCost;
        }
        return totalCost;
    }

    private void storeOrderInDatabase(Calculation shoppingCart, int totalCost) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Connection connection = null;
                try {
                    connection = connectionHelper.connectionclass();
                    if (connection != null) {
                        int orderId = getNextOrderId(connection);
                        insertCustomerDetails(connection, orderId); // Insert customer details
                        insertOrder(connection, shoppingCart.getSelectedItems(), totalCost, orderId);
                        updateOrderId(connection, orderId + 1); // Increment and update the order ID
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent(OrderActivity.this, OrderConfirmationActivity.class));
                            }
                        });
                    }
                } catch (Exception e) {
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
        }).start();
    }

    private int getNextOrderId(Connection connection) throws SQLException {
        String query = "SELECT OrdID FROM ID";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("OrdID");
            }
        }
        return 0; // Default if no entry exists
    }

    private void updateOrderId(Connection connection, int newOrderId) throws SQLException {
        String query = "UPDATE ID SET OrdID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, newOrderId);
            preparedStatement.executeUpdate();
        }
    }

    private void insertCustomerDetails(Connection connection, int orderId) throws SQLException {
        String name = getIntent().getStringExtra("Name");
        String mobileNumber = getIntent().getStringExtra("MobileNumber");
        String address = getIntent().getStringExtra("Address");

        String query = "INSERT INTO CustomerDetails (OrderID, Name, MobileNumber, address) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, orderId);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, mobileNumber);
            preparedStatement.setString(4, address);
            preparedStatement.executeUpdate();
        }
    }

    private void insertOrder(Connection connection, ArrayList<Hen> selectedItems, int totalCost, int orderId) throws SQLException {
        String query = "INSERT INTO OrderDetails (OrderID, ItemName, Quantity, Cost, TotalCost) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            for (Hen hen : selectedItems) {
                int quantity = shoppingCart.getQuantity(hen);
                int itemCost = quantity * Integer.parseInt(hen.getPrice().replace(" ₹", ""));
                preparedStatement.setInt(1, orderId); // Set the order ID
                preparedStatement.setString(2, hen.getName());
                preparedStatement.setInt(3, quantity);
                preparedStatement.setInt(4, itemCost);
                preparedStatement.setInt(5, totalCost);
                preparedStatement.executeUpdate();
            }
        }
    }
}
