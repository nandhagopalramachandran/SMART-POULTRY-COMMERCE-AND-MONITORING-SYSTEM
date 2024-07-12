package com.example.orderpage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class PlaceOrder extends AppCompatActivity {
    private GridView henGridView;
    private HenAdapter henAdapter;
    private Calculation shoppingCart;
    private Button placeOrderButton;

    // Variables to hold user details
    private String name;
    private String mobileNumber;
    private String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placeorder);

        // Receive user details from intent
        Intent intent = getIntent();
        if (intent != null) {
            name = intent.getStringExtra("Name");
            mobileNumber = intent.getStringExtra("MobileNumber");
            address = intent.getStringExtra("Address");
        }

        shoppingCart = Calculation.getInstance();

        henGridView = findViewById(R.id.henGridView);
        henAdapter = new HenAdapter(this, getSampleHenData(), shoppingCart);
        henGridView.setAdapter(henAdapter);

        placeOrderButton = findViewById(R.id.placeOrderButton);
        placeOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectedItems();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        shoppingCart.clearSelectedItems(); // Clear the selected items when the activity is resumed
    }

    private ArrayList<Hen> getSampleHenData() {
        ArrayList<Hen> hens = new ArrayList<>();
        hens.add(new Hen("Brown hen", "assel", "1 year", " ₹200", R.drawable.hen1));
        hens.add(new Hen("Hamsphire", "Chittagong", "2 years", " ₹300", R.drawable.hen2));
        hens.add(new Hen("Broiler", "Bursa", "1.5 years", " ₹230", R.drawable.hen3));
        hens.add(new Hen("Ancona", "Kadaknak", "2.5 years", " ₹200", R.drawable.hen4));
        hens.add(new Hen("Country Chicken Egg", "One Tray", "1", " ₹157", R.drawable.hen5));
        hens.add(new Hen("Turkey", "Norfolk Black", "2 years", " ₹2600", R.drawable.hen6));
        return hens;
    }

    private void showSelectedItems() {
        // Start the OrderActivity to display the selected items
        Intent intent = new Intent(PlaceOrder.this, OrderActivity.class);
        // Pass user details to the OrderActivity
        intent.putExtra("Name", name);
        intent.putExtra("MobileNumber", mobileNumber);
        intent.putExtra("Address", address);
        startActivity(intent);
    }
}
