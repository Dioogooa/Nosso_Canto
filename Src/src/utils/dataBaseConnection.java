package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/nossocanto_db?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "Diogo123@";

    private static Connection connection;

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");

                } catch (ClassNotFoundException e) {
                    System.out.println("Driver JDBC não encontrado :/");
                }

                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Deu bom !");
            }
            return connection;
        } catch (SQLException e) {
            System.out.println(" ERRO MySQL: " + e.getMessage());
            System.out.println(" Soluções:");
            System.out.println("- MySQL está rodando?");
            System.out.println("- Banco 'nossocanto_db' existe?");
            System.out.println("- Senha do MySQL está correta?");
            throw new RuntimeException("Falha na conexão com o banco", e);
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