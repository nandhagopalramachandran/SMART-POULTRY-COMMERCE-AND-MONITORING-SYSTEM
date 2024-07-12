package com.example.orderpage;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ConnectionHelper {
    // Connection parameters
    private static final String IP = "192.168.222.8";
    private static final String PORT = "53142";
    private static final String DATABASE = "PlaceOrder";
    private static final String USER = "sa";
    private static final String PASSWORD = "bmw@8068";

    @SuppressLint("NewApi")
    public Connection connectionclass() {
        // Enable strict mode to perform network operations on the main thread
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Connection connection = null;
        String connectionURL;

        try {
            // Load the JDBC driver
            Class.forName("net.sourceforge.jtds.jdbc.Driver");

            // Create the connection URL
            connectionURL = "jdbc:jtds:sqlserver://" + IP + ":" + PORT + ";databaseName=" + DATABASE + ";user=" + USER + ";password=" + PASSWORD + ";";

            // Establish the connection
            connection = DriverManager.getConnection(connectionURL);

        } catch (Exception e) {
            Log.e("Connection Error", e.getMessage());
        }

        return connection;
    }

    // Add a method for closing the connection
    public static void closeConnection(Connection connection) {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (Exception e) {
            Log.e("Error closing connection", e.getMessage());
        }
    }
}
