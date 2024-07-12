package com.example.orderpage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class ord extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ord);

        CardView orderCardView = findViewById(R.id.Order1);
        orderCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open OrdersActivity
                applyPopEffect(orderCardView);
                Intent intent = new Intent(ord.this, EntireOrder.class);
                startActivity(intent);
            }
        });
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        CardView orderCardView1 = findViewById(R.id.specific);
        orderCardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open SearchOrderActivity
                applyPopEffect(orderCardView1);
                Intent intent = new Intent(ord.this, SearchOrderActivity.class);
                startActivity(intent);
            }
        });

        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        CardView orderCardView2 = findViewById(R.id.confirmed);
        orderCardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open SearchOrderActivity
                applyPopEffect(orderCardView2);
                Intent intent = new Intent(ord.this, ConfirmedOrderActivity.class);
                startActivity(intent);
            }
        });

        CardView orderCardView3 = findViewById(R.id.trackAddress);
        orderCardView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open SearchOrderActivity
                applyPopEffect(orderCardView2);
                Intent intent = new Intent(ord.this, TrackAddressActivity.class);
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
