import java.awt.*;
import java.awt.event.*;

public class ALogin extends Frame implements ActionListener {
    private TextField usernameField;
    private TextField passwordField;
    private Button loginButton;

    public ALogin() {
        setTitle("Administrator Login");
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
        if ("admin".equals(username) && "admin@123".equals(password)) {
            showMessage("Login successful");
            new AdminPortal().setVisible(true);
            dispose(); // Close the login window
        } else {
            showMessage("Invalid username or password");
        }
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
        new ALogin();
    }
}

class AdminPortal extends Frame {
    private Button addOfficerButton;
    private Button manageOfficerButton;
    private Button viewCrossButton;
    private Button logoutButton;
    private Button viewBorderOfficersButton;

    public AdminPortal() {
        setTitle("Administrator Panel");
        setSize(400, 300);
        setLayout(new GridLayout(5, 1));

        addOfficerButton = new Button("Add Officer");
        viewBorderOfficersButton = new Button("View Border officers");
        manageOfficerButton = new Button("Manage Officers"); //update and delete
        viewCrossButton = new Button("View Border Cross");
        logoutButton = new Button("Logout");

        add(addOfficerButton);
        add(viewBorderOfficersButton);
        add(manageOfficerButton);
        add(viewCrossButton);
        add(logoutButton);

        addOfficerButton.addActionListener(e -> {
            new AddBorderOfficerForm().setVisible(true);
            dispose();
        });

        viewBorderOfficersButton.addActionListener(new ViewBorderOfficersActionListener());

        manageOfficerButton.addActionListener(e -> {
            new ManageBorderOfficers().setVisible(true);
            dispose();
        });

        viewCrossButton.addActionListener(e -> {
            new AViewBorderCrossings().setVisible(true);
            dispose();
        });

        logoutButton.addActionListener(e -> {
            new HomePage().setVisible(true);
            dispose();
        });

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                dispose();
            }
        });

        setVisible(true);
    }

    private class ViewBorderOfficersActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            new ViewBorderOfficers().setVisible(true);
            dispose();
        }
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
}
