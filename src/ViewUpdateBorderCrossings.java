import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ViewUpdateBorderCrossings extends JFrame implements ActionListener {
    private JTable borderCrossingsTable;
    private JButton updateButton;
    private JTextField driverNameField;
    private JTextField vehiclePlateNoField;
    private JTextField driverLicenseNoField;

    // Database credentials
    static final String DB_URL = "jdbc:mysql://localhost:3309/bms";
    static final String USER = "root";
    static final String PASS = ""; // Replace with your MySQL password

    public ViewUpdateBorderCrossings() {
        setTitle("View and Update Border Crossings");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel topPanel = new JPanel(new BorderLayout());
        JPanel searchPanel = new JPanel();
        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        searchPanel.add(new JLabel("Search by Date/Driving License No/Vehicle No:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        topPanel.add(searchPanel, BorderLayout.NORTH);

        updateButton = new JButton("Update");
        updateButton.addActionListener(this);
        topPanel.add(updateButton, BorderLayout.SOUTH);
        getContentPane().add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(4, 2));
        centerPanel.add(new JLabel("Driver Name:"));
        driverNameField = new JTextField();
        centerPanel.add(driverNameField);
        centerPanel.add(new JLabel("Vehicle Plate No:"));
        vehiclePlateNoField = new JTextField();
        centerPanel.add(vehiclePlateNoField);
        centerPanel.add(new JLabel("Driver License No:"));
        driverLicenseNoField = new JTextField();
        centerPanel.add(driverLicenseNoField);
        getContentPane().add(centerPanel, BorderLayout.CENTER);

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

            DefaultTableModel model = new DefaultTableModel(resizedData, columnNames);
            borderCrossingsTable = new JTable(model);
            JScrollPane scrollPane = new JScrollPane(borderCrossingsTable);
            getContentPane().add(scrollPane, BorderLayout.SOUTH);

        } catch (SQLException ex) {
            ex.printStackTrace();
            showMessage("Database error: " + ex.getMessage());
        }

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == updateButton) {
            updateBorderCrossing();
        }
    }

    private void updateBorderCrossing() {
        int selectedRow = borderCrossingsTable.getSelectedRow();
        if (selectedRow == -1) {
            showMessage("Please select a border crossing to update");
            return;
        }

        String driverName = driverNameField.getText();
        String vehiclePlateNo = vehiclePlateNoField.getText();
        String driverLicenseNo = driverLicenseNoField.getText();

        if (driverName.isEmpty() || vehiclePlateNo.isEmpty() || driverLicenseNo.isEmpty()) {
            showMessage("Please fill in all fields");
            return;
        }

        String selectedDriverLicenseNo = (String) borderCrossingsTable.getValueAt(selectedRow, 2);

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement("UPDATE bordercrossings SET DriverName = ?, VehiclePlateNo = ? WHERE DriverLicenseNo = ?")) {
            stmt.setString(1, driverName);
            stmt.setString(2, vehiclePlateNo);
            stmt.setString(3, selectedDriverLicenseNo);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                showMessage("Border crossing details updated successfully");
                clearFields();
            } else {
                showMessage("Failed to update border crossing details");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            showMessage("Database error: " + ex.getMessage());
        }
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    private void clearFields() {
        driverNameField.setText("");
        vehiclePlateNoField.setText("");
        driverLicenseNoField.setText("");
    }

    public static void main(String[] args) {
        new ViewUpdateBorderCrossings();
    }
}
