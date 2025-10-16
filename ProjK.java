package projk;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.net.URI;
import java.awt.Desktop;
import java.sql.*;
import java.util.ArrayList;

public class ProjK extends JFrame {

    private JComboBox<String> collegeSearchBox;
    private JComboBox<String> eventSearchBox;
    private ArrayList<String> collegeList;
    private ArrayList<String> eventList;

    public ProjK() {
        setTitle("EventMate - Book Event");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Split pane
        JSplitPane splitPane = new JSplitPane();
        splitPane.setDividerLocation(400);
        splitPane.setDividerSize(0);
        splitPane.setEnabled(false);

        // Left panel (icon + title)
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(new Color(25, 25, 30));
        leftPanel.setBorder(new EmptyBorder(40, 20, 40, 20));

        JLabel iconLabel = new JLabel("\uD83C\uDFE2"); // simple event icon
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 120));
        iconLabel.setForeground(new Color(0, 200, 255));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel("EventMate");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 48));
        titleLabel.setForeground(new Color(0, 200, 255));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("<html><center>Book Your College Events</center></html>");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        subtitleLabel.setForeground(new Color(200, 200, 200));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitleLabel.setBorder(new EmptyBorder(20,0,40,0));

        leftPanel.add(Box.createVerticalGlue());
        leftPanel.add(iconLabel);
        leftPanel.add(Box.createVerticalStrut(20));
        leftPanel.add(titleLabel);
        leftPanel.add(subtitleLabel);
        leftPanel.add(Box.createVerticalGlue());

        // Right panel (search + buttons)
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(new Color(40, 40, 50));
        rightPanel.setBorder(new EmptyBorder(20,20,20,20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15,15,15,15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Load data
        loadCollegesFromDB();
        loadEventsFromDB();

        JLabel collegeLabel = new JLabel("College Name:");
        collegeLabel.setForeground(Color.WHITE);
        collegeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        gbc.gridx = 0; gbc.gridy = 0;
        rightPanel.add(collegeLabel, gbc);

        collegeSearchBox = new JComboBox<>(collegeList.toArray(new String[0]));
        collegeSearchBox.setEditable(true);
        collegeSearchBox.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        collegeSearchBox.setBackground(Color.BLACK);
        collegeSearchBox.setForeground(Color.GREEN);
        gbc.gridx = 1; gbc.gridy = 0;
        rightPanel.add(collegeSearchBox, gbc);

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

        JLabel eventLabel = new JLabel("Event Name:");
        eventLabel.setForeground(Color.WHITE);
        eventLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        gbc.gridx = 0; gbc.gridy = 1;
        rightPanel.add(eventLabel, gbc);

        eventSearchBox = new JComboBox<>(eventList.toArray(new String[0]));
        eventSearchBox.setEditable(true);
        eventSearchBox.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        eventSearchBox.setBackground(Color.BLACK);
        eventSearchBox.setForeground(Color.GREEN);
        gbc.gridx = 1; gbc.gridy = 1;
        rightPanel.add(eventSearchBox, gbc);

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

        // Buttons
        JButton searchBtn = createStyledButton("Search Event", new Color(0,200,255));
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        rightPanel.add(searchBtn, gbc);

        JButton backBtn = createStyledButton("Back", new Color(255,90,90));
        gbc.gridy = 3;
        rightPanel.add(backBtn, gbc);

        // Actions
        searchBtn.addActionListener(e -> searchEvent());
        backBtn.addActionListener(e -> dispose());

        splitPane.setLeftComponent(leftPanel);
        splitPane.setRightComponent(rightPanel);
        add(splitPane);
    }

    private JButton createStyledButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 22));
        btn.setForeground(Color.WHITE);
        btn.setBackground(color);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createLineBorder(color.darker(), 2));
        btn.setOpaque(true);
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e){ btn.setBackground(color.brighter()); }
            public void mouseExited(MouseEvent e){ btn.setBackground(color); }
        });
        return btn;
    }

    private void loadCollegesFromDB() {
        collegeList = new ArrayList<>();
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement pst = conn.prepareStatement("SELECT DISTINCT college_name FROM events");
            ResultSet rs = pst.executeQuery()) {
            while(rs.next()) collegeList.add(rs.getString("college_name"));
        } catch(Exception e){ e.printStackTrace(); }
    }

    private void loadEventsFromDB() {
        eventList = new ArrayList<>();
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement pst = conn.prepareStatement("SELECT DISTINCT event_name FROM events");
            ResultSet rs = pst.executeQuery()) {
            while(rs.next()) eventList.add(rs.getString("event_name"));
        } catch(Exception e){ e.printStackTrace(); }
    }

    private void searchEvent() {
        String college = ((JTextField) collegeSearchBox.getEditor().getEditorComponent()).getText().trim();
        String event = ((JTextField) eventSearchBox.getEditor().getEditorComponent()).getText().trim();

        if(college.isEmpty() || event.isEmpty()){
            JOptionPane.showMessageDialog(this,"Please enter both fields");
            return;
        }

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT * FROM events WHERE college_name=? AND event_name=?")) {
            ps.setString(1,college);
            ps.setString(2,event);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                showEventDetails(rs.getString("event_name"), rs.getString("college_name"),
                        rs.getString("venue"), rs.getDate("event_date").toString());
            } else JOptionPane.showMessageDialog(this,"No such events found");
        } catch(Exception e){ e.printStackTrace(); }
    }

    private void showEventDetails(String event, String college, String venue, String date){
        JFrame frame = new JFrame(event + " - Details");
        frame.setSize(500,400);
        frame.setResizable(false);
        frame.setLocationRelativeTo(this);

        JPanel panel = new JPanel();
        panel.setBackground(new Color(25,25,30));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(20,20,20,20));

        JTextArea details = new JTextArea(
                "Event: "+event+"\nCollege: "+college+"\nVenue: "+venue+"\nDate: "+date
        );
        details.setEditable(false);
        details.setFont(new Font("Segoe UI",Font.PLAIN,18));
        details.setForeground(Color.CYAN);
        details.setBackground(new Color(25,25,30));
        details.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.CYAN),"Event Details"));

        JButton registerBtn = createStyledButton("Register", new Color(0,200,150));
        registerBtn.addActionListener(e -> {
            try{ Desktop.getDesktop().browse(new URI("https://www.google.com")); }
            catch(Exception ex){ JOptionPane.showMessageDialog(frame,"Failed to open link"); }
        });

        JButton backBtn = createStyledButton("Back", new Color(255,90,90));
        backBtn.addActionListener(e -> frame.dispose());

        panel.add(details);
        panel.add(Box.createRigidArea(new Dimension(0,20)));
        panel.add(registerBtn);
        panel.add(Box.createRigidArea(new Dimension(0,20)));
        panel.add(backBtn);

        frame.add(panel);
        frame.setVisible(true);
    }

    public static void main(String[] args){
        SwingUtilities.invokeLater(() -> {
            ProjK frame = new ProjK();
            frame.setVisible(true);
        });
    }
}
