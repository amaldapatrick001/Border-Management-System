import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateDatabase {
    private static final String URL = "jdbc:mysql://localhost:3309/";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // Replace with your MySQL root password

    public static void main(String[] args) {
        Connection connection = null;
        Statement statement = null;

        try {
            System.out.println("Loading JDBC driver...");
            Class.forName("com.mysql.cj.jdbc.Driver"); // Ensure the driver class is loaded
            System.out.println("Connecting to the database server...");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected to the database server successfully.");

            statement = connection.createStatement();
            String sql = "CREATE DATABASE BMS";
            statement.executeUpdate(sql);
            System.out.println("Database created successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Database creation failed!");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("JDBC Driver not found!");
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                    System.out.println("Connection closed.");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
