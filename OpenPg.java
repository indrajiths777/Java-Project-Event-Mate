package projk;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URI;
import java.awt.Desktop;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class ProjK extends JFrame {

    private JComboBox<String> collegeSearchBox; // editable for autocomplete
    private JComboBox<String> eventSearchBox;   // changed to JComboBox for autocomplete
    private ArrayList<String> collegeList;
    private ArrayList<String> eventList;

    public ProjK() {
        setTitle("Event Mate");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(null);
        setResizable(true);
        getContentPane().setBackground(new Color(20, 20, 20));

        // Load distinct names from events table
        loadCollegesFromDB();
        loadEventsFromDB();

        JLabel titleLabel = new JLabel("Event Mate", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Courier New", Font.BOLD, 42));
        titleLabel.setForeground(new Color(0, 255, 180));
        titleLabel.setBounds(0, 20, 850, 50);

        // College field
        JLabel collegeLabel = new JLabel("College Name:");
        collegeLabel.setFont(new Font("Courier New", Font.PLAIN, 22));
        collegeLabel.setForeground(new Color(0, 255, 255));
        collegeLabel.setBounds(100, 100, 180, 30);

        collegeSearchBox = new JComboBox<>(collegeList.toArray(new String[0]));
        collegeSearchBox.setEditable(true);
        JPanel collegePanel = createClearableField(collegeSearchBox);
        collegePanel.setBounds(320, 100, 350, 35);

        JTextField collegeEditor = (JTextField) collegeSearchBox.getEditor().getEditorComponent();
        collegeEditor.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                String text = collegeEditor.getText().toLowerCase();
                collegeSearchBox.removeAllItems();
                for (String college : collegeList) {
                    if (college.toLowerCase().contains(text)) {
                        collegeSearchBox.addItem(college);
                    }
                }
                collegeEditor.setText(text);
                collegeSearchBox.showPopup();
            }
        });

        // Event field
        JLabel eventLabel = new JLabel("Event Name:");
        eventLabel.setFont(new Font("Courier New", Font.PLAIN, 22));
        eventLabel.setForeground(new Color(0, 255, 255));
        eventLabel.setBounds(100, 160, 180, 30);

        eventSearchBox = new JComboBox<>(eventList.toArray(new String[0]));
        eventSearchBox.setEditable(true);
        JPanel eventPanel = createClearableField(eventSearchBox);
        eventPanel.setBounds(320, 160, 350, 35);

        JTextField eventEditor = (JTextField) eventSearchBox.getEditor().getEditorComponent();
        eventEditor.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                String text = eventEditor.getText().toLowerCase();
                eventSearchBox.removeAllItems();
                for (String event : eventList) {
                    if (event.toLowerCase().contains(text)) {
                        eventSearchBox.addItem(event);
                    }
                }
                eventEditor.setText(text);
                eventSearchBox.showPopup();
            }
        });

        JButton clickButton = new JButton("Click Here");
        clickButton.setFont(new Font("Courier New", Font.BOLD, 20));
        clickButton.setBackground(Color.BLACK);
        clickButton.setForeground(Color.GREEN);
        clickButton.setBorder(BorderFactory.createLineBorder(Color.GREEN, 2));
        clickButton.setBounds(320, 230, 200, 45);
        clickButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        clickButton.setFocusPainted(false);

        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Courier New", Font.BOLD, 20));
        backButton.setBackground(Color.BLACK);
        backButton.setForeground(Color.RED);
        backButton.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
        backButton.setBounds(20, 20, 120, 40);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.setFocusPainted(false);
        backButton.addActionListener(e -> dispose());

        add(titleLabel);
        add(collegeLabel);
        add(collegePanel);
        add(eventLabel);
        add(eventPanel);
        add(clickButton);
        add(backButton);

        clickButton.addActionListener(e -> searchEvent());
    }

    private void loadCollegesFromDB() {
        collegeList = new ArrayList<>();
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement pst = conn.prepareStatement("SELECT DISTINCT college_name FROM events");
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                collegeList.add(rs.getString("college_name"));
            }
            rs.close();
            pst.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load college names from database.");
        }
    }

    private void loadEventsFromDB() {
        eventList = new ArrayList<>();
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement pst = conn.prepareStatement("SELECT DISTINCT event_name FROM events");
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                eventList.add(rs.getString("event_name"));
            }
            rs.close();
            pst.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load event names from database.");
        }
    }

    private void searchEvent() {
        String collegeName = ((JTextField) collegeSearchBox.getEditor().getEditorComponent()).getText().trim();
        String eventName = ((JTextField) eventSearchBox.getEditor().getEditorComponent()).getText().trim();

        if (collegeName.isEmpty() || eventName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both college and event name.");
            return;
        }

        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT * FROM events WHERE college_name = ? AND event_name = ?"
            );
            ps.setString(1, collegeName);
            ps.setString(2, eventName);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String event = rs.getString("event_name");
                String venue = rs.getString("venue");
                java.sql.Date date = rs.getDate("event_date");

                showEventDetails(event, collegeName, venue, date.toString());
            } else {
                JOptionPane.showMessageDialog(this, "Sorry!!\nNo such events are currently available for this college.");
            }

            rs.close();
            ps.close();
            conn.close();

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching event from database.");
        }
    }

    private void showEventDetails(String event, String college, String venue, String date) {
        JFrame eventFrame = new JFrame(event + " Event");
        eventFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        eventFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        eventFrame.setLocationRelativeTo(this);
        eventFrame.setResizable(true);
        eventFrame.getContentPane().setBackground(new Color(20, 20, 20));

        JTextArea detailsArea = new JTextArea(
                "Event: " + event + "\n" +
                        "College: " + college + "\n" +
                        "Date: " + date + "\n" +
                        "Venue: " + venue + "\n\n" +
                        "Join us for this amazing event!"
        );
        detailsArea.setEditable(false);
        detailsArea.setLineWrap(true);
        detailsArea.setWrapStyleWord(true);
        detailsArea.setFont(new Font("Courier New", Font.PLAIN, 18));
        detailsArea.setForeground(new Color(255, 105, 180));
        detailsArea.setBackground(new Color(30, 30, 30));
        detailsArea.setAlignmentX(Component.CENTER_ALIGNMENT);
        detailsArea.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.PINK), "Event Details"));

        JButton registerButton = new JButton("<HTML><U>Register</U></HTML>");
        registerButton.setFont(new Font("Courier New", Font.BOLD, 22));
        registerButton.setForeground(Color.CYAN);
        registerButton.setBackground(Color.BLACK);
        registerButton.setBorderPainted(false);
        registerButton.setContentAreaFilled(false);
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(eventFrame, "Registration link clicked! Redirecting...");
            try {
                Desktop.getDesktop().browse(new URI("https://www.google.com"));
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(eventFrame, "Failed to open registration link.");
            }
        });

        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Courier New", Font.BOLD, 20));
        backButton.setForeground(Color.RED);
        backButton.setBackground(Color.BLACK);
        backButton.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.setFocusPainted(false);
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.addActionListener(e -> eventFrame.dispose());

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(20, 20, 20));

        panel.add(detailsArea);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(registerButton);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));
        panel.add(backButton);

        eventFrame.add(panel);
        eventFrame.setVisible(true);
    }

    private JPanel createClearableField(JComboBox<String> comboBox) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(30, 30, 30));

        JTextField editor = (JTextField) comboBox.getEditor().getEditorComponent();
        editor.setFont(new Font("Courier New", Font.PLAIN, 16));
        editor.setBackground(Color.BLACK);
        editor.setForeground(Color.GREEN);
        editor.setCaretColor(Color.GREEN);

        panel.add(comboBox, BorderLayout.CENTER);

        JButton clearButton = new JButton("×");
        clearButton.setMargin(new Insets(1, 10, 1, 10));
        clearButton.setFocusable(false);
        clearButton.setFont(new Font("Courier New", Font.BOLD, 16));
        clearButton.setForeground(Color.RED);
        clearButton.setBackground(Color.BLACK);
        clearButton.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
        clearButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        clearButton.setToolTipText("Clear");
        clearButton.addActionListener(e -> editor.setText(""));

        panel.add(clearButton, BorderLayout.EAST);
        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ProjK frame = new ProjK();
            frame.setVisible(true);
        });
    }
}
