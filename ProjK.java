package projk;

import javax.swing.*;
import java.awt.*;


public class ProjK extends JFrame {

   
    public ProjK() {
        
        setTitle("Event Mate");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
        setSize(450, 300); 
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(220, 240, 220));
        setLayout(null); 

        
        JLabel titleLabel = new JLabel("Event Mate", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setBounds(0, 20, 450, 30);

        JLabel collegeLabel = new JLabel("College Name:");
        collegeLabel.setBounds(50, 80, 100, 25);
        JTextField collegeSearchBox = new JTextField(20);
        collegeSearchBox.setBounds(160, 80, 220, 25);

        JLabel eventLabel = new JLabel("Event Name:");
        eventLabel.setBounds(50, 120, 100, 25);
        JTextField eventSearchBox = new JTextField(20);
        eventSearchBox.setBounds(160, 120, 220, 25);

        JButton clickButton = new JButton("Click Here");
        clickButton.setBounds(175, 180, 100, 30); 

        
        add(titleLabel);
        add(collegeLabel);
        add(collegeSearchBox);
        add(eventLabel);
        add(eventSearchBox);
        add(clickButton);
    }
    
   
}

