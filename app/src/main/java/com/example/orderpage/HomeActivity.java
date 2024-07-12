package com.example.orderpage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Get the login layout
        View loginLayout = findViewById(R.id.loginLayout);

        // Set OnClickListener for the login layout
        loginLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the login button click
                goToLoginActivity();
            }
        });

        View loginLayout1 = findViewById(R.id.loginLayout1);

        // Set OnClickListener for the login layout
        loginLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the login button click
                goToadminLoginActivity();
            }
        });
    }

    // Method to navigate to the login activity
    private void goToLoginActivity() {
        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    private void goToadminLoginActivity() {
        Intent intent = new Intent(HomeActivity.this, AdminLogin.class);
        startActivity(intent);
    }


    // Method to handle the login button click
    public void onLoginClick(View view) {
        goToLoginActivity();
    }
}
