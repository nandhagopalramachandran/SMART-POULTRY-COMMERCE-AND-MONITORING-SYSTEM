package com.example.orderpage;

import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderHistoryActivity extends AppCompatActivity {

    private ConnectionHelper connectionHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        connectionHelper = new ConnectionHelper();
        displayOrderHistory();
    }

    private void displayOrderHistory() {
        Connection connection = null;
        try {
            connection = connectionHelper.connectionclass();
            if (connection != null) {
                String query = "SELECT OrderID, OrderDate, Name, MobileNumber, ItemName, Quantity, Cost, TotalCost, Address FROM OrderHistory";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    ResultSet resultSet = preparedStatement.executeQuery();

                    TableLayout orderHistoryTableLayout = findViewById(R.id.orderHistoryTableLayout);

                    while (resultSet.next()) {
                        int orderId = resultSet.getInt("OrderID");
                        String orderDate = resultSet.getString("OrderDate");
                        String name = resultSet.getString("Name");
                        String mobileNumber = resultSet.getString("MobileNumber");
                        String itemName = resultSet.getString("ItemName");
                        int quantity = resultSet.getInt("Quantity");
                        double cost = resultSet.getDouble("Cost");
                        double totalCost = resultSet.getDouble("TotalCost");
                        String address = resultSet.getString("Address");

                        TableRow row = new TableRow(this);
                        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(
                                TableRow.LayoutParams.WRAP_CONTENT,
                                TableRow.LayoutParams.WRAP_CONTENT
                        );
                        row.setLayoutParams(layoutParams);

                        TextView orderIdTextView = new TextView(this);
                        orderIdTextView.setText(String.valueOf(orderId));
                        orderIdTextView.setPadding(8, 8, 8, 8);
                        row.addView(orderIdTextView);

                        TextView orderDateTextView = new TextView(this);
                        orderDateTextView.setText(orderDate);
                        orderDateTextView.setPadding(8, 8, 8, 8);
                        row.addView(orderDateTextView);

                        TextView nameTextView = new TextView(this);
                        nameTextView.setText(name);
                        nameTextView.setPadding(8, 8, 8, 8);
                        row.addView(nameTextView);

                        TextView mobileNumberTextView = new TextView(this);
                        mobileNumberTextView.setText(mobileNumber);
                        mobileNumberTextView.setPadding(8, 8, 8, 8);
                        row.addView(mobileNumberTextView);

                        TextView itemNameTextView = new TextView(this);
                        itemNameTextView.setText(itemName);
                        itemNameTextView.setPadding(8, 8, 8, 8);
                        row.addView(itemNameTextView);

                        TextView quantityTextView = new TextView(this);
                        quantityTextView.setText(String.valueOf(quantity));
                        quantityTextView.setPadding(8, 8, 8, 8);
                        row.addView(quantityTextView);

                        TextView costTextView = new TextView(this);
                        costTextView.setText(String.valueOf(cost));
                        costTextView.setPadding(8, 8, 8, 8);
                        row.addView(costTextView);

                        TextView totalCostTextView = new TextView(this);
                        totalCostTextView.setText(String.valueOf(totalCost));
                        totalCostTextView.setPadding(8, 8, 8, 8);
                        row.addView(totalCostTextView);

                        TextView addressTextView = new TextView(this);
                        addressTextView.setText(address);
                        addressTextView.setPadding(8, 8, 8, 8);
                        row.addView(addressTextView);

                        orderHistoryTableLayout.addView(row);
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
