package sample;

import java.sql.*;

public class ConnectDriver {

    public Connection loadDriver () {

        try {
            //Register JDBC driver
            Class.forName("oracle.jdbc.driver.OracleDriver");
            //Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException cnfe) {
            System.out.println("Error loading driver: " + cnfe);
        }

        // jdbc:oracle:thin:@localhost:1521:william [scott on SCOTT]
        String host = "localhost";
        String dbName = "william";
        int port =  1521;
        String oracleURL = "jdbc:oracle:thin:@" + host + ":" + port + ":" + dbName;
        String username = "scott";
        String password = "tiger";
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(oracleURL, username, password);

            DatabaseMetaData dbMetaData = connection.getMetaData();
            String productName = dbMetaData.getDatabaseProductName();
            System.out.println("Database: " + productName);
            String productVersion = dbMetaData.getDatabaseProductVersion();
            System.out.println("Version: " + productVersion);

        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        return connection;
    }
}

