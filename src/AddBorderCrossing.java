import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddBorderCrossing extends Frame implements ActionListener {
    private TextField driverNameField;
    private TextField vehiclePlateNoField;
    private TextField driverLicenseNoField;
    private TextField crossingDateTimeField;
    private TextField crossingPointField;
    private TextField purposeOfVisitField;
    private TextArea additionalNotesArea;
    private Button submitButton;
    private Button backButton; // Added button for going back to Border Portal

    // Database credentials
    static final String DB_URL = "jdbc:mysql://localhost:3309/bms";
    static final String USER = "root";
    static final String PASS = ""; // Replace with your MySQL password

    public AddBorderCrossing(String station) {
        setTitle("Add Border Crossing");
        setSize(400, 400);
        setLayout(new GridLayout(10, 2)); // Increased rows to accommodate the back button

        add(new Label("Driver Name:"));
        driverNameField = new TextField();
        add(driverNameField);

        add(new Label("Vehicle Plate No:"));
        vehiclePlateNoField = new TextField();
        add(vehiclePlateNoField);

        add(new Label("Driver License No:"));
        driverLicenseNoField = new TextField();
        add(driverLicenseNoField);

        add(new Label("Crossing Date Time (YYYY-MM-DD HH:MM:SS):"));
        crossingDateTimeField = new TextField(getCurrentDateTime()); // Set default value as current date and time
        add(crossingDateTimeField);

        add(new Label("Crossing Point:"));
        crossingPointField = new TextField(station); // Set default value as station
        add(crossingPointField);

        add(new Label("Purpose of Visit:"));
        purposeOfVisitField = new TextField();
        add(purposeOfVisitField);

        add(new Label("Additional Notes:"));
        additionalNotesArea = new TextArea();
        add(additionalNotesArea);

        submitButton = new Button("Submit");
        submitButton.addActionListener(this);
        add(submitButton);

        backButton = new Button("Back to Border Portal");
        backButton.addActionListener(this);
        add(backButton); // Added the back button

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                dispose();
            }
        });

        setVisible(true);
    }

    private String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == submitButton) {
            submitBorderCrossing();
        } else if (e.getSource() == backButton) {
            openBorderPortal();
        }
    }

    private void submitBorderCrossing() {
        String driverName = driverNameField.getText();
        String vehiclePlateNo = vehiclePlateNoField.getText();
        String driverLicenseNo = driverLicenseNoField.getText();
        String crossingDateTime = crossingDateTimeField.getText();
        String crossingPoint = crossingPointField.getText();
        String purposeOfVisit = purposeOfVisitField.getText();
        String additionalNotes = additionalNotesArea.getText();

        // Insert the border crossing details into the database
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO bordercrossings (DriverName, VehiclePlateNo, DriverLicenseNo, CrossingDateTime, BorderCrossingPoint, PurposeOfVisit, AdditionalNotes) VALUES (?, ?, ?, ?, ?, ?, ?)")) {
            stmt.setString(1, driverName);
            stmt.setString(2, vehiclePlateNo);
            stmt.setString(3, driverLicenseNo);
            stmt.setString(4, crossingDateTime);
            stmt.setString(5, crossingPoint);
            stmt.setString(6, purposeOfVisit);
            stmt.setString(7, additionalNotes);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                showMessage("Border crossing details added successfully");
                clearFields();
            } else {
                showMessage("Failed to add border crossing details");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            showMessage("Database error: " + ex.getMessage());
        }
    }

    private void openBorderPortal() {
        new BorderPortal(crossingPointField.getText()).setVisible(true); // Pass the station information to BorderPortal
        dispose(); // Close the AddBorderCrossing window
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

    private void clearFields() {
        driverNameField.setText("");
        vehiclePlateNoField.setText("");
        driverLicenseNoField.setText("");
        crossingDateTimeField.setText(getCurrentDateTime()); // Set current date and time again after submission
        purposeOfVisitField.setText("");
        additionalNotesArea.setText("");
    }
}
