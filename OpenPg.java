package projk;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class OpenPg {
    private static Map<String, String> tempUsers = new HashMap<>();

    public static void main(String[] args) {
        JFrame frame = new JFrame("EventMate");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(new Color(20, 20, 20));

        JLabel imageLabel;
        URL imgURL = OpenPg.class.getResource("/icon2/icon2.png");
        if (imgURL != null) {
            ImageIcon originalIcon = new ImageIcon(imgURL);
            Image scaledImage = originalIcon.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
            ImageIcon myIcon = new ImageIcon(scaledImage);
            imageLabel = new JLabel(myIcon);
            frame.setIconImage(myIcon.getImage());
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

        addButton.addActionListener(e -> openLoginWindow());

        frame.add(panel);
        frame.setSize(420, 480);
        frame.setLocationRelativeTo(null);
        frame.setResizable(true);
        frame.setVisible(true);
    }

    private static void openLoginWindow() {
        JFrame loginFrame = new JFrame("Admin Login");
        loginFrame.setUndecorated(true);
        loginFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        loginFrame.setSize(400, 300);
        loginFrame.setLocationRelativeTo(null);
        loginFrame.setResizable(true);
        loginFrame.setOpacity(0f);
        loginFrame.setVisible(true);

        JLabel userLabel = new JLabel("Username:");
        JTextField userField = new JTextField(15);
        JLabel passLabel = new JLabel("Password:");
        JPasswordField passField = new JPasswordField(15);

        JButton loginButton = new JButton("Login");
        JButton signUpButton = new JButton("Sign Up");
        JButton backButton = new JButton("Back");

        Color neonGreen = new Color(0, 255, 100);
        for (JLabel lbl : new JLabel[]{userLabel, passLabel}) lbl.setForeground(neonGreen);
        for (JButton btn : new JButton[]{loginButton, signUpButton, backButton}) styleRetroButton(btn, neonGreen);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        loginFrame.setLayout(new GridBagLayout());

        gbc.gridx = 0; gbc.gridy = 0;
        loginFrame.add(userLabel, gbc);
        gbc.gridx = 1;
        loginFrame.add(userField, gbc);
        gbc.gridx = 0; gbc.gridy = 1;
        loginFrame.add(passLabel, gbc);
        gbc.gridx = 1;
        loginFrame.add(passField, gbc);
        gbc.gridwidth = 2; gbc.gridx = 0; gbc.gridy = 2;
        loginFrame.add(loginButton, gbc);
        gbc.gridy = 3;
        loginFrame.add(signUpButton, gbc);
        gbc.gridy = 4;
        loginFrame.add(backButton, gbc);

        loginButton.addActionListener(e -> {
            String username = userField.getText().trim();
            String password = new String(passField.getPassword()).trim();
            if ((username.equals("admin") && password.equals("1234")) || 
                (tempUsers.containsKey(username) && tempUsers.get(username).equals(password))) {
                fadeOut(loginFrame, () -> SwingUtilities.invokeLater(OpenPg::openEventForm));
            } else {
                JOptionPane.showMessageDialog(loginFrame, "Invalid username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        });

        signUpButton.addActionListener(e -> fadeOut(loginFrame, OpenPg::openSignUpWindow));
        backButton.addActionListener(e -> fadeOut(loginFrame, null));

        fadeIn(loginFrame);
    }

    private static void openSignUpWindow() {
        JFrame signUpFrame = new JFrame("Sign Up");
        signUpFrame.setUndecorated(true);
        signUpFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        signUpFrame.setSize(400, 300);
        signUpFrame.setLocationRelativeTo(null);
        signUpFrame.setResizable(true);
        signUpFrame.setOpacity(0f);
        signUpFrame.setVisible(true);

        JLabel userLabel = new JLabel("New Username:");
        JTextField userField = new JTextField(15);
        JLabel passLabel = new JLabel("New Password:");
        JPasswordField passField = new JPasswordField(15);

        JButton createButton = new JButton("Create Account");
        JButton backButton = new JButton("Back");

        Color neonGreen = new Color(0, 255, 100);
        for (JLabel lbl : new JLabel[]{userLabel, passLabel}) lbl.setForeground(neonGreen);
        for (JButton btn : new JButton[]{createButton, backButton}) styleRetroButton(btn, neonGreen);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        signUpFrame.setLayout(new GridBagLayout());

        gbc.gridx = 0; gbc.gridy = 0;
        signUpFrame.add(userLabel, gbc);
        gbc.gridx = 1;
        signUpFrame.add(userField, gbc);
        gbc.gridx = 0; gbc.gridy = 1;
        signUpFrame.add(passLabel, gbc);
        gbc.gridx = 1;
        signUpFrame.add(passField, gbc);
        gbc.gridwidth = 2; gbc.gridx = 0; gbc.gridy = 2;
        signUpFrame.add(createButton, gbc);
        gbc.gridy = 3;
        signUpFrame.add(backButton, gbc);

        createButton.addActionListener(e -> {
            String username = userField.getText().trim();
            String password = new String(passField.getPassword()).trim();
            if (!username.isEmpty() && !password.isEmpty()) {
                tempUsers.put(username, password);
                JOptionPane.showMessageDialog(signUpFrame, "Account created successfully!");
                fadeOut(signUpFrame, OpenPg::openLoginWindow);
            } else {
                JOptionPane.showMessageDialog(signUpFrame, "Please fill all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        backButton.addActionListener(e -> fadeOut(signUpFrame, OpenPg::openLoginWindow));
        fadeIn(signUpFrame);
    }

    private static void styleRetroButton(JButton btn, Color color) {
        btn.setFont(new Font("Courier New", Font.BOLD, 16));
        btn.setBackground(Color.BLACK);
        btn.setForeground(color);
        btn.setBorder(BorderFactory.createLineBorder(color, 2));
        btn.setFocusPainted(false);
    }

    private static void fadeIn(JFrame frame) {
        Timer timer = new Timer(40, null);
        timer.addActionListener(new ActionListener() {
            float opacity = 0f;
            public void actionPerformed(ActionEvent e) {
                opacity += 0.05f;
                frame.setOpacity(Math.min(1f, opacity));
                if(opacity >= 1f) ((Timer)e.getSource()).stop();
            }
        });
        timer.start();
    }

    private static void fadeOut(JFrame frame, Runnable nextAction) {
        Timer timer = new Timer(40, null);
        timer.addActionListener(new ActionListener() {
            float opacity = 1f;
            public void actionPerformed(ActionEvent e) {
                opacity -= 0.05f;
                frame.setOpacity(Math.max(0f, opacity));
                if (opacity <= 0f) {
                    ((Timer)e.getSource()).stop();
                    frame.dispose();
                    if (nextAction != null) nextAction.run();
                }
            }
        });
        timer.start();
    }

    private static JPanel createClearableField(JTextField textField) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(30, 30, 30));
        panel.add(textField, BorderLayout.CENTER);

        JButton clearButton = new JButton("×");
        clearButton.setMargin(new Insets(1, 5, 1, 5));
        clearButton.setFocusable(false);
        clearButton.setFont(new Font("Courier New", Font.BOLD, 14));
        clearButton.setForeground(Color.RED);
        clearButton.setBackground(Color.BLACK);
        clearButton.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
        clearButton.setToolTipText("Clear");
        clearButton.addActionListener(e -> textField.setText(""));

        panel.add(clearButton, BorderLayout.EAST);
        return panel;
    }

    private static void openEventForm() {
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
        JLabel dateLabel = new JLabel("Date:");
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
            if (nameField.getText().trim().isEmpty() || venueField.getText().trim().isEmpty() ||
                    dateField.getText().trim().isEmpty() || collegeField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(eventFrame, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(eventFrame, "Event added successfully!");
                eventFrame.dispose();
            }
        });

        backButton.addActionListener(e -> eventFrame.dispose());
        eventFrame.setVisible(true);
    }
}

