package com.example.orderpage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class UserHomeActivity extends AppCompatActivity {

    private String name;
    private String mobileNumber;
    private String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userhome);

        // Receive user details from intent
        Intent intent = getIntent();
        if (intent != null) {
            name = intent.getStringExtra("Name");
            mobileNumber = intent.getStringExtra("MobileNumber");
            address = intent.getStringExtra("Address");
        }

        ImageView imageViewHome = findViewById(R.id.imageViewHome);
        imageViewHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the PlaceOrder activity
                Intent orderIntent = new Intent(UserHomeActivity.this, PlaceOrder.class);
                // Pass user details to the PlaceOrder activity
                orderIntent.putExtra("Name", name);
                orderIntent.putExtra("MobileNumber", mobileNumber);
                orderIntent.putExtra("Address", address);
                startActivity(orderIntent);
                finish();
            }
        });
    }
}
