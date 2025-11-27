package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class dataBaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/nossocanto_db?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "Diogo123@";

    private static Connection connection;

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Connected to database");
            }
            return connection;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver not found", e);
        } catch (SQLException e) {
            throw new RuntimeException("SQL Error" + e.getMessage(), e);
        }
    }

    public static void closeConnection() {
        try {
            if (connection != null || !connection.isClosed()) {
                connection.close();
                System.out.println("Connection closed");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error to close connection" , e);
        }
    }

    public static void connectionTest() {
        try {
            getConnection();
            System.out.println("Connection established");
        } catch (Exception e) {
            System.out.println("Connection failed"+ e.getMessage());
        }
    }

}

