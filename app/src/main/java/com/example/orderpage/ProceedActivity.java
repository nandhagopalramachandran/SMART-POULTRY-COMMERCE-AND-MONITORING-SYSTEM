package com.example.orderpage;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ProceedActivity extends AppCompatActivity {

    // Declare TextViews
    private TextView orderIdTextView, orderDateTextView,
            orderToNameTextView, orderToAddressTextView, orderToMobileTextView,
            totalCostTextView;

    private static final int STORAGE_PERMISSION_CODE = 101;
    private ConnectionHelper connectionHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proceed);
        connectionHelper = new ConnectionHelper();

        // Initialize TextViews
        orderIdTextView = findViewById(R.id.text_order_id);
        orderDateTextView = findViewById(R.id.text_order_date);
        orderToNameTextView = findViewById(R.id.text_order_to_name);
        orderToAddressTextView = findViewById(R.id.text_order_to_address);
        orderToMobileTextView = findViewById(R.id.text_order_to_mobile);
        totalCostTextView = findViewById(R.id.text_total_cost);

        // Retrieve data from Intent
        ConfirmedOrder selectedOrder = (ConfirmedOrder) getIntent().getSerializableExtra("selectedOrder");

        // Populate TextViews with data from selectedOrder
        orderIdTextView.setText("Order ID: " + selectedOrder.getOrderId());
        // Set current date
        orderDateTextView.setText("Order Date: " + getCurrentDate());
        // You can set other TextViews in a similar manner with the respective data from selectedOrder
        orderToNameTextView.setText("Name: " + selectedOrder.getName());
        orderToAddressTextView.setText("Address: " + selectedOrder.getAddress());
        orderToMobileTextView.setText("Mobile Number: " + selectedOrder.getMobileNumber());

        // For item details, dynamically add rows for each item
        List<Item> items = selectedOrder.getItems();
        for (Item item : items) {
            addRow(item.getItemName(), String.valueOf(item.getQuantity()), String.valueOf(item.getCost()));
        }

        totalCostTextView.setText("Total Cost: " + selectedOrder.getTotalCost());

        // Button to trigger PDF generation and download
        Button buttonPrint = findViewById(R.id.buttonPrint);
        buttonPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissionAndGeneratePDF(selectedOrder);
                insertOrderIntoDatabase(selectedOrder.getOrderId());
                Intent intent = new Intent(ProceedActivity.this, ord.class);
                startActivity(intent);
                finish(); //
            }
        });
    }

    // Method to dynamically add rows for item details
    private void addRow(String item, String quantity, String cost) {
        LinearLayout layout = findViewById(R.id.item_details_layout);

        LinearLayout row = new LinearLayout(this);
        row.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        row.setOrientation(LinearLayout.HORIZONTAL);

        TextView itemTextView = new TextView(this);
        itemTextView.setLayoutParams(new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1));
        itemTextView.setText(item);
        row.addView(itemTextView);

        TextView quantityTextView = new TextView(this);
        quantityTextView.setLayoutParams(new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1));
        quantityTextView.setText(quantity);
        row.addView(quantityTextView);

        TextView costTextView = new TextView(this);
        costTextView.setLayoutParams(new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1));
        costTextView.setText(cost);
        row.addView(costTextView);

        layout.addView(row);
    }

    // Check storage permission and generate PDF
    private void checkPermissionAndGeneratePDF(ConfirmedOrder selectedOrder) {
        if (ContextCompat.checkSelfPermission(ProceedActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            generatePDF(selectedOrder);
        } else {
            ActivityCompat.requestPermissions(ProceedActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    // Generate PDF invoice
    private void generatePDF(ConfirmedOrder selectedOrder) {
        PdfDocument document = new PdfDocument();
        LinearLayout view = findViewById(R.id.root_layout); // Replace "root_layout" with the id of your root layout
        Context context = view.getContext();

        // Hide the print button before drawing onto PDF
        findViewById(R.id.buttonPrint).setVisibility(View.GONE);

        int width = view.getWidth();
        int height = view.getHeight();

        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(width, height, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        view.draw(page.getCanvas());
        document.finishPage(page);

        // Restore the print button after drawing onto PDF
        findViewById(R.id.buttonPrint).setVisibility(View.VISIBLE);

        String orderId = selectedOrder.getOrderId() + ""; // Extract order ID from selectedOrder
        File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        String fileName = "OrderID_" + orderId + ".pdf"; // Filename based on order ID
        File file = new File(downloadsDir, fileName);

        try {
            FileOutputStream fos = new FileOutputStream(file);
            document.writeTo(fos);
            document.close();
            Toast.makeText(context, "PDF saved successfully.", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Log.e("PDF", "Error generating PDF", e);
            Toast.makeText(context, "Failed to generate PDF.", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to get current date
    private String getCurrentDate() {
        java.time.LocalDate currentDate = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            currentDate = java.time.LocalDate.now();
        }
        return currentDate.toString();
    }

    // Handle permission request result
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Retrieve data from Intent again
                ConfirmedOrder selectedOrder = (ConfirmedOrder) getIntent().getSerializableExtra("selectedOrder");
                generatePDF(selectedOrder);
            } else {
                Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void insertOrderIntoDatabase(int orderId) {
        Connection connection = connectionHelper.connectionclass();
        if (connection != null) {
            try {
                String selectQuery = "SELECT [Name], [MobileNumber], [Address], [ItemName], [Quantity], [Cost], [TotalCost], [OrderDate] " +
                        "FROM [PlaceOrder].[dbo].[ConfirmOrder] " +
                        "WHERE [OrderID] = ?";
                PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
                selectStatement.setInt(1, orderId);
                ResultSet resultSet = selectStatement.executeQuery();

                while (resultSet.next()) {
                    String name = resultSet.getString("Name");
                    String mobileNumber = resultSet.getString("MobileNumber");
                    String address = resultSet.getString("Address");
                    String itemName = resultSet.getString("ItemName");
                    int quantity = resultSet.getInt("Quantity");
                    double cost = resultSet.getDouble("Cost");
                    double totalCost = resultSet.getDouble("TotalCost");
                    String orderDate = resultSet.getString("OrderDate");

                    // Now, insert these values into the OrderHistory table
                    insertIntoOrderHistory(orderId, name, mobileNumber, address, itemName, quantity, cost, totalCost, orderDate);
                }

                // Close result set and statement
                resultSet.close();
                selectStatement.close();

                // Delete the order from ConfirmOrder table
                String deleteConfirmOrderQuery = "DELETE FROM [PlaceOrder].[dbo].[ConfirmOrder] WHERE [OrderID] = ?";
                PreparedStatement deleteConfirmOrderStatement = connection.prepareStatement(deleteConfirmOrderQuery);
                deleteConfirmOrderStatement.setInt(1, orderId);
                int confirmOrderRowsDeleted = deleteConfirmOrderStatement.executeUpdate();

                if (confirmOrderRowsDeleted > 0) {
                    Toast.makeText(this, "Order deleted from ConfirmOrder table.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Failed to delete order from ConfirmOrder table.", Toast.LENGTH_SHORT).show();
                }

                // Close delete statement for ConfirmOrder table
                deleteConfirmOrderStatement.close();

                // Delete order details from OrderDetails table
                String deleteOrderDetailsQuery = "DELETE FROM [PlaceOrder].[dbo].[OrderDetails] WHERE [OrderID] = ?";
                PreparedStatement deleteOrderDetailsStatement = connection.prepareStatement(deleteOrderDetailsQuery);
                deleteOrderDetailsStatement.setInt(1, orderId);
                int orderDetailsRowsDeleted = deleteOrderDetailsStatement.executeUpdate();

                if (orderDetailsRowsDeleted > 0) {
                    Toast.makeText(this, "Order details deleted from OrderDetails table.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Failed to delete order details from OrderDetails table.", Toast.LENGTH_SHORT).show();
                }

                // Close delete statement for OrderDetails table
                deleteOrderDetailsStatement.close();

                // Delete customer details from CustomerDetails table
                String deleteCustomerDetailsQuery = "DELETE FROM [PlaceOrder].[dbo].[CustomerDetails] WHERE [OrderID] = ?";
                PreparedStatement deleteCustomerDetailsStatement = connection.prepareStatement(deleteCustomerDetailsQuery);
                deleteCustomerDetailsStatement.setInt(1, orderId);
                int customerDetailsRowsDeleted = deleteCustomerDetailsStatement.executeUpdate();

                if (customerDetailsRowsDeleted > 0) {
                    Toast.makeText(this, "Customer details deleted from CustomerDetails table.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Failed to delete customer details from CustomerDetails table.", Toast.LENGTH_SHORT).show();
                }

                // Close delete statement for CustomerDetails table
                deleteCustomerDetailsStatement.close();

            } catch (SQLException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            } finally {
                connectionHelper.closeConnection(connection);
            }
        } else {
            Toast.makeText(this, "Failed to establish database connection.", Toast.LENGTH_SHORT).show();
        }
    }



    private void insertIntoOrderHistory(int orderId, String name, String mobileNumber, String address,
                                        String itemName, int quantity, double cost, double totalCost, String orderDate) {
        Connection connection = connectionHelper.connectionclass();
        if (connection != null) {
            try {
                String insertQuery = "INSERT INTO OrderHistory (OrderID, Name, MobileNumber, ItemName, Quantity, Cost, TotalCost, Address) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
                insertStatement.setInt(1, orderId);
                insertStatement.setString(2, name);
                insertStatement.setString(3, mobileNumber);
                insertStatement.setString(4, itemName);
                insertStatement.setInt(5, quantity);
                insertStatement.setDouble(6, cost);
                insertStatement.setDouble(7, totalCost);
                insertStatement.setString(8, address);


                int rowsAffected = insertStatement.executeUpdate();
                if (rowsAffected > 0) {
                    Toast.makeText(this, "Order inserted successfully into OrderHistory table.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Failed to insert order into OrderHistory table.", Toast.LENGTH_SHORT).show();
                }

                insertStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            } finally {
                connectionHelper.closeConnection(connection);
            }
        } else {
            Toast.makeText(this, "Failed to establish database connection.", Toast.LENGTH_SHORT).show();
        }
    }
}
