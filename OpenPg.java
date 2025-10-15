package projk;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.net.URL;

public class OpenPg {
    private static JFrame mainFrame;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(OpenPg::createMainWindow);
    }

    private static void createMainWindow() {
        mainFrame = new JFrame("EventMate");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.getContentPane().setBackground(new Color(20, 20, 20));

        JLabel imageLabel;
        URL imgURL = OpenPg.class.getResource("/icon2/icon2.png");
        if (imgURL != null) {
            ImageIcon originalIcon = new ImageIcon(imgURL);
            Image scaledImage = originalIcon.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
            ImageIcon myIcon = new ImageIcon(scaledImage);
            imageLabel = new JLabel(myIcon);
            mainFrame.setIconImage(myIcon.getImage());
        } else {
            imageLabel = new JLabel("EventMate");
            imageLabel.setForeground(new Color(0, 255, 255));
            imageLabel.setFont(new Font("Courier New", Font.BOLD, 30));
        }
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton bookButton = new JButton("Book an Event");
        JButton addButton = new JButton("Add an Event");

        Font retroFont = new Font("Courier New", Font.BOLD, 16);
        Dimension buttonSize = new Dimension(220, 45);
        Color neonBlue = new Color(0, 255, 255);
        Color background = new Color(20, 20, 20);
        Color borderColor = new Color(0, 255, 180);

        for (JButton btn : new JButton[]{bookButton, addButton}) {
            btn.setFont(retroFont);
            btn.setBackground(background);
            btn.setForeground(neonBlue);
            btn.setBorder(BorderFactory.createLineBorder(borderColor, 2));
            btn.setMaximumSize(buttonSize);
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.setFocusPainted(false);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        panel.add(imageLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 25)));
        panel.add(bookButton);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(addButton);

        bookButton.addActionListener(e -> {
            ProjK bookingWindow = new ProjK();
            bookingWindow.setExtendedState(JFrame.MAXIMIZED_BOTH);
            bookingWindow.setVisible(true);
        });

        addButton.addActionListener(e -> openLoginSignupForm());

        mainFrame.add(panel);
        mainFrame.setSize(420, 480);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setResizable(true);
        mainFrame.setVisible(true);
    }

    private static void openLoginSignupForm() {
        JFrame loginFrame = new JFrame("Login / Sign Up");
        loginFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        loginFrame.setSize(400, 350);
        loginFrame.setLocationRelativeTo(null);
        loginFrame.setResizable(false);
        loginFrame.getContentPane().setBackground(new Color(25, 25, 25));
        loginFrame.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField(20);
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField(20);

        JButton loginButton = new JButton("Login");
        JButton signupButton = new JButton("Sign Up");

        Color neonPink = new Color(255, 105, 180);
        usernameLabel.setForeground(neonPink);
        passwordLabel.setForeground(neonPink);
        styleRetroButton(loginButton, neonPink);
        styleRetroButton(signupButton, neonPink);

        gbc.gridx = 0; gbc.gridy = 0;
        loginFrame.add(usernameLabel, gbc);
        gbc.gridx = 1;
        loginFrame.add(usernameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        loginFrame.add(passwordLabel, gbc);
        gbc.gridx = 1;
        loginFrame.add(passwordField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        loginFrame.add(loginButton, gbc);
        gbc.gridy = 3;
        loginFrame.add(signupButton, gbc);

        loginButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(loginFrame, "Enter both username and password.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(
                        "SELECT * FROM users WHERE username = ? AND password = ?"
                );
                ps.setString(1, username);
                ps.setString(2, password);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    JOptionPane.showMessageDialog(loginFrame, "Login successful!");
                    loginFrame.dispose();
                    openEventForm(); // Open Add Event form after login
                } else {
                    JOptionPane.showMessageDialog(loginFrame, "Invalid username or password.", "Error", JOptionPane.ERROR_MESSAGE);
                }

                rs.close();
                ps.close();
                conn.close();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(loginFrame, "Database error.");
            }
        });

        signupButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(loginFrame, "Enter both username and password.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO users (username, password) VALUES (?, ?)"
                );
                ps.setString(1, username);
                ps.setString(2, password);
                ps.executeUpdate();
                ps.close();
                conn.close();

                JOptionPane.showMessageDialog(loginFrame, "Sign up successful! You can now login.");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(loginFrame, "Failed to sign up. Username may already exist.");
            }
        });

        loginFrame.setVisible(true);
    }

    private static void openEventForm() {
        // This is the same Add Event form from before
        JFrame eventFrame = new JFrame("Add New Event");
        eventFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        eventFrame.setSize(500, 500);
        eventFrame.setLocationRelativeTo(null);
        eventFrame.setResizable(true);
        eventFrame.getContentPane().setBackground(new Color(25, 25, 25));
        eventFrame.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel nameLabel = new JLabel("Event Name:");
        JTextField nameField = new JTextField(20);
        JLabel venueLabel = new JLabel("Venue:");
        JTextField venueField = new JTextField(20);
        JLabel dateLabel = new JLabel("Date (YYYY-MM-DD):");
        JTextField dateField = new JTextField(20);
        JLabel collegeLabel = new JLabel("College Name:");
        JTextField collegeField = new JTextField(20);

        JButton submitButton = new JButton("Submit");
        JButton backButton = new JButton("Back");

        Color neonPink = new Color(255, 105, 180);
        for (JLabel lbl : new JLabel[]{nameLabel, venueLabel, dateLabel, collegeLabel}) lbl.setForeground(neonPink);
        for (JButton btn : new JButton[]{submitButton, backButton}) styleRetroButton(btn, neonPink);

        JPanel namePanel = createClearableField(nameField);
        JPanel venuePanel = createClearableField(venueField);
        JPanel datePanel = createClearableField(dateField);
        JPanel collegePanel = createClearableField(collegeField);

        gbc.gridx = 0; gbc.gridy = 0;
        eventFrame.add(nameLabel, gbc);
        gbc.gridx = 1;
        eventFrame.add(namePanel, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        eventFrame.add(venueLabel, gbc);
        gbc.gridx = 1;
        eventFrame.add(venuePanel, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        eventFrame.add(dateLabel, gbc);
        gbc.gridx = 1;
        eventFrame.add(datePanel, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        eventFrame.add(collegeLabel, gbc);
        gbc.gridx = 1;
        eventFrame.add(collegePanel, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        eventFrame.add(submitButton, gbc);
        gbc.gridy = 5;
        eventFrame.add(backButton, gbc);

        submitButton.addActionListener(e -> {
            String eventName = nameField.getText().trim();
            String venue = venueField.getText().trim();
            String date = dateField.getText().trim();
            String college = collegeField.getText().trim();

            if (eventName.isEmpty() || venue.isEmpty() || date.isEmpty() || college.isEmpty()) {
                JOptionPane.showMessageDialog(eventFrame, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO events (event_name, venue, event_date, college_name) VALUES (?, ?, ?, ?)"
                );
                ps.setString(1, eventName);
                ps.setString(2, venue);
                ps.setString(3, date);
                ps.setString(4, college);
                ps.executeUpdate();
                ps.close();
                conn.close();

                JOptionPane.showMessageDialog(eventFrame, "Event added successfully!");
                eventFrame.dispose();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(eventFrame, "Failed to add event to database.");
            }
        });

        backButton.addActionListener(e -> eventFrame.dispose());
        eventFrame.setVisible(true);
    }

    private static JPanel createClearableField(JTextField textField) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(30, 30, 30));
        textField.setFont(new Font("Courier New", Font.PLAIN, 16));
        textField.setBackground(Color.BLACK);
        textField.setForeground(Color.GREEN);
        textField.setCaretColor(Color.GREEN);
        panel.add(textField, BorderLayout.CENTER);

        JButton clearButton = new JButton("×");
        clearButton.setMargin(new Insets(1, 10, 1, 10));
        clearButton.setFocusable(false);
        clearButton.setFont(new Font("Courier New", Font.BOLD, 16));
        clearButton.setForeground(Color.RED);
        clearButton.setBackground(Color.BLACK);
        clearButton.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
        clearButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        clearButton.setToolTipText("Clear");
        clearButton.addActionListener(e -> textField.setText(""));

        panel.add(clearButton, BorderLayout.EAST);
        return panel;
    }

    private static void styleRetroButton(JButton btn, Color color) {
        btn.setFont(new Font("Courier New", Font.BOLD, 16));
        btn.setBackground(Color.BLACK);
        btn.setForeground(color);
        btn.setBorder(BorderFactory.createLineBorder(color, 2));
        btn.setFocusPainted(false);
    }
}
