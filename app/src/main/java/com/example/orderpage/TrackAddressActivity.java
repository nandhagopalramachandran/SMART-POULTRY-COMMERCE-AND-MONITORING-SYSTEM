package com.example.orderpage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;

public class TrackAddressActivity extends AppCompatActivity {

    private ConnectionHelper connectionHelper;
    private ListView listView;
    private OrderAdapter adapter;
    private ArrayList<Order1> orderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_address);

        listView = findViewById(R.id.listView);
        orderList = new ArrayList<>();
        adapter = new OrderAdapter(this, orderList);
        listView.setAdapter(adapter);

        connectionHelper = new ConnectionHelper();
        displayOrderDetails();

        // Set item click listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Handle item click event
                Order1 selectedOrder = orderList.get(position);
                // Start AddressActivity and pass selected order details
                Intent intent = new Intent(TrackAddressActivity.this, AddressActivity.class);
                intent.putExtra("orderId", selectedOrder.getOrderId());
                startActivity(intent);
            }
        });

    }

    private void displayOrderDetails() {
        // Retrieve order details from the database
        ArrayList<Order1> orders = getOrderDetailsFromDatabase();

        // Check if any orders were retrieved
        if (orders.isEmpty()) {
            Toast.makeText(this, "No orders found in database", Toast.LENGTH_SHORT).show();
        } else {
            // Clear existing orderList
            orderList.clear();
            // Add retrieved orders to the list
            orderList.addAll(orders);
            // Notify the adapter of the changes
            adapter.notifyDataSetChanged();
        }
    }

    private ArrayList<Order1> getOrderDetailsFromDatabase() {
        ArrayList<Order1> orders = new ArrayList<>();
        HashSet<Integer> uniqueOrderIds = new HashSet<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = connectionHelper.connectionclass();
            if (connection != null) {
                String query = "SELECT OrderID, Name FROM ConfirmOrder";
                preparedStatement = connection.prepareStatement(query);
                resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    int orderId = resultSet.getInt("OrderID");
                    if (!uniqueOrderIds.contains(orderId)) { // Check if orderId is unique
                        uniqueOrderIds.add(orderId);
                        String name = resultSet.getString("Name");
                        Order1 order = new Order1(orderId, name); // Adjust constructor call
                        orders.add(order);
                    }
                }
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

        return orders;
    }

}