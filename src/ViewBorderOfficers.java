import java.awt.*;
import java.sql.*;

public class ViewBorderOfficers extends Frame {
    private Panel tablePanel;
    private Button closeButton;
    private Button backButton; // Button to go back to admin portal

    // Database credentials
    static final String DB_URL = "jdbc:mysql://localhost:3309/bms";
    static final String USER = "root";
    static final String PASS = ""; // Replace with your MySQL password

    public ViewBorderOfficers() {
        setTitle("View Border Officers");
        setSize(800, 400);
        setLayout(new BorderLayout());

        tablePanel = new Panel();
        tablePanel.setLayout(new GridBagLayout());
        add(tablePanel, BorderLayout.CENTER);

        closeButton = new Button("Close");
        add(closeButton, BorderLayout.SOUTH);

        closeButton.addActionListener(e -> dispose());

        backButton = new Button("Back to Admin Portal"); // Create the back button
        backButton.addActionListener(e -> {
            openAdminPortal(); // Navigate back to the admin portal
            dispose(); // Close the ViewBorderOfficers window
        });
        add(backButton, BorderLayout.NORTH); // Add the button to the top of the frame

        fetchBorderOfficers();
        setVisible(true);
    }

    private void fetchBorderOfficers() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Add column labels
        gbc.gridy = 0;
        String[] columnLabels = {"ID", "Name", "Username", "Email", "Phone", "Station"};
        for (int i = 0; i < columnLabels.length; i++) {
            gbc.gridx = i;
            Label label = new Label(columnLabels[i]);
            label.setFont(new Font("Arial", Font.BOLD, 14));
            tablePanel.add(label, gbc);
        }

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM border_officers")) {

            int rowNum = 1;
            while (rs.next()) {
                gbc.gridy = rowNum;

                // Add officer information
                gbc.gridx = 0;
                tablePanel.add(new Label(String.valueOf(rs.getInt("officer_id"))), gbc);

                gbc.gridx = 1;
                String name = rs.getString("first_name") + " " + rs.getString("last_name");
                tablePanel.add(new Label(name), gbc);

                gbc.gridx = 2;
                tablePanel.add(new Label(rs.getString("username")), gbc);

                gbc.gridx = 3;
                tablePanel.add(new Label(rs.getString("email")), gbc);

                gbc.gridx = 4;
                tablePanel.add(new Label(rs.getString("phone_number")), gbc);

                gbc.gridx = 5;
                tablePanel.add(new Label(rs.getString("station")), gbc);

                rowNum++;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            showMessage("Database error: " + ex.getMessage());
        }
    }

    private void showMessage(String message) {
        Dialog dialog = new Dialog(this, "Error", true);
        dialog.setLayout(new FlowLayout());
        dialog.add(new Label(message));
        Button okButton = new Button("OK");
        okButton.addActionListener(ae -> dialog.setVisible(false));
        dialog.add(okButton);
        dialog.setSize(300, 100);
        dialog.setVisible(true);
    }

    private void openAdminPortal() {
        new AdminPortal().setVisible(true);
    }
    

    public static void main(String[] args) {
        new ViewBorderOfficers();
    }
}
