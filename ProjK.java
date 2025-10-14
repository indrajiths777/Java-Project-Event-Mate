package projk;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;
import java.net.URL;
import java.awt.event.*;
import java.net.URI;
import java.awt.Desktop;

public class ProjK extends JFrame {

    private JTextField collegeSearchBox;
    private JTextField eventSearchBox;

    public ProjK() {
        setTitle("Event Mate");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(850, 600);
        setLocationRelativeTo(null);
        setLayout(null);
        setResizable(true);

        // Title
        JLabel titleLabel = new JLabel("Event Mate", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setBounds(0, 20, 850, 50);

        // College label and search box
        JLabel collegeLabel = new JLabel("College Name:");
        collegeLabel.setFont(new Font("Arial", Font.PLAIN, 22));
        collegeLabel.setBounds(100, 100, 180, 30);

        collegeSearchBox = new JTextField(20);
        JPanel collegePanel = createClearableField(collegeSearchBox);
        collegePanel.setBounds(320, 100, 350, 35);

        // Event label and search box
        JLabel eventLabel = new JLabel("Event Name:");
        eventLabel.setFont(new Font("Arial", Font.PLAIN, 22));
        eventLabel.setBounds(100, 160, 180, 30);

        eventSearchBox = new JTextField(20);
        JPanel eventPanel = createClearableField(eventSearchBox);
        eventPanel.setBounds(320, 160, 350, 35);

        // Buttons
        Font buttonFont = new Font("Arial", Font.BOLD, 20);
        Dimension buttonSize = new Dimension(200, 45);

        JButton clickButton = new JButton("Click Here");
        clickButton.setFont(buttonFont);
        clickButton.setBounds(320, 230, 200, 45);
        clickButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JButton backButton = new JButton("Back");
        backButton.setFont(buttonFont);
        backButton.setBounds(20, 20, 120, 40);
        backButton.addActionListener(e -> dispose());

        getContentPane().setBackground(new Color(220, 240, 220));

        add(titleLabel);
        add(collegeLabel);
        add(collegePanel);
        add(eventLabel);
        add(eventPanel);
        add(clickButton);
        add(backButton);

        clickButton.addActionListener(e -> onClick());
    }

    private void onClick() {
        String collegeName = collegeSearchBox.getText().trim();
        String eventName = eventSearchBox.getText().trim();

        if (collegeName.equalsIgnoreCase("cep") && eventName.equalsIgnoreCase("hackathon")) {
            showHackathonImage();
        } else {
            JOptionPane.showMessageDialog(this, "Sorry!!\nNo such events are currently available on this college.");
        }
    }

    private void showHackathonImage() {
        URL imgURL = getClass().getResource("/hack/hackathon.png");
        if (imgURL == null) {
            JOptionPane.showMessageDialog(this, "hackathon.png image not found.");
            return;
        }

        ImageIcon originalIcon = new ImageIcon(imgURL);
        Image scaledImage = originalIcon.getImage().getScaledInstance(600, 400, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        JFrame imageFrame = new JFrame("Hackathon Event");
        imageFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        imageFrame.setSize(700, 750);
        imageFrame.setLocationRelativeTo(this);
        imageFrame.setResizable(true);

        JLabel imageLabel = new JLabel(scaledIcon);
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JTextArea detailsArea = new JTextArea(
            "Event: Hackathon\n" +
            "College: CEP\n" +
            "Date: 2025-10-15\n" +
            "Venue: Main Auditorium\n\n" +
            "Join us for an exciting 24-hour coding competition!\n" +
            "Prizes for top teams and networking opportunities."
        );
        detailsArea.setEditable(false);
        detailsArea.setLineWrap(true);
        detailsArea.setWrapStyleWord(true);
        detailsArea.setFont(new Font("Arial", Font.PLAIN, 18));
        detailsArea.setBorder(BorderFactory.createTitledBorder("Event Details"));
        detailsArea.setBackground(imageFrame.getBackground());

        JButton registerButton = new JButton("<HTML><U>Register for Hackathon</U></HTML>");
        registerButton.setFont(new Font("Arial", Font.BOLD, 22));
        registerButton.setForeground(Color.BLUE);
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerButton.setBorderPainted(false);
        registerButton.setContentAreaFilled(false);

        registerButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(imageFrame, "Registration link clicked! Redirecting...");
            try {
                Desktop.getDesktop().browse(new URI("https://www.google.com"));
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(imageFrame, "Failed to open registration link.");
            }
        });

        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.PLAIN, 20));
        backButton.addActionListener(e -> imageFrame.dispose());

        // Use BoxLayout for vertical alignment
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(imageLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(detailsArea);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(registerButton);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));
        panel.add(backButton);

        // Align center
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        detailsArea.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        imageFrame.add(panel);
        imageFrame.setVisible(true);
    }

    private JPanel createClearableField(JTextField textField) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(textField, BorderLayout.CENTER);

        JButton clearButton = new JButton("×");
        clearButton.setMargin(new Insets(1, 10, 1, 10));
        clearButton.setFocusable(false);
        clearButton.setFont(new Font("Arial", Font.BOLD, 16));
        clearButton.setForeground(Color.RED);
        clearButton.setToolTipText("Clear");

        clearButton.addActionListener(e -> textField.setText(""));

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

