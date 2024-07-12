package com.example.orderpage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class OrderConfirmationActivity extends AppCompatActivity {
    private Calculation shoppingCart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);
        shoppingCart = Calculation.getInstance();
        displayOrderDetails();

        // Retrieve order details from the intent

        // Display the order confirmation message

        // Display the order details in a TextView
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        Button gohome = findViewById(R.id.gohome);
        gohome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the main activity
                shoppingCart.clearSelectedItems();
                Intent intent = new Intent(OrderConfirmationActivity.this, HomeActivity.class);
                startActivity(intent);
                finish(); //
            }
        });
    }

    // Helper method to format order details as a string
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

}
