package com.example.orderpage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginActivity extends AppCompatActivity {

    EditText username;
    EditText password;
    Button loginButton;
    TextView signupText;
    ConnectionHelper connectionHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        connectionHelper = new ConnectionHelper();

        // Initialize views
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        signupText = findViewById(R.id.signupText);

        // Set OnClickListener for login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get username and password from EditText fields
                String enteredUsername = username.getText().toString();
                String enteredPassword = password.getText().toString();

                // Authenticate user against database
                User user = authenticateUser(enteredUsername, enteredPassword);

                // Check if authentication is successful
                if (user != null) {
                    // If correct, navigate to the homepage
                    Toast.makeText(LoginActivity.this, "LOGIN SUCCESSFUL", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, UserHomeActivity.class);
                    intent.putExtra("Name", user.getName());
                    intent.putExtra("MobileNumber", user.getMobileNumber());
                    intent.putExtra("Address", user.getAddress());
                    startActivity(intent);
                    finish();
                } else {
                    // If incorrect, display a toast message
                    Toast.makeText(LoginActivity.this, "Incorrect username or password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Set OnClickListener for signup text
        signupText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the register activity when signup text is clicked
                Intent intent = new Intent(LoginActivity.this, Register.class);
                startActivity(intent);
            }
        });
    }

    private User authenticateUser(String username, String password) {
        Connection connection = connectionHelper.connectionclass();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            // SQL query to check if the provided username and password match
            String query = "SELECT * FROM UserDetails WHERE username=? AND password=?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            resultSet = preparedStatement.executeQuery();

            // If there is at least one row returned, the username and password are correct
            if (resultSet.next()){
                // Retrieve user information from the result set
                String name = resultSet.getString("Name");
                String mobileNumber = resultSet.getString("MobileNumber");
                String address = resultSet.getString("Address");

                // Close resources
                resultSet.close();
                preparedStatement.close();
                connection.close();

                // Return a new User object with retrieved information
                return new User(name, mobileNumber, address);
            }
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

        // Return null if authentication fails
        return null;
    }
}
