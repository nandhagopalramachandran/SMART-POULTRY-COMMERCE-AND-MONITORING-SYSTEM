package com.example.orderpage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class Register extends AppCompatActivity {

    private EditText nameEditText, usernameEditText, emailEditText, addressEditText, passwordEditText, mobileEditText;
    private CheckBox showPasswordCheckBox;
    private ConnectionHelper connectionHelper;
    private Button registerButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize views
        nameEditText = findViewById(R.id.nameEditText);
        usernameEditText = findViewById(R.id.usernameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        addressEditText = findViewById(R.id.addressEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        mobileEditText = findViewById(R.id.mobileEditText);
        showPasswordCheckBox = findViewById(R.id.showPasswordCheckBox);
        registerButton = findViewById(R.id.registerButton);

        connectionHelper = new ConnectionHelper();

        // Set listener for the checkbox to toggle password visibility
        showPasswordCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> togglePasswordVisibility(isChecked));

        // Set listener for the register button
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the values from EditText fields
                String name = nameEditText.getText().toString().trim();
                String username = usernameEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();
                String address = addressEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                String mobileNumber = mobileEditText.getText().toString().trim();
                Intent intent = new Intent(Register.this, HomeActivity.class);
                startActivity(intent);

                // Execute AsyncTask to perform database operation in the background
                new RegisterTask().execute(name, username, email, address, password, mobileNumber);
            }
        });
    }

    private void togglePasswordVisibility(boolean isChecked) {
        if (isChecked) {
            // If checkbox is checked, show the password
            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {
            // If checkbox is unchecked, hide the password
            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }

        // Move cursor to the end of the password field
        passwordEditText.setSelection(passwordEditText.length());
    }

    private class RegisterTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            String name = params[0];
            String username = params[1];
            String email = params[2];
            String address = params[3];
            String password = params[4];
            String mobileNumber = params[5];

            Connection connection = connectionHelper.connectionclass();

            if (connection != null) {
                try {
                    String query = "INSERT INTO UserDetails (Name, Username, Password, Email, MobileNumber, Address) VALUES (?, ?, ?, ?, ?, ?)";
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, name);  // Index starts from 1
                    preparedStatement.setString(2, username);
                    preparedStatement.setString(3, password);
                    preparedStatement.setString(4, email);
                    preparedStatement.setString(5, mobileNumber);
                    preparedStatement.setString(6, address);


                    int rowsAffected = preparedStatement.executeUpdate();
                    return rowsAffected > 0;

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    connectionHelper.closeConnection(connection);
                }
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                // Display success message
                Toast.makeText(Register.this, "User registered successfully", Toast.LENGTH_SHORT).show();

                // Clear EditText fields after successful registration
                nameEditText.setText("");
                usernameEditText.setText("");
                passwordEditText.setText("");
                emailEditText.setText("");
                addressEditText.setText("");
                mobileEditText.setText("");
            } else {
                Toast.makeText(Register.this, "Failed to register user", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
