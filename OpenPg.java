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
	
	URL imgURL=OpenPg.class.getResource("/icon/icon.png");
	ImageIcon  myIcon = null;
	if (imgURL != null) {
        ImageIcon originalIcon = new ImageIcon(imgURL);
        int desiredWidth=300;
        int desiredHeight=300;
        
        Image originalImage = originalIcon.getImage();
        Image ScaledImage = originalImage.getScaledInstance(desiredWidth,desiredHeight,Image.SCALE_SMOOTH);
        myIcon = new ImageIcon(ScaledImage);
        
    } else {
        System.err.println("Couldn't find file: icons/icon.png");
        frame.add(new JLabel("Image not found at /icons/icon.png"));
        frame.setSize(300, 100);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        return;
    }
	
	JLabel imageLabel = new JLabel(myIcon);
    
    imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT); 
	
	JButton bookButton = new JButton("Book an Event");
	bookButton.setAlignmentX(Component.CENTER_ALIGNMENT);
	bookButton.setFont(new Font("Arial",Font.BOLD,16));
	bookButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
	bookButton.setMaximumSize(new Dimension(200, 40));
	
	JPanel panel = new JPanel();
	panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
	panel.setOpaque(false);
    panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
    
    panel.add(imageLabel);
    
    panel.add(Box.createRigidArea(new Dimension(0, 25)));
    
    panel.add(bookButton);
    
    bookButton.addActionListener(new ActionListener() {
    	
        public void actionPerformed(ActionEvent e) {
           
            ProjK bookingWindow = new ProjK();
            bookingWindow.setVisible(true);  

            
        }
    });
	
	frame.setSize(400,400);
	panel.add(imageLabel);
	frame.add(panel);
	frame.setIconImage(myIcon.getImage());
	frame.pack();
	frame.setMinimumSize(frame.getSize());
    frame.setLocationRelativeTo(null);
	frame.setVisible(true);
	}
}
