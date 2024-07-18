import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class SearchAndUpdateCrossingDetails extends JFrame implements ActionListener {
    private JTextField searchField;
    private JButton searchButton;
    private JTable borderCrossingsTable;
    private JButton updateButton;
    private JButton backButton; // Back button

    static final String DB_URL = "jdbc:mysql://localhost:3309/BMS";
    static final String USER = "root";
    static final String PASS = "";

    public SearchAndUpdateCrossingDetails() {
        setTitle("Search and Update Crossing Details");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel searchPanel = new JPanel();
        searchField = new JTextField(20);
        searchButton = new JButton("Search");
        searchButton.addActionListener(this);
        searchPanel.add(new JLabel("Search by Date/Vehicle Plate No/Driving License No:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        getContentPane().add(searchPanel, BorderLayout.NORTH);

        updateButton = new JButton("Update");
        updateButton.addActionListener(this);
        getContentPane().add(updateButton, BorderLayout.SOUTH);

        backButton = new JButton("Back to Border Portal"); // Create back button
        backButton.addActionListener(this); // Add action listener
        getContentPane().add(backButton, BorderLayout.SOUTH); // Add the button to the bottom of the frame

        borderCrossingsTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(borderCrossingsTable);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }


    private void searchCrossingDetails(String searchText) {
        String query = "SELECT * FROM bordercrossings WHERE DATE_FORMAT(CrossingDateTime, '%Y-%m-%d') LIKE ? " +
                "OR VehiclePlateNo LIKE ? OR DriverLicenseNo LIKE ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, "%" + searchText + "%");
            pstmt.setString(2, "%" + searchText + "%");
            pstmt.setString(3, "%" + searchText + "%");
            ResultSet rs = pstmt.executeQuery();

            DefaultTableModel model = new DefaultTableModel();
            borderCrossingsTable.setModel(model);

            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                model.addColumn(metaData.getColumnName(i));
            }

            while (rs.next()) {
                Object[] rowData = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    rowData[i - 1] = rs.getObject(i);
                }
                model.addRow(rowData);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            showMessage("Error: Failed to search crossing details. Please try again.");
        }
    }

    private void updateCrossingDetails() {
        int selectedRow = borderCrossingsTable.getSelectedRow();
        if (selectedRow != -1) {
            String newDriverName = null;
            String newVehiclePlateNo = null;
            String newDriverLicenseNo = null;
            
            // Prompt user for which fields to update
            String[] options = {"Driver Name", "Vehicle Plate Number", "Driver License Number", "Cancel"};
            int choice = JOptionPane.showOptionDialog(this, "Which field do you want to update?", "Update Fields",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
    
            if (choice == 0) {
                newDriverName = JOptionPane.showInputDialog("Enter new driver name:");
            } else if (choice == 1) {
                newVehiclePlateNo = JOptionPane.showInputDialog("Enter new vehicle plate number:");
            } else if (choice == 2) {
                newDriverLicenseNo = JOptionPane.showInputDialog("Enter new driver license number:");
            } else {
                showMessage("Update canceled.");
                return;
            }
    
            if ((newDriverName != null && !newDriverName.isEmpty()) ||
                (newVehiclePlateNo != null && !newVehiclePlateNo.isEmpty()) ||
                (newDriverLicenseNo != null && !newDriverLicenseNo.isEmpty())) {
                try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                     Statement stmt = conn.createStatement()) {
                    int id = (int) borderCrossingsTable.getValueAt(selectedRow, 0);
                    StringBuilder updateQuery = new StringBuilder("UPDATE bordercrossings SET ");
                    if (newDriverName != null && !newDriverName.isEmpty()) {
                        updateQuery.append("DriverName = '").append(newDriverName).append("', ");
                    }
                    if (newVehiclePlateNo != null && !newVehiclePlateNo.isEmpty()) {
                        updateQuery.append("VehiclePlateNo = '").append(newVehiclePlateNo).append("', ");
                    }
                    if (newDriverLicenseNo != null && !newDriverLicenseNo.isEmpty()) {
                        updateQuery.append("DriverLicenseNo = '").append(newDriverLicenseNo).append("', ");
                    }
                    // Remove the last comma and space
                    updateQuery.setLength(updateQuery.length() - 2);
                    updateQuery.append(" WHERE CrossingID = ").append(id);
    
                    int rowsAffected = stmt.executeUpdate(updateQuery.toString());
                    if (rowsAffected > 0) {
                        showMessage("Crossing details updated successfully");
                        searchCrossingDetails(searchField.getText());
                    } else {
                        showMessage("Failed to update crossing details");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    showMessage("Error: Failed to update crossing details. Please try again.");
                }
            } else {
                showMessage("Update canceled. No changes were made.");
            }
        } else {
            showMessage("Please select a row to update.");
        }
    }
    
    private void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == searchButton) {
            searchCrossingDetails(searchField.getText());
        } else if (e.getSource() == updateButton) {
            updateCrossingDetails();
        } else if (e.getSource() == backButton) { // Handle back button click
            dispose(); // Close the current window
            new BorderPortal(getTitle()).setVisible(true); // Show the Border Portal window
        }
    }

    public static void main(String[] args) {
        new SearchAndUpdateCrossingDetails();
    }
}