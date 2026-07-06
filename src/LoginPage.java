import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Font;
import java.awt.Color;
import java.util.HashMap;
import javax.swing.*;
import java.util.ArrayList;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
public class LoginPage extends JPanel implements ActionListener{
    private BufferedImage logo;
    private JFrame frame = new JFrame();
    private JButton loginButton = new JButton("Login");
    private JButton resetButton = new JButton("Reset");
    private JTextField userIDField = new JTextField();
    private JPasswordField userPasswordField = new JPasswordField();
    private JLabel userIDLabel = new JLabel("userID:");
    private JLabel userPasswordLabel = new JLabel("password:");
    private JLabel messageLabel = new JLabel("");
    private HashMap<String, String> loginInfo = new HashMap<String, String>();
    private DatabaseManager dManager = new DatabaseManager();
    private ArrayList<User> users = dManager.loadUsers();
    private ArrayList<Passenger> passengers = dManager.getPassengers();
    private Color color = new Color(255,255,255);
    
    
    
    public LoginPage(){
        this.loginInfo = loginInfo;

        userIDLabel.setBounds(255,285,80,25);
        userPasswordLabel.setBounds(255,335,80,25);
        messageLabel.setBounds(125,250,250,35);
        messageLabel.setFont(new Font(null,Font.ITALIC,25));
        userIDField.setBounds(345, 285, 200, 25);
        userPasswordField.setBounds(345,335,200,25);
        loginButton.setBounds(305,390,90,30);
        loginButton.addActionListener(this);
        loginButton.setFocusable(false);
        resetButton.setBounds(405,390,90,30);
        resetButton.addActionListener(this);
        resetButton.setFocusable(false);

        this.add(userIDLabel);
        this.add(userPasswordLabel);
        this.add(messageLabel);
        this.add(userIDField);
        this.add(userPasswordField);
        this.add(loginButton);
        this.add(resetButton);


        try{
            logo = ImageIO.read(new File("icon2.jpeg"));
        }
        catch(IOException ex){
            System.out.println("Couldn't read the image");
        }
        this.setLayout(null);
        this.setBounds(0,0,800,700);
        frame.add(this);
        frame.getContentPane().setBackground(color);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800,700);
        frame.setLayout(null);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource()==resetButton){
            userIDField.setText("");
            userPasswordField.setText("");
        }
        if(e.getSource()==loginButton){
            String userID = userIDField.getText();
            String userPassword = String.valueOf(userPasswordField.getPassword());
            boolean enc = false;
            for(User u : users){
                if(u.getStudentId().equals(userID)){
                    if(u.getPassword().equals(userPassword)){
                        enc = true;
                        messageLabel.setForeground(Color.green);
                        messageLabel.setText("Login successful");
                        frame.dispose();
                        Panel newPanel = new Panel(u, passengers);
                    }
                    else{
                        enc = true;
                        messageLabel.setForeground(Color.red);
                        messageLabel.setText("Wrong password");
                        break;
                    }
                }
            }
            if(!enc){
                messageLabel.setForeground(Color.red);
                messageLabel.setText("User not found");
            }


/*
            if(loginInfo.containsKey(userID)){
                if(loginInfo.get(userID).equals(userPassword)){
                    messageLabel.setForeground(Color.green);
                    messageLabel.setText("Login successful");
                    frame.dispose();
                    WelcomePage welcomePage = new WelcomePage(userID);
                }
                else{
                    messageLabel.setForeground(Color.red);
                    messageLabel.setText("Wrong password");
                
                }
            }
            else{
                messageLabel.setForeground(Color.red);
                messageLabel.setText("User not found");
        
            }
            */
        }
    }
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
            g.drawImage(logo, 300, 50,200,200, this);
    }
}
