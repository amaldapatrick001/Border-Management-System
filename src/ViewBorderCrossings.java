import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ViewBorderCrossings extends JFrame implements ActionListener {
    private JTable borderCrossingsTable;
    private JTextField searchField;
    private JButton searchButton;
    private JButton updateButton; // Button for update
    private JButton backButton;

    // Database credentials
    static final String DB_URL = "jdbc:mysql://localhost:3309/bms";
    static final String USER = "root";
    static final String PASS = ""; // Replace with your MySQL password

    public ViewBorderCrossings() {
        setTitle("View Border Crossings");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel searchPanel = new JPanel();
        searchField = new JTextField(20);
        searchButton = new JButton("Search");
        searchButton.addActionListener(this);
        searchPanel.add(new JLabel("Search by Date/Driving License No/Vehicle No:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        getContentPane().add(searchPanel, BorderLayout.NORTH);

        updateButton = new JButton("Update"); // Create the update button
        updateButton.addActionListener(this); // Add action listener
        getContentPane().add(updateButton, BorderLayout.SOUTH); // Add the button to the bottom of the frame

        backButton = new JButton("Back to BorderPortal");
        backButton.addActionListener(this);
        getContentPane().add(backButton, BorderLayout.SOUTH);

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM bordercrossings")) {

            ResultSetMetaData metaData = rs.getMetaData();

            // Get column names
            int columnCount = metaData.getColumnCount();
            String[] columnNames = new String[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                columnNames[i - 1] = metaData.getColumnName(i);
            }

            // Get data
            Object[][] data = new Object[100][columnCount];
            int rowCount = 0;
            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    data[rowCount][i - 1] = rs.getObject(i);
                }
                rowCount++;
            }

            // Resize data array to fit actual number of rows
            Object[][] resizedData = new Object[rowCount][columnCount];
            for (int i = 0; i < rowCount; i++) {
                resizedData[i] = data[i];
            }

            // Create table
            DefaultTableModel model = new DefaultTableModel(resizedData, columnNames);
            borderCrossingsTable = new JTable(model);
            JScrollPane scrollPane = new JScrollPane(borderCrossingsTable);
            getContentPane().add(scrollPane, BorderLayout.CENTER);

        } catch (SQLException ex) {
            ex.printStackTrace();
            showMessage("Database error: " + ex.getMessage());
        }

        setVisible(true);
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == searchButton) {
            String searchText = searchField.getText();
            String query = "SELECT * FROM bordercrossings WHERE DATE_FORMAT(CrossingDateTime, '%Y-%m-%d') LIKE '%" + searchText + "%' OR DriverLicenseNo LIKE '%" + searchText + "%' OR VehiclePlateNo LIKE '%" + searchText + "%'";
            searchBorderCrossings(query);
        } else if (e.getSource() == updateButton) { // Handle update button click
            int selectedRow = borderCrossingsTable.getSelectedRow();
            if (selectedRow != -1) {
                String driverName = JOptionPane.showInputDialog("Enter new driver name:");
                String driverLicenseNo = JOptionPane.showInputDialog("Enter new driver license number:");
                String vehiclePlateNo = JOptionPane.showInputDialog("Enter new vehicle plate number:");
                updateBorderCrossing(selectedRow, driverName, driverLicenseNo, vehiclePlateNo);
            } else {
                showMessage("Please select a row to update.");
            }
        } else if (e.getSource() == backButton) {
            dispose();
            new BorderPortal(getTitle()).setVisible(true);
        }
    }

    private void searchBorderCrossings(String query) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            ResultSetMetaData metaData = rs.getMetaData();

            // Get column names
            int columnCount = metaData.getColumnCount();
            String[] columnNames = new String[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                columnNames[i - 1] = metaData.getColumnName(i);
            }

            // Get data
            Object[][] data = new Object[100][columnCount];
            int rowCount = 0;
            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    data[rowCount][i - 1] = rs.getObject(i);
                }
                rowCount++;
            }

            // Resize data array to fit actual number of rows
            Object[][] resizedData = new Object[rowCount][columnCount];
            for (int i = 0; i < rowCount; i++) {
                resizedData[i] = data[i];
            }

            // Update table model
            DefaultTableModel model = new DefaultTableModel(resizedData, columnNames);
            borderCrossingsTable.setModel(model);

        } catch (SQLException ex) {
            ex.printStackTrace();
            showMessage("Database error: " + ex.getMessage());
        }
    }

    private void updateBorderCrossing(int row, String driverName, String driverLicenseNo, String vehiclePlateNo) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement()) {
            String updateQuery = "UPDATE bordercrossings SET DriverName = '" + driverName + "', DriverLicenseNo = '" + driverLicenseNo + "', VehiclePlateNo = '" + vehiclePlateNo + "' WHERE id = " + borderCrossingsTable.getValueAt(row, 0);
            int rowsAffected = stmt.executeUpdate(updateQuery);
            if (rowsAffected > 0) {
                showMessage("Border crossing details updated successfully");
                searchBorderCrossings("SELECT * FROM bordercrossings");
            } else {
                showMessage("Failed to update border crossing details");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            showMessage("Database error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        new ViewBorderCrossings();
    }
}
