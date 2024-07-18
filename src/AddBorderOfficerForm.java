import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class AddBorderOfficerForm extends Frame implements ActionListener, ItemListener {
    private TextField firstNameField, lastNameField, usernameField, emailField, phoneNumberField, passwordField;
    private Choice stateChoice, checkpostChoice;
    private Button addButton, backButton;

    // Database credentials
    static final String DB_URL = "jdbc:mysql://localhost:3309/bms";
    static final String USER = "root";
    static final String PASS = ""; // Replace with your MySQL password

    private Map<String, String[]> stateCheckpostsMap;

    public AddBorderOfficerForm() {
        setTitle("Add Border Officer");
        setSize(400, 500);
        setLayout(new GridLayout(11, 2));

        // Initialize state and checkpost data
        stateCheckpostsMap = new HashMap<>();
        stateCheckpostsMap.put("Karnataka", new String[]{
                "Talapady Checkpost", "Thalappuzha Checkpost", "Makutta Checkpost", "Muthanga Checkpost"
        });
        stateCheckpostsMap.put("Tamil Nadu", new String[]{
                "Walayar Checkpost", "Kumily Checkpost", "Kaliyakkavilai Checkpost", "Bodimettu Checkpost"
        });

        addFormComponents();

        addButton = new Button("Add Officer");
        addButton.addActionListener(this);
        backButton = new Button("Back to Admin Portal");
        backButton.addActionListener(this);

        add(new Label()); // Empty cell for alignment
        add(addButton);
        add(backButton);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            }
        });

        setVisible(true);
    }

    private void addFormComponents() {
        add(new Label("First Name:"));
        firstNameField = new TextField();
        add(firstNameField);

        add(new Label("Last Name:"));
        lastNameField = new TextField();
        add(lastNameField);

        add(new Label("Username:"));
        usernameField = new TextField();
        add(usernameField);

        add(new Label("Password:"));
        passwordField = new TextField();
        add(passwordField);

        add(new Label("Email:"));
        emailField = new TextField();
        add(emailField);

        add(new Label("Phone Number:"));
        phoneNumberField = new TextField();
        add(phoneNumberField);

        add(new Label("State:"));
        stateChoice = new Choice();
        stateChoice.add("");
        stateChoice.add("Karnataka");
        stateChoice.add("Tamil Nadu");
        stateChoice.addItemListener(this);
        add(stateChoice);

        add(new Label("Checkpost:"));
        checkpostChoice = new Choice();
        checkpostChoice.add("");
        add(checkpostChoice);

        updateCheckposts(stateChoice.getSelectedItem());
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() == stateChoice) {
            updateCheckposts(stateChoice.getSelectedItem());
        }
    }

    private void updateCheckposts(String state) {
        checkpostChoice.removeAll();
        checkpostChoice.add("");
        String[] checkposts = stateCheckpostsMap.get(state);
        if (checkposts != null) {
            for (String checkpost : checkposts) {
                checkpostChoice.add(checkpost);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String username = usernameField.getText();
            String password = passwordField.getText();
            String email = emailField.getText();
            String phoneNumber = phoneNumberField.getText();
            String state = stateChoice.getSelectedItem();
            String checkpost = checkpostChoice.getSelectedItem();

            if (isAnyFieldEmpty(firstName, lastName, username, password, email, phoneNumber, state, checkpost)) {
                showMessage("All fields are required");
            } else {
                addOfficerToDatabase(firstName, lastName, username, password, email, phoneNumber, state, checkpost);
            }
        } else if (e.getSource() == backButton) {
            openAdminPortal(); // Navigate back to the admin portal
        }
    }

    private boolean isAnyFieldEmpty(String... fields) {
        for (String field : fields) {
            if (field == null || field.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    private void addOfficerToDatabase(String firstName, String lastName, String username, String password, String email, String phoneNumber, String state, String checkpost) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO border_officers (first_name, last_name, username, password, email, phone_number, station, hire_date, status) VALUES (?, ?, ?, ?, ?, ?, ?, CURDATE(), 'active')")) {

            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            stmt.setString(3, username);
            stmt.setString(4, password);
            stmt.setString(5, email);
            stmt.setString(6, phoneNumber);
            stmt.setString(7, state + " - " + checkpost);

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                showMessage("Border officer added successfully");
                clearForm();
            } else {
                showMessage("Error adding border officer");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            showMessage("Database error: " + ex.getMessage());
        }
    }

    private void clearForm() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'clearForm'");
    }

    private void showMessage(String message) {
        Dialog dialog = new Dialog(this, "Message", true);
        dialog.setLayout(new FlowLayout());
       
        dialog.add(new Label(message));
        Button okButton = new Button("OK");
        okButton.addActionListener(ae -> dialog.setVisible(false));
        dialog.add(okButton);
        dialog.setSize(300, 150);
        dialog.setVisible(true);
    }

    private void openAdminPortal() {
        new AdminPortal().setVisible(true); // Open the admin portal
        dispose(); // Close the current window
    }

    public static void main(String[] args) {
        new AddBorderOfficerForm();
    }
}
