import java.awt.*;
import java.awt.event.*;

public class BorderPortal extends Frame implements ActionListener {
    private Button addBorderCrossingButton;
    private Button viewBorderCrossingsButton;
    private Button updateButton;
    private Button logoutButton;

    public BorderPortal(String station) {
        setTitle("Border Portal - " + station);
        setSize(400, 300);
        setLayout(new GridLayout(4, 1)); // Adjust the grid layout to match the number of buttons

        addBorderCrossingButton = new Button("Add Border Crossing");
        viewBorderCrossingsButton = new Button("View Border Crossings");
        updateButton = new Button("Update Border Crossing");
        logoutButton = new Button("Logout");

        add(addBorderCrossingButton);
        add(viewBorderCrossingsButton);
        add(updateButton);
        add(logoutButton);

        addBorderCrossingButton.addActionListener(e -> openAddBorderCrossing(station));
        viewBorderCrossingsButton.addActionListener(this);
        updateButton.addActionListener(e -> openUpdateBorderCrossing(station));
        logoutButton.addActionListener(e -> {
            new BLogin().setVisible(true);
            dispose();
        });

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                dispose();
            }
        });

        setVisible(true);
    }

    private void openAddBorderCrossing(String station) {
        new AddBorderCrossing(station).setVisible(true);
        dispose(); // Close the BorderPortal window
    }

    private void openUpdateBorderCrossing(String station) {
        new SearchAndUpdateCrossingDetails().setVisible(true);
        dispose(); // Close the BorderPortal window
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == viewBorderCrossingsButton) {
            new ViewBorderCrossings().setVisible(true); // Open the ViewBorderCrossings window
            dispose(); // Close the BorderPortal window
        }
    }

    @SuppressWarnings("unused")
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
        new BorderPortal("Station Name"); // Replace "Station Name" with the actual station name
    }
}
