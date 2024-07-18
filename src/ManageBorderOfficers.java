import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

public class ManageBorderOfficers extends Frame implements ActionListener {
    private TextField searchField;
    private Button searchButton;
    private Button backButton;
    private TextArea resultTextArea;
    private Panel buttonPanel;

    // Database credentials
    static final String DB_URL = "jdbc:mysql://localhost:3309/bms";
    static final String USER = "root";
    static final String PASS = ""; // Replace with your MySQL password

    public ManageBorderOfficers() {
        setTitle("Manage Border Officers");
        setSize(600, 400);
        setLayout(new BorderLayout());

        Panel searchPanel = new Panel();
        searchPanel.setLayout(new FlowLayout());
        searchField = new TextField(20);
        searchField.setText("Enter name or station...");
        searchPanel.add(searchField);
        searchButton = new Button("Search");
        searchButton.addActionListener(this);
        searchPanel.add(searchButton);
        
        // Adding back button to the search panel
        backButton = new Button("Back to Admin Portal");
        backButton.addActionListener(e -> {
            openAdminPortal();
            dispose();
        });
        searchPanel.add(backButton);

        add(searchPanel, BorderLayout.NORTH);

        resultTextArea = new TextArea();
        resultTextArea.setEditable(false);
        add(resultTextArea, BorderLayout.CENTER);

        buttonPanel = new Panel();
        buttonPanel.setLayout(new FlowLayout());
        add(buttonPanel, BorderLayout.SOUTH);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                dispose();
            }
        });

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == searchButton) {
            searchOfficers();
        }
    }

    private void searchOfficers() {
        String searchTerm = searchField.getText();
        if (searchTerm.equals("") || searchTerm.equals("Enter name or station...")) {
            showMessage("Please enter a valid search term.");
            return;
        }
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM border_officers WHERE first_name LIKE ? OR last_name LIKE ? OR station LIKE ?")) {

            stmt.setString(1, "%" + searchTerm + "%");
            stmt.setString(2, "%" + searchTerm + "%");
            stmt.setString(3, "%" + searchTerm + "%");

            ResultSet rs = stmt.executeQuery();

            StringBuilder result = new StringBuilder();
            while (rs.next()) {
                int officerID = rs.getInt("officer_id");
                String name = rs.getString("first_name") + " " + rs.getString("last_name");
                String username = rs.getString("username");
                String email = rs.getString("email");
                String phone = rs.getString("phone_number");
                String station = rs.getString("station");
                result.append("ID: ").append(officerID).append(", Name: ").append(name)
                        .append(", Username: ").append(username).append(", Email: ").append(email)
                        .append(", Phone: ").append(phone).append(", Station: ").append(station).append("\n");

                // Add update and delete buttons for each officer
                addButton(officerID);
            }
            if (result.length() == 0) {
                showMessage("No matching records found.");
            } else {
                resultTextArea.setText(result.toString());
            }

            // Ensure that the update and delete buttons are visible
            revalidate();
            repaint();
        } catch (SQLException ex) {
            ex.printStackTrace();
            showMessage("Database error: " + ex.getMessage());
        }
    }

    private void addButton(int officerID) {
        Button updateButton = new Button("Update");
        updateButton.addActionListener(e -> updateOfficer(officerID));

        Button deleteButton = new Button("Delete");
        deleteButton.addActionListener(e -> deleteOfficer(officerID));

        Panel officerButtonPanel = new Panel();
        officerButtonPanel.add(updateButton);
        officerButtonPanel.add(deleteButton);

        buttonPanel.add(officerButtonPanel);
    }

    private void updateOfficer(int officerID) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            // Fetch officer details to display for reference
            String query = "SELECT * FROM border_officers WHERE officer_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, officerID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String username = rs.getString("username");
                String email = rs.getString("email");
                String phoneNumber = rs.getString("phone_number");
                String station = rs.getString("station");

                // Create a form to update officer details
                JTextField firstNameField = new JTextField(firstName);
                JTextField lastNameField = new JTextField(lastName);
                JTextField usernameField = new JTextField(username);
                JTextField emailField = new JTextField(email);
                JTextField phoneNumberField = new JTextField(phoneNumber);
                JTextField stationField = new JTextField(station);

                JPanel panel = new JPanel(new GridLayout(0, 1));
                panel.add(new JLabel("First Name:"));
                panel.add(firstNameField);
                panel.add(new JLabel("Last Name:"));
                panel.add(lastNameField);
                panel.add(new JLabel("Username:"));
                panel.add(usernameField);
                panel.add(new JLabel("Email:"));
                panel.add(emailField);
                panel.add(new JLabel("Phone Number:"));
                panel.add(phoneNumberField);
                panel.add(new JLabel("Station:"));
                panel.add(stationField);

                int result = JOptionPane.showConfirmDialog(null, panel, "Update Officer Details",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (result == JOptionPane.OK_OPTION) {
                    // Update officer record in the database
                    String updateQuery = "UPDATE border_officers SET first_name = ?, last_name = ?, username = ?, email = ?, phone_number = ?, station = ? WHERE officer_id = ?";
                    PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
                    updateStmt.setString(1, firstNameField.getText());
                    updateStmt.setString(2, lastNameField.getText());
                    updateStmt.setString(3, usernameField.getText());
                    updateStmt.setString(4, emailField.getText());
                    updateStmt.setString(5, phoneNumberField.getText());
                    updateStmt.setString(6, stationField.getText());
                    updateStmt.setInt(7, officerID);

                    int rowsAffected = updateStmt.executeUpdate();
                    if (rowsAffected > 0) {
                        showMessage("Officer details updated successfully.");
                    } else {
                        showMessage("Failed to update officer details.");
                    }
                }
            } else {
                showMessage("Officer ID not found.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            showMessage("Database error: " + ex.getMessage());
        }
    }

    private void deleteOfficer(int officerID) {
        int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this officer?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
                // Execute SQL DELETE query
                String deleteQuery = "DELETE FROM border_officers WHERE officer_id = ?";
                PreparedStatement stmt = conn.prepareStatement(deleteQuery);
                stmt.setInt(1, officerID);

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    showMessage("Officer deleted successfully.");
                } else {
                    showMessage("Failed to delete officer.");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                showMessage("Database error: " + ex.getMessage());
            }
        }
    }

    private void openAdminPortal() {
        // Implement your code to open the admin portal
        new AdminPortal().setVisible(true);
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    public static void main(String[] args) {
        new ManageBorderOfficers();
    }
}
