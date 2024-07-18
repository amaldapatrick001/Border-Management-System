import java.awt.*;
import java.awt.event.*;

public class HomePage extends Frame {

    // Constructor to set up the GUI
    public HomePage() {
        // Set the title of the window
        setTitle("Border Management System - Home");

        // Set the size of the window
        setSize(400, 200);

        // Set layout manager for the frame
        setLayout(new GridLayout(3, 1));

        // Initialize components
        Label welcomeLabel = new Label("Welcome to Border Management System", Label.CENTER);
        Button borderOfficerButton = new Button("Border Officer Portal");
        Button adminButton = new Button("Administrator Portal");

        // Add components to the frame
        add(welcomeLabel);
        add(borderOfficerButton);
        add(adminButton);

        // Add action listeners for buttons
        borderOfficerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Open Border Officer Portal login
                new BLogin().setVisible(true);
                // Close the current window
                dispose();
            }
        });

        adminButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Open Administrator Portal login
                new ALogin().setVisible(true);
                // Close the current window
                dispose();
            }
        });

        // Handle window closing event
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                System.exit(0);
            }
        });
    }

    // Main method to run the application
    public static void main(String[] args) {
        // Create an instance of the home page
        HomePage homePage = new HomePage();

        // Make the window visible
        homePage.setVisible(true);
    }
}
