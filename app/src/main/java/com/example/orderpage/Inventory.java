package com.example.orderpage;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class Inventory extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inventory);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        CardView inventoryDetail = findViewById(R.id.detail);

        // Set OnClickListener on the Inventory CardView
        inventoryDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Apply pop effect
                applyPopEffect(inventoryDetail);

                // Open InventoryActivity
                Intent intent = new Intent(Inventory.this, invent.class);
                startActivity(intent);
            }
        });

        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        CardView inventorycustom = findViewById(R.id.custom);

        // Set OnClickListener on the Inventory CardView
        inventorycustom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Apply pop effect
                applyPopEffect(inventorycustom);

                // Open InventoryActivity
                Intent intent = new Intent(Inventory.this, CustomInventoryActivity.class);
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
