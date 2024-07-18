import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class AViewBorderCrossings extends Frame implements ActionListener {
    private TextField searchField;
    private Choice searchCriteriaChoice;
    private Button searchButton;
    private Button backButton;
    private JTable borderCrossingsTable;
    private JScrollPane scrollPane;

    // Database credentials
    static final String DB_URL = "jdbc:mysql://localhost:3309/bms";
    static final String USER = "root";
    static final String PASS = ""; // Replace with your MySQL password

    public AViewBorderCrossings() {
        setTitle("View Border Crossings");
        setSize(800, 600);
        setLayout(new BorderLayout());

        Panel topPanel = new Panel();
        topPanel.setLayout(new FlowLayout());

        searchCriteriaChoice = new Choice();
        searchCriteriaChoice.add("Date");
        searchCriteriaChoice.add("Driver Name");
        searchCriteriaChoice.add("Vehicle Plate No");
        searchCriteriaChoice.add("Driver License No");
        topPanel.add(searchCriteriaChoice);

        searchField = new TextField(20);
        topPanel.add(searchField);

        searchButton = new Button("Search");
        searchButton.addActionListener(this);
        topPanel.add(searchButton);

        backButton = new Button("Back to Admin Portal");
        backButton.addActionListener(e -> {
            openAdminPortal(); // Navigate back to the admin portal
            dispose(); // Close the ViewBorderCrossings window
        });
        topPanel.add(backButton);

        add(topPanel, BorderLayout.NORTH);

        borderCrossingsTable = new JTable();
        scrollPane = new JScrollPane(borderCrossingsTable);
        add(scrollPane, BorderLayout.CENTER);

        fetchBorderCrossings(null, null);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                dispose();
            }
        });

        setVisible(true);
    }

    private void fetchBorderCrossings(String criteria, String value) {
        String query = "SELECT * FROM bordercrossings";
        if (criteria != null && value != null && !value.isEmpty()) {
            query += " WHERE " + criteria + " LIKE ?";
        }

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            if (criteria != null && value != null && !value.isEmpty()) {
                stmt.setString(1, "%" + value + "%");
            }

            ResultSet rs = stmt.executeQuery();

            List<String[]> data = new ArrayList<>();
            while (rs.next()) {
                data.add(new String[]{
                        String.valueOf(rs.getInt("CrossingID")),
                        rs.getString("DriverName"),
                        rs.getString("VehiclePlateNo"),
                        rs.getString("DriverLicenseNo"),
                        rs.getString("CrossingDateTime"),
                        rs.getString("BorderCrossingPoint"),
                        rs.getString("PurposeOfVisit"),
                        rs.getString("AdditionalNotes")
                });
            }

            String[] columnNames = {"Crossing ID", "Driver Name", "Vehicle Plate No", "Driver License No", "Crossing Date Time", "Crossing Point", "Purpose of Visit", "Additional Notes"};
            String[][] tableData = data.toArray(new String[0][]);
            borderCrossingsTable.setModel(new javax.swing.table.DefaultTableModel(tableData, columnNames));

        } catch (SQLException ex) {
            ex.printStackTrace();
            showMessage("Database error: " + ex.getMessage());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == searchButton) {
            String criteria = null;
            switch (searchCriteriaChoice.getSelectedItem()) {
                case "Date":
                    criteria = "CrossingDateTime";
                    break;
                case "Driver Name":
                    criteria = "DriverName";
                    break;
                case "Vehicle Plate No":
                    criteria = "VehiclePlateNo";
                    break;
                case "Driver License No":
                    criteria = "DriverLicenseNo";
                    break;
            }
            fetchBorderCrossings(criteria, searchField.getText());
        } else if (e.getSource() == backButton) {
            openAdminPortal();
        }
    }

    private void openAdminPortal() {
        new AdminPortal().setVisible(true);
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
        new AViewBorderCrossings();
    }
}
