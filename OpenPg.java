package projk;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

import org.kordamp.ikonli.swing.FontIcon;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;

public class OpenPg {

    private static JFrame mainFrame;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(OpenPg::createMainWindow);
    }

    
    private static void createMainWindow() {
        mainFrame = new JFrame("EventMate");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(900, 600);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setResizable(true);

        // Main split panel
        JSplitPane splitPane = new JSplitPane();
        splitPane.setDividerLocation(350);
        splitPane.setDividerSize(0);
        splitPane.setEnabled(false);

        
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(new Color(25, 25, 30));
        leftPanel.setBorder(new EmptyBorder(40, 20, 40, 20));

        JLabel iconLabel = new JLabel(FontIcon.of(FontAwesomeSolid.CALENDAR_ALT, 120, new Color(0, 200, 255)));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = new JLabel("EventMate");
        title.setFont(new Font("Segoe UI", Font.BOLD, 48));
        title.setForeground(new Color(0, 200, 255));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("<html><center>Your Gateway to College Events</center></html>");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        subtitle.setForeground(new Color(200, 200, 200));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitle.setBorder(new EmptyBorder(20,0,40,0));

        leftPanel.add(Box.createVerticalGlue());
        leftPanel.add(iconLabel);
        leftPanel.add(Box.createVerticalStrut(20));
        leftPanel.add(title);
        leftPanel.add(subtitle);
        leftPanel.add(Box.createVerticalGlue());

       
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new GridBagLayout());
        rightPanel.setBackground(new Color(40, 40, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JButton bookButton = createStyledButton("Book an Event", new Color(0, 150, 255), FontAwesomeSolid.BOOK);
        JButton addButton = createStyledButton("Add an Event", new Color(0, 200, 150), FontAwesomeSolid.PLUS);
        JButton editButton = createStyledButton("Edit Events", new Color(255, 90, 90), FontAwesomeSolid.EDIT);

        gbc.gridx = 0; gbc.gridy = 0;
        rightPanel.add(bookButton, gbc);
        gbc.gridy = 1;
        rightPanel.add(addButton, gbc);
        gbc.gridy = 2;
        rightPanel.add(editButton, gbc);

        gbc.gridy = 3;
        gbc.insets = new Insets(60,20,20,20);
        JLabel footer = new JLabel("© 2025 EventMate");
        footer.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        footer.setForeground(new Color(150, 150, 150));
        rightPanel.add(footer, gbc);

        
        splitPane.setLeftComponent(leftPanel);
        splitPane.setRightComponent(rightPanel);
        mainFrame.add(splitPane);
        mainFrame.setVisible(true);

        
        bookButton.addActionListener(e -> {
            ProjK bookingWindow = new ProjK();
            bookingWindow.setExtendedState(JFrame.MAXIMIZED_BOTH);
            bookingWindow.setVisible(true);
        });
        addButton.addActionListener(e -> openLoginSignupForm(false));
        editButton.addActionListener(e -> openLoginSignupForm(true));
    }

    
    private static JButton createStyledButton(String text, Color accent, FontAwesomeSolid icon) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 22));
        btn.setForeground(Color.WHITE);
        btn.setBackground(accent);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(250, 60));
        btn.setBorder(BorderFactory.createLineBorder(accent.darker(), 2));
        btn.setOpaque(true);

        
        btn.setIcon(FontIcon.of(icon, 24, Color.WHITE));
        btn.setIconTextGap(15); 

        btn.addMouseListener(new MouseAdapter() {
            
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(accent.brighter());
                btn.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
            }

            
            public void mouseExited(MouseEvent e) {
                btn.setBackground(accent);
                btn.setBorder(BorderFactory.createLineBorder(accent.darker(), 2));
            }

            
            public void mousePressed(MouseEvent e) {
                btn.setBackground(accent.darker());
            }

            
            public void mouseReleased(MouseEvent e) {
                btn.setBackground(accent.brighter());
            }
        });

        return btn;
    }

    
    private static void openLoginSignupForm(boolean isEditMode) {
        JFrame frame = new JFrame("Login / Sign Up");
        frame.setSize(420,340);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(new Color(20,20,30,200));
        panel.setBorder(new EmptyBorder(20,20,20,20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12,12,12,12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel userLabel = new JLabel("Username:"); userLabel.setForeground(Color.WHITE);
        JTextField userField = new JTextField(20);
        JLabel passLabel = new JLabel("Password:"); passLabel.setForeground(Color.WHITE);
        JPasswordField passField = new JPasswordField(20);

        JButton loginBtn = createStyledButton("Login", new Color(0,200,255), FontAwesomeSolid.SIGN_IN_ALT);
        JButton signupBtn = createStyledButton("Sign Up", new Color(0,255,150), FontAwesomeSolid.USER_PLUS);

        gbc.gridx=0; gbc.gridy=0; panel.add(userLabel,gbc);
        gbc.gridx=1; panel.add(userField,gbc);
        gbc.gridx=0; gbc.gridy=1; panel.add(passLabel,gbc);
        gbc.gridx=1; panel.add(passField,gbc);
        gbc.gridx=0; gbc.gridy=2; gbc.gridwidth=2; panel.add(loginBtn,gbc);
        gbc.gridy=3;
        panel.add(signupBtn,gbc);

        frame.add(panel);
        frame.setVisible(true);

        loginBtn.addActionListener(e -> handleLogin(userField, passField, frame, isEditMode));
        signupBtn.addActionListener(e -> handleSignup(userField, passField, frame));
    }

    private static void handleLogin(JTextField u, JPasswordField p, JFrame frame, boolean editMode) {
        String username = u.getText().trim();
        String password = new String(p.getPassword());
        if(username.isEmpty()||password.isEmpty()) {
            JOptionPane.showMessageDialog(frame,"Enter both fields","Error",JOptionPane.ERROR_MESSAGE); return;
        }
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE username=? AND password=?")) {
            ps.setString(1,username); ps.setString(2,password);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                JOptionPane.showMessageDialog(frame,"Login successful!");
                frame.dispose();
                if(editMode) openEventEditor(); else openEventForm();
            } else { JOptionPane.showMessageDialog(frame,"Invalid credentials","Error",JOptionPane.ERROR_MESSAGE); }
        } catch(Exception ex){ ex.printStackTrace(); JOptionPane.showMessageDialog(frame,"Database error"); }
    }

    private static void handleSignup(JTextField u, JPasswordField p, JFrame frame) {
        String username = u.getText().trim();
        String password = new String(p.getPassword());
        if(username.isEmpty()||password.isEmpty()){ JOptionPane.showMessageDialog(frame,"Enter both fields","Error",JOptionPane.ERROR_MESSAGE); return; }
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement("INSERT INTO users(username,password) VALUES(?,?)")) {
            ps.setString(1,username); ps.setString(2,password);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(frame,"Sign up successful! You can now login.");
        } catch(Exception ex){ ex.printStackTrace(); JOptionPane.showMessageDialog(frame,"Sign up failed (username may exist)"); }
    }

    
    private static void openEventForm() {
        JFrame frame = new JFrame("Add Event");
        frame.setSize(520,500);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(20,20,30,200));
        panel.setBorder(new EmptyBorder(20,20,20,20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12,12,12,12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel nLabel = new JLabel("Event Name:"); nLabel.setForeground(Color.WHITE);
        JTextField nField = new JTextField(20);
        JLabel vLabel = new JLabel("Venue:"); vLabel.setForeground(Color.WHITE);
        JTextField vField = new JTextField(20);
        JLabel dLabel = new JLabel("Date (YYYY-MM-DD):"); dLabel.setForeground(Color.WHITE);
        JTextField dField = new JTextField(20);
        JLabel cLabel = new JLabel("College:"); cLabel.setForeground(Color.WHITE);
        JTextField cField = new JTextField(20);

        JButton submitBtn = createStyledButton("Submit", new Color(0,255,150), FontAwesomeSolid.CHECK);
        JButton backBtn = createStyledButton("Back", new Color(255,80,80), FontAwesomeSolid.ARROW_LEFT);

        gbc.gridx=0; gbc.gridy=0; panel.add(nLabel,gbc); gbc.gridx=1; panel.add(nField,gbc);
        gbc.gridx=0; gbc.gridy=1; panel.add(vLabel,gbc); gbc.gridx=1; panel.add(vField,gbc);
        gbc.gridx=0; gbc.gridy=2; panel.add(dLabel,gbc); gbc.gridx=1; panel.add(dField,gbc);
        gbc.gridx=0; gbc.gridy=3; panel.add(cLabel,gbc); gbc.gridx=1; panel.add(cField,gbc);
        gbc.gridx=0; gbc.gridy=4; gbc.gridwidth=2; panel.add(submitBtn,gbc); gbc.gridy=5; panel.add(backBtn,gbc);

        frame.add(panel); frame.setVisible(true);

        submitBtn.addActionListener(e->{
            String n=nField.getText().trim(),v=vField.getText().trim(),d=dField.getText().trim(),c=cField.getText().trim();
            if(n.isEmpty()||v.isEmpty()||d.isEmpty()||c.isEmpty()){ JOptionPane.showMessageDialog(frame,"Fill all fields","Error",JOptionPane.ERROR_MESSAGE); return; }
            try(Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement("INSERT INTO events(event_name,venue,event_date,college_name) VALUES(?,?,?,?)")) {
                ps.setString(1,n); ps.setString(2,v); ps.setString(3,d); ps.setString(4,c);
                ps.executeUpdate(); JOptionPane.showMessageDialog(frame,"Event added!"); frame.dispose();
            } catch(Exception ex){ ex.printStackTrace(); JOptionPane.showMessageDialog(frame,"Database error"); }
        });

        backBtn.addActionListener(e->frame.dispose());
    }

    
    private static void openEventEditor() {
        JFrame frame = new JFrame("Edit/Delete Events");
        frame.setSize(750,450);
        frame.setResizable(true);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> eventList = new JList<>(listModel);
        eventList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        eventList.setFont(new Font("Segoe UI",Font.PLAIN,14));

        JButton editBtn = createStyledButton("Edit Selected", new Color(0,200,150), FontAwesomeSolid.EDIT);
        JButton deleteBtn = createStyledButton("Delete Selected", new Color(255,90,90), FontAwesomeSolid.TRASH);
        JButton refreshBtn = createStyledButton("Refresh", new Color(0,150,255), FontAwesomeSolid.ROTATE);

        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(new Color(30,30,40));
        btnPanel.add(editBtn); btnPanel.add(deleteBtn); btnPanel.add(refreshBtn);

        frame.add(new JScrollPane(eventList), BorderLayout.CENTER);
        frame.add(btnPanel, BorderLayout.SOUTH);

        Runnable loadEvents = () -> {
            listModel.clear();
            try(Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement("SELECT id,event_name,venue,event_date,college_name FROM events");
                ResultSet rs = ps.executeQuery()) {
                while(rs.next()) {
                    String line = rs.getInt("id")+" | "+rs.getString("event_name")+" | "+
                            rs.getString("venue")+" | "+rs.getString("event_date")+" | "+
                            rs.getString("college_name");
                    listModel.addElement(line);
                }
            } catch(Exception ex){ ex.printStackTrace(); }
        };
        loadEvents.run();

        deleteBtn.addActionListener(e->{
            String selected=eventList.getSelectedValue();
            if(selected==null){ JOptionPane.showMessageDialog(frame,"Select an event"); return; }
            int id=Integer.parseInt(selected.split(" \\| ")[0]);
            try(Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement("DELETE FROM events WHERE id=?")) {
                ps.setInt(1,id); ps.executeUpdate(); JOptionPane.showMessageDialog(frame,"Deleted!");
                loadEvents.run();
            } catch(Exception ex){ ex.printStackTrace(); }
        });

        editBtn.addActionListener(e->{
            String selected=eventList.getSelectedValue();
            if(selected==null){ JOptionPane.showMessageDialog(frame,"Select an event"); return; }
            String[] parts=selected.split(" \\| ");
            int id=Integer.parseInt(parts[0]);
            JTextField nameF=new JTextField(parts[1].trim());
            JTextField venueF=new JTextField(parts[2].trim());
            JTextField dateF=new JTextField(parts[3].trim());
            JTextField collegeF=new JTextField(parts[4].trim());

            JPanel panel=new JPanel(new GridLayout(0,1));
            panel.add(new JLabel("Event Name:")); panel.add(nameF);
            panel.add(new JLabel("Venue:")); panel.add(venueF);
            panel.add(new JLabel("Date:")); panel.add(dateF);
            panel.add(new JLabel("College:")); panel.add(collegeF);

            int res = JOptionPane.showConfirmDialog(frame,panel,"Edit Event",JOptionPane.OK_CANCEL_OPTION);
            if(res==JOptionPane.OK_OPTION){
                try(Connection conn = DBConnection.getConnection();
                    PreparedStatement ps = conn.prepareStatement(
                            "UPDATE events SET event_name=?,venue=?,event_date=?,college_name=? WHERE id=?")) {
                    ps.setString(1,nameF.getText().trim());
                    ps.setString(2,venueF.getText().trim());
                    ps.setString(3,dateF.getText().trim());
                    ps.setString(4,collegeF.getText().trim());
                    ps.setInt(5,id); ps.executeUpdate();
                    JOptionPane.showMessageDialog(frame,"Updated!");
                    loadEvents.run();
                } catch(Exception ex){ ex.printStackTrace(); }
            }
        });

        refreshBtn.addActionListener(e->loadEvents.run());
        frame.setVisible(true);
    }
}
