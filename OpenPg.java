package projk;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
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
        JButton editButton = new JButton("Edit Events");

        Font retroFont = new Font("Courier New", Font.BOLD, 16);
        Dimension buttonSize = new Dimension(220, 45);
        Color neonBlue = new Color(0, 255, 255);
        Color background = new Color(20, 20, 20);
        Color borderColor = new Color(0, 255, 180);

        for (JButton btn : new JButton[]{bookButton, addButton, editButton}) {
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
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(editButton);

        bookButton.addActionListener(e -> {
            ProjK bookingWindow = new ProjK();
            bookingWindow.setExtendedState(JFrame.MAXIMIZED_BOTH);
            bookingWindow.setVisible(true);
        });

        addButton.addActionListener(e -> openLoginSignupForm(false));
        editButton.addActionListener(e -> openLoginSignupForm(true));

        mainFrame.add(panel);
        mainFrame.setSize(420, 520);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setResizable(true);
        mainFrame.setVisible(true);
    }

    
    private static void openLoginSignupForm(boolean isEditMode) {
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
                    if (isEditMode) openEventEditor(); else openEventForm();
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
        JFrame eventFrame = new JFrame("Add New Event");
        eventFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        eventFrame.setSize(500, 500);
        eventFrame.setLocationRelativeTo(null);
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

    
    private static void openEventEditor() {
        JFrame frame = new JFrame("Edit or Delete Events");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(700, 400);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(new Color(25, 25, 25));

        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> eventList = new JList<>(listModel);
        eventList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        eventList.setForeground(Color.CYAN);
        eventList.setBackground(Color.BLACK);
        eventList.setFont(new Font("Courier New", Font.PLAIN, 14));

        JButton editButton = new JButton("Edit Selected");
        JButton deleteButton = new JButton("Delete Selected");
        JButton refreshButton = new JButton("Refresh");

        styleRetroButton(editButton, Color.PINK);
        styleRetroButton(deleteButton, Color.RED);
        styleRetroButton(refreshButton, Color.GREEN);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(25, 25, 25));
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        frame.add(new JScrollPane(eventList), BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Function to load events
        Runnable loadEvents = () -> {
            listModel.clear();
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement ps = conn.prepareStatement("SELECT id, event_name, venue, event_date, college_name FROM events");
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    String line = rs.getInt("id") + " | " + rs.getString("event_name") +
                            " | " + rs.getString("venue") + " | " +
                            rs.getString("event_date") + " | " +
                            rs.getString("college_name");
                    listModel.addElement(line);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        };
        loadEvents.run();

        deleteButton.addActionListener(e -> {
            String selected = eventList.getSelectedValue();
            if (selected == null) {
                JOptionPane.showMessageDialog(frame, "Select an event to delete.");
                return;
            }
            int id = Integer.parseInt(selected.split(" \\| ")[0]);
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement ps = conn.prepareStatement("DELETE FROM events WHERE id = ?")) {
                ps.setInt(1, id);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(frame, "Event deleted!");
                loadEvents.run();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        editButton.addActionListener(e -> {
            String selected = eventList.getSelectedValue();
            if (selected == null) {
                JOptionPane.showMessageDialog(frame, "Select an event to edit.");
                return;
            }

            String[] parts = selected.split(" \\| ");
            int id = Integer.parseInt(parts[0]);
            String eventName = parts[1].trim();
            String venue = parts[2].trim();
            String date = parts[3].trim();
            String college = parts[4].trim();

            JTextField nameField = new JTextField(eventName);
            JTextField venueField = new JTextField(venue);
            JTextField dateField = new JTextField(date);
            JTextField collegeField = new JTextField(college);

            JPanel panel = new JPanel(new GridLayout(0, 1));
            panel.add(new JLabel("Event Name:")); panel.add(nameField);
            panel.add(new JLabel("Venue:")); panel.add(venueField);
            panel.add(new JLabel("Date:")); panel.add(dateField);
            panel.add(new JLabel("College:")); panel.add(collegeField);

            int result = JOptionPane.showConfirmDialog(frame, panel, "Edit Event", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                try (Connection conn = DBConnection.getConnection();
                     PreparedStatement ps = conn.prepareStatement(
                             "UPDATE events SET event_name=?, venue=?, event_date=?, college_name=? WHERE id=?")) {
                    ps.setString(1, nameField.getText().trim());
                    ps.setString(2, venueField.getText().trim());
                    ps.setString(3, dateField.getText().trim());
                    ps.setString(4, collegeField.getText().trim());
                    ps.setInt(5, id);
                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(frame, "Event updated!");
                    loadEvents.run();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        refreshButton.addActionListener(e -> loadEvents.run());
        frame.setVisible(true);
    }

    
    private static void styleRetroButton(JButton btn, Color color) {
        btn.setFont(new Font("Courier New", Font.BOLD, 16));
        btn.setBackground(Color.BLACK);
        btn.setForeground(color);
        btn.setBorder(BorderFactory.createLineBorder(color, 2));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}
