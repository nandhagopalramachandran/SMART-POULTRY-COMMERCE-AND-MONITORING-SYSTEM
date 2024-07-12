package com.example.orderpage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class Dashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);

        // Find the Inventory CardView by its ID
        CardView inventoryCardView = findViewById(R.id.Iventory);

        // Set OnClickListener on the Inventory CardView
        inventoryCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Apply pop effect
                applyPopEffect(inventoryCardView);

                // Open InventoryActivity
                Intent intent = new Intent(Dashboard.this, Inventory.class);
                startActivity(intent);
            }
        });

        // Set OnClickListener on the Order CardView
        CardView orderCardView = findViewById(R.id.Order);
        orderCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Apply pop effect
                applyPopEffect(orderCardView);

                // Open OrdersActivity
                Intent intent = new Intent(Dashboard.this, ord.class);
                startActivity(intent);
            }
        });

        CardView moni = findViewById(R.id.Monitoring);
        moni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Apply pop effect
                applyPopEffect(orderCardView);

                // Open OrdersActivity
                Intent intent = new Intent(Dashboard.this,Monitoring.class);
                startActivity(intent);
            }
        });



        CardView orderhistory = findViewById(R.id.OrdHist);
        orderhistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Apply pop effect
                applyPopEffect(orderCardView);

                // Open OrdersActivity
                Intent intent = new Intent(Dashboard.this, OrderHistoryActivity.class);
                startActivity(intent);
            }
        });
    }





    private void applyPopEffect(View view) {
        ScaleAnimation scaleAnim = new ScaleAnimation(1f, 1.1f, 1f, 1.1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnim.setDuration(300);
        scaleAnim.setFillAfter(false);
        view.startAnimation(scaleAnim);
    }
}
