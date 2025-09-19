package projk;

import javax.swing.*;
import java.awt.*;

public class ProjK {
    public static void main(String[] args) {
        
        JFrame frame = new JFrame("Event Mate"); 

        
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
        
        clickButton.setBounds(175, 160, 100, 30);
       
        frame.setLayout(null);

        
        frame.add(titleLabel);
        frame.add(collegeLabel);
        frame.add(collegeSearchBox);
        frame.add(eventLabel);
        frame.add(eventSearchBox);
        frame.add(clickButton); 

        
        frame.getContentPane().setBackground(new Color(220, 240, 220));

        
        frame.setSize(450, 250);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null); 
    }
}