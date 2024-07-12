package com.example.orderpage;

import  android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SearchOrderActivity extends AppCompatActivity {

    private ConnectionHelper connectionHelper;
    private EditText searchEditText;
    private TableLayout orderTableLayout;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_order);

        connectionHelper = new ConnectionHelper();
        searchEditText = findViewById(R.id.searchEditText);
        orderTableLayout = findViewById(R.id.customerDetailsTableLayout);

    }

    public void onSearchButtonClick(View view) {
        String searchQuery = searchEditText.getText().toString();
        ArrayList<Order> searchResults = performSearch(searchQuery);
        displaySearchResults(searchResults);
    }

    public void onConfirmOrderButtonClick(View view) {
        String searchQuery = searchEditText.getText().toString();
        ArrayList<Order> searchResults = performSearch(searchQuery);

        if (!searchResults.isEmpty()) {
            for (Order order : searchResults) {
                insertConfirmedOrder(order);
            }
            // Show toast message
            showToast("Order confirmed successfully");
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }



    private ArrayList<Order> performSearch(String searchQuery) {
        Connection connection = connectionHelper.connectionclass();
        ArrayList<Order> searchResults = new ArrayList<>();

        if (connection != null) {
            String query = "SELECT OD.OrderID, CD.Name, CD.MobileNumber, CD.Address, OD.ItemName, OD.Quantity, OD.Cost, OD.TotalCost " +
                    "FROM OrderDetails OD " +
                    "INNER JOIN CustomerDetails CD ON OD.OrderID = CD.OrderID " +
                    "WHERE OD.OrderID = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, searchQuery);
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    int orderId = resultSet.getInt("OrderID");
                    String name = resultSet.getString("Name");
                    String mobileNumber = resultSet.getString("MobileNumber");
                    String address = resultSet.getString("Address");
                    String itemName = resultSet.getString("ItemName");
                    int quantity = resultSet.getInt("Quantity");
                    int cost = resultSet.getInt("Cost");
                    int totalCost = resultSet.getInt("TotalCost");

                    Order order = new Order(orderId, name, mobileNumber, address, itemName, quantity, cost, totalCost);
                    searchResults.add(order);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return searchResults;
    }

    private void displaySearchResults(ArrayList<Order> searchResults) {
        orderTableLayout.removeAllViews();

        boolean nameAndAddressDisplayed = false;
        boolean itemDetailsHeaderDisplayed = false;

        // Display address only if there are search results
        if (!searchResults.isEmpty()) {
            Order firstOrder = searchResults.get(0);

            TableRow nameRow = createRow("Name:", firstOrder.getName(), "", "");
            orderTableLayout.addView(nameRow);

            TableRow mobileNumberRow = createRow("Mobile Number:", firstOrder.getMobileNumber(), "", "");
            orderTableLayout.addView(mobileNumberRow);

            String address = firstOrder.getAddress();
            String[] addressLines = splitAddress(address, 25); // Adjust the line length as needed

            TableRow addressRow = new TableRow(this);

            TextView addressLabelTextView = new TextView(this);
            addressLabelTextView.setText("Address:");
            addressLabelTextView.setTextColor(Color.BLACK);
            addressLabelTextView.setPadding(8, 8, 8, 8);
            addressLabelTextView.setTextSize(18);
            addressLabelTextView.setTypeface(null, Typeface.BOLD);
            addressRow.addView(addressLabelTextView);

            TextView addressValueTextView = new TextView(this);
            addressValueTextView.setText(addressLines[0].trim());
            addressValueTextView.setTextColor(Color.BLACK);
            addressValueTextView.setPadding(8, 0, 8, 8);
            addressValueTextView.setTextSize(18);
            addressRow.addView(addressValueTextView);

            orderTableLayout.addView(addressRow);

            // Display the remaining address lines
            for (int i = 1; i < addressLines.length; i++) {
                TableRow addressRowRemaining = createRow("", addressLines[i].trim(), "", "");
                orderTableLayout.addView(addressRowRemaining);
            }

            nameAndAddressDisplayed = true;
        }

        // Display item details and total cost
        if (!itemDetailsHeaderDisplayed) {
            TableRow headerRow = createHeaderRow("Item Name", "Quantity", "");
            orderTableLayout.addView(headerRow);
            itemDetailsHeaderDisplayed = true;
        }

        for (Order order : searchResults) {
            if (!nameAndAddressDisplayed) {
                TableRow nameRow = createRow("Name:", order.getName(), "", "");
                orderTableLayout.addView(nameRow);

                TableRow mobileNumberRow = createRow("Mobile Number:", order.getMobileNumber(), "", "");
                orderTableLayout.addView(mobileNumberRow);
                nameAndAddressDisplayed = true;
            }

            TableRow itemRow = createRow(order.getItemName(), String.valueOf(order.getQuantity()), "", "");
            orderTableLayout.addView(itemRow);

            TableRow costRow = createRow("Cost per item (₹)", String.valueOf(order.getCost()), "", "");
            orderTableLayout.addView(costRow);
        }

        if (!searchResults.isEmpty()) {
            TableRow totalCostRow = createRow("Total Cost (₹)", String.valueOf(searchResults.get(0).getTotalCost()), "", "");
            orderTableLayout.addView(totalCostRow);
        }
    }

    private void insertConfirmedOrder(Order order) {
        Connection connection = connectionHelper.connectionclass();

        if (connection != null) {
            String query = "INSERT INTO ConfirmOrder (OrderID, Name, MobileNumber, Address, ItemName, Quantity, Cost, TotalCost) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, order.getOrderId());
                preparedStatement.setString(2, order.getName());
                preparedStatement.setString(3, order.getMobileNumber());
                preparedStatement.setString(4, order.getAddress());
                preparedStatement.setString(5, order.getItemName());
                preparedStatement.setInt(6, order.getQuantity());
                preparedStatement.setInt(7, order.getCost());
                preparedStatement.setInt(8, order.getTotalCost());

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    // Insert successful
                } else {
                    // Insert failed
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }



    private TableRow createRow(String label1, String value1, String label2, String value2) {
        TableRow row = new TableRow(this);

        TextView label1TextView = new TextView(this);
        label1TextView.setText(label1);
        label1TextView.setPadding(8, 8, 8, 8);
        label1TextView.setTextColor(Color.BLACK);
        label1TextView.setTextSize(18);
        label1TextView.setTypeface(null, Typeface.BOLD);
        row.addView(label1TextView);

        TextView value1TextView = new TextView(this);
        value1TextView.setText(value1);
        value1TextView.setTextColor(Color.BLACK);
        value1TextView.setPadding(8, 8, 8, 8);
        value1TextView.setTextSize(18);
        row.addView(value1TextView);

        TextView label2TextView = new TextView(this);
        label2TextView.setText(label2);
        label2TextView.setTextColor(Color.BLACK);
        label2TextView.setPadding(8, 8, 8, 8);
        label2TextView.setTextSize(18);
        label2TextView.setTypeface(null, Typeface.BOLD);
        row.addView(label2TextView);

        TextView value2TextView = new TextView(this);
        value2TextView.setText(value2); // Removed the "₹" symbol from here
        value1TextView.setTextColor(Color.BLACK);
        value2TextView.setPadding(8, 8, 32  , 8); // Adjusted padding to align with the "Cost per item" heading
        value2TextView.setTextSize(18);
        value2TextView.setGravity(Gravity.START | Gravity.CENTER_VERTICAL); // Aligning text to the start
        row.addView(value2TextView);

        // Adjust padding for the address heading
        if (label1.equals("Address")) {
            label1TextView.setPadding(8, 0, 8, 0); // Adjust top padding to align with address value
            value1TextView.setPadding(8, 0, 8, 8); // Adjust top padding to align with address value
        } else if (label2.equals("Cost per item (₹)")) { // Adjusted condition to match label2
            label2TextView.setPadding(8, 0, 8, 0); // Adjust top padding to align with "Cost per item" value
            value2TextView.setPadding(8, 8, 8, 8); // Adjust padding for the value
        }

        return row;
    }






    private TableRow createHeaderRow(String header1, String header2, String header3) {
        TableRow row = new TableRow(this);

        TextView header1TextView = new TextView(this);
        header1TextView.setText(header1);
        header1TextView.setPadding(8, 8, 8, 8);
        header1TextView.setTextColor(Color.BLACK);
        header1TextView.setTextSize(18);
        header1TextView.setTypeface(null, Typeface.BOLD);
        row.addView(header1TextView);

        TextView header2TextView = new TextView(this);
        header2TextView.setText(header2);
        header2TextView.setTextColor(Color.BLACK);
        header2TextView.setPadding(8, 8, 8, 8);
        header2TextView.setTextSize(18);
        header2TextView.setTypeface(null, Typeface.BOLD);
        row.addView(header2TextView);

        TextView header3TextView = new TextView(this);
        header3TextView.setText(header3);
        header3TextView.setTextColor(Color.BLACK);
        header3TextView.setPadding(8, 8, 32, 8); // Adjusted right padding for header3
        header3TextView.setTextSize(18);
        header3TextView.setTypeface(null, Typeface.BOLD);
        row.addView(header3TextView);

        return row;
    }



    private String[] splitAddress(String address, int maxLength) {
        ArrayList<String> lines = new ArrayList<>();
        if (address.length() <= maxLength) {
            lines.add(address);
        } else {
            // Split the address into multiple lines of maxLength characters each
            for (int i = 0; i < address.length(); i += maxLength) {
                lines.add(address.substring(i, Math.min(i + maxLength, address.length())));
            }
        }
        return lines.toArray(new String[0]);
    }
}
