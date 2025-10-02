package projk;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

public class OpenPg {
    public static void main(String[] args) {

        JFrame frame = new JFrame("Image Icon");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(Color.WHITE);

        URL imgURL = OpenPg.class.getResource("/icon/icon.png");
        ImageIcon myIcon = null;

        if (imgURL != null) {
            ImageIcon originalIcon = new ImageIcon(imgURL);
            int desiredWidth = 300;
            int desiredHeight = 300;

            Image originalImage = originalIcon.getImage();
            Image scaledImage = originalImage.getScaledInstance(desiredWidth, desiredHeight, Image.SCALE_SMOOTH);
            myIcon = new ImageIcon(scaledImage);
        } else {
            System.err.println("Couldn't find file: /icon/icon.png");
            frame.add(new JLabel("Image not found at /icon/icon.png"));
            frame.setSize(300, 100);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            return;
        }

        JLabel imageLabel = new JLabel(myIcon);
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton bookButton = new JButton("Book an Event");
        bookButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        bookButton.setFont(new Font("Arial", Font.BOLD, 16));
        bookButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        bookButton.setMaximumSize(new Dimension(200, 40));

        JButton addButton = new JButton("Add an Event");
        addButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        addButton.setFont(new Font("Arial", Font.BOLD, 16));
        addButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addButton.setMaximumSize(new Dimension(200, 40));

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        panel.add(imageLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 25)));
        panel.add(bookButton);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(addButton);

        bookButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ProjK bookingWindow = new ProjK();
                bookingWindow.setVisible(true);
            }
        });

        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openLoginWindow();
            }
        });

        frame.setSize(400, 400);
        frame.add(panel);
        frame.setIconImage(myIcon.getImage());
        frame.pack();
        frame.setMinimumSize(frame.getSize());
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static void openLoginWindow() {
        JFrame loginFrame = new JFrame("Admin Login");
        loginFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        loginFrame.setSize(350, 200);
        loginFrame.setLayout(new GridBagLayout());
        loginFrame.setLocationRelativeTo(null);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel userLabel = new JLabel("Username:");
        JTextField userField = new JTextField(15);

        JLabel passLabel = new JLabel("Password:");
        JPasswordField passField = new JPasswordField(15);

        JButton loginButton = new JButton("Login");

        gbc.gridx = 0;
        gbc.gridy = 0;
        loginFrame.add(userLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        loginFrame.add(userField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        loginFrame.add(passLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        loginFrame.add(passField, gbc);

        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 2;
        loginFrame.add(loginButton, gbc);

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = userField.getText().trim();
                String password = new String(passField.getPassword()).trim();

                if (username.equals("admin") && password.equals("1234")) {
                    JOptionPane.showMessageDialog(loginFrame, "Login successful!");
                    loginFrame.dispose();
                    openEventForm();
                } else {
                    JOptionPane.showMessageDialog(loginFrame, "Invalid username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        loginFrame.setVisible(true);
    }

    private static void openEventForm() {
        JFrame eventFrame = new JFrame("Add New Event");
        eventFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        eventFrame.setSize(400, 350);
        eventFrame.setLayout(new GridBagLayout());
        eventFrame.setLocationRelativeTo(null);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        
        JLabel nameLabel = new JLabel("Event Name:");
        JTextField nameField = new JTextField(20);

        JLabel venueLabel = new JLabel("Venue:");
        JTextField venueField = new JTextField(20);

        JLabel dateLabel = new JLabel("Date:");
        JTextField dateField = new JTextField(20);

        JLabel collegeLabel = new JLabel("College Name:");
        JTextField collegeField = new JTextField(20);

        JButton submitButton = new JButton("Submit");

       
        gbc.gridx = 0; gbc.gridy = 0;
        eventFrame.add(nameLabel, gbc);
        gbc.gridx = 1;
        eventFrame.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        eventFrame.add(venueLabel, gbc);
        gbc.gridx = 1;
        eventFrame.add(venueField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        eventFrame.add(dateLabel, gbc);
        gbc.gridx = 1;
        eventFrame.add(dateField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        eventFrame.add(collegeLabel, gbc);
        gbc.gridx = 1;
        eventFrame.add(collegeField, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        eventFrame.add(submitButton, gbc);

        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
        
                String eventName = nameField.getText().trim();
                String venue = venueField.getText().trim();
                String date = dateField.getText().trim();
                String college = collegeField.getText().trim();

                
                JOptionPane.showMessageDialog(eventFrame, "Event added successfully!");
                eventFrame.dispose();
            }
        });

        eventFrame.setVisible(true);
    }
}
