    package com.example.orderpage;

    import android.content.Intent;
    import android.os.Bundle;
    import android.widget.Button;

    import androidx.appcompat.app.AppCompatActivity;
    import androidx.recyclerview.widget.LinearLayoutManager;
    import androidx.recyclerview.widget.RecyclerView;

    import java.sql.Connection;
    import java.sql.PreparedStatement;
    import java.sql.ResultSet;
    import java.sql.SQLException;
    import java.util.ArrayList;

    public class ConfirmedOrderActivity extends AppCompatActivity implements ConfirmedOrderAdapter.OnItemClickListener {

        private RecyclerView recyclerView;
        private ConfirmedOrderAdapter adapter;
        private ConnectionHelper connectionHelper;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_confirmed_order);

            recyclerView = findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            connectionHelper = new ConnectionHelper();
            ArrayList<ConfirmedOrder> orderDetailsList = getConfirmedOrderDetailsFromDatabase();
            adapter = new ConfirmedOrderAdapter(orderDetailsList, this);
            recyclerView.setAdapter(adapter);

            Button proceedButton = findViewById(R.id.proceedButton);
            proceedButton.setOnClickListener(v -> {
                // Get the selected item position from the adapter
                int selectedPosition = adapter.getSelectedPosition();
                if (selectedPosition != RecyclerView.NO_POSITION && orderDetailsList != null) {
                    // Get the selected item details
                    ConfirmedOrder selectedOrder = orderDetailsList.get(selectedPosition);

                    // Start ProceedActivity and pass selected item details
                    Intent intent = new Intent(ConfirmedOrderActivity.this, ProceedActivity.class);
                    intent.putExtra("selectedOrder", selectedOrder);
                    startActivity(intent);
                }
            });
        }

        private ArrayList<ConfirmedOrder> getConfirmedOrderDetailsFromDatabase() {
            ArrayList<ConfirmedOrder> orderDetailsList = new ArrayList<>();

            Connection connection = null;
            PreparedStatement preparedStatement = null;
            ResultSet resultSet = null;

            try {
                connection = connectionHelper.connectionclass();
                if (connection != null) {
                    String query = "SELECT OrderID, Name, MobileNumber, Address, ItemName, Quantity, Cost, TotalCost FROM ConfirmOrder";
                    preparedStatement = connection.prepareStatement(query);
                    resultSet = preparedStatement.executeQuery();

                    while (resultSet.next()) {
                        int orderId = resultSet.getInt("OrderID");
                        String name = resultSet.getString("Name");
                        String mobileNumber = resultSet.getString("MobileNumber");
                        String address = resultSet.getString("Address");
                        double totalCost = resultSet.getDouble("TotalCost");

                        // Retrieve items for the current order from the database
                        ArrayList<Item> items = new ArrayList<>();
                        do {
                            String itemName = resultSet.getString("ItemName");
                            int quantity = resultSet.getInt("Quantity");
                            double cost = resultSet.getDouble("Cost");

                            // Create an Item object and add it to the list of items for this order
                            items.add(new Item(itemName, quantity, cost));
                        } while (resultSet.next() && orderId == resultSet.getInt("OrderID"));

                        // Create a ConfirmedOrder object and add it to the list
                        ConfirmedOrder confirmedOrder = new ConfirmedOrder(orderId, name, mobileNumber, address, items, totalCost);
                        orderDetailsList.add(confirmedOrder);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                // Close resources
                try {
                    if (resultSet != null) {
                        resultSet.close();
                    }
                    if (preparedStatement != null) {
                        preparedStatement.close();
                    }
                    if (connection != null) {
                        connection.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            return orderDetailsList;
        }

        @Override
        public void onItemClick(int orderId) {
            if (adapter != null) {
                // Get the position of the clicked order ID
                int position = adapter.getPositionByOrderId(orderId);
                if (position != RecyclerView.NO_POSITION) {
                    // Update the selected position in the adapter
                    adapter.setSelectedPosition(position);
                    // Notify adapter of the item change to update its appearance
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }