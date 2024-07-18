import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class BLogin extends Frame implements ActionListener {
    private TextField usernameField;
    private TextField passwordField;
    private Button loginButton;

    // Database credentials
    static final String DB_URL = "jdbc:mysql://localhost:3309/bms";
    static final String USER = "root";
    static final String PASS = ""; // Replace with your MySQL password

    public BLogin() {
        setTitle("Border Officer Login");
        setSize(300, 150);
        setLayout(new GridLayout(3, 2));

        add(new Label("Username:"));
        usernameField = new TextField();
        add(usernameField);

        add(new Label("Password:"));
        passwordField = new TextField();
        passwordField.setEchoChar('*'); // Hide input
        add(passwordField);

        loginButton = new Button("Login");
        loginButton.addActionListener(this);
        add(loginButton);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                dispose();
            }
        });

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showMessage("Username and password are required");
        } else {
            authenticate(username, password);
        }
    }

    private void authenticate(String username, String password) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM border_officers WHERE username = ?")) {
            stmt.setString(1, username);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String storedPassword = rs.getString("password");
                if (storedPassword.equals(password)) {
                    showMessage("Login successful");
                    String station = rs.getString("station"); // Get station information
                    openBorderPortal(station);
                } else {
                    showMessage("Invalid password");
                }
            } else {
                showMessage("Invalid username");
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // Consider logging the error instead
            showMessage("Database error: " + ex.getMessage());
        }
    }

    private void openBorderPortal(String station) {
        new BorderPortal(station).setVisible(true); // Pass station information to BorderPortal
        dispose(); // Close the login window
    }

    private void showMessage(String message) {
        Dialog dialog = new Dialog(this, "Message", true);
        dialog.setLayout(new FlowLayout());
        dialog.add(new Label(message));
        Button okButton = new Button("OK");
        okButton.addActionListener(ae -> dialog.setVisible(false));
        dialog.add(okButton);
        dialog.setSize(300, 100);
        dialog.setVisible(true);
    }

    public static void main(String[] args) {
        new BLogin();
    }
}
