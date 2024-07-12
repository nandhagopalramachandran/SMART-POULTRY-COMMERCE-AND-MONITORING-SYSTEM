package com.example.orderpage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AdminLogin extends AppCompatActivity {

    EditText adminname;
    EditText adminpassword;
    Button loginButton;
    ConnectionHelper connectionHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adminlogin);

        connectionHelper = new ConnectionHelper();

        // Initialize views
        adminname = findViewById(R.id.adminname);
        adminpassword = findViewById(R.id.adminpassword);
        loginButton = findViewById(R.id.adminloginButton);

        // Set OnClickListener for login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get username and password from EditText fields
                String enteredAdminname = adminname.getText().toString();
                String enteredAdminPassword = adminpassword.getText().toString();

                // Authenticate admin against database
                boolean isAuthenticated = authenticateAdmin(enteredAdminname, enteredAdminPassword);

                // Check if authentication is successful
                if (isAuthenticated) {
                    // If correct, navigate to the admin home page
                    Toast.makeText(AdminLogin.this, "ADMIN LOGIN SUCCESSFUL", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AdminLogin.this, Dashboard.class);
                    startActivity(intent);
                    finish();
                } else {
                    // If incorrect, display a toast message
                    Toast.makeText(AdminLogin.this, "Incorrect username or password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean authenticateAdmin(String username, String password) {
        Connection connection = connectionHelper.connectionclass();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            // SQL query to check if the provided username and password match
            String query = "SELECT * FROM adminauth WHERE username=? AND password=?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            resultSet = preparedStatement.executeQuery();

            // If there is at least one row returned, the username and password are correct
            return resultSet.next();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close resources
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Return false if authentication fails
        return false;
    }
}
