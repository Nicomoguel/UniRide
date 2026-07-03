import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Font;
import java.awt.Color;
import java.util.HashMap;
import javax.swing.*;
public class LoginPage implements ActionListener{

    JFrame frame = new JFrame();
    JButton loginButton = new JButton("Login");
    JButton resetButton = new JButton("Reset");
    JTextField userIDField = new JTextField();
    JPasswordField userPasswordField = new JPasswordField();
    JLabel userIDLabel = new JLabel("userID:");
    JLabel userPasswordLabel = new JLabel("password:");
    JLabel messageLabel = new JLabel("");
    HashMap<String, String> loginInfo = new HashMap<String, String>();
    
    public LoginPage(HashMap<String, String> loginInfo){
        this.loginInfo = loginInfo;
        
        userIDLabel.setBounds(50,100,75,25);
        userPasswordLabel.setBounds(50,150,75,25);
        messageLabel.setBounds(125,250,250,35);
        messageLabel.setFont(new Font(null,Font.ITALIC,25));
        userIDField.setBounds(125,100,200,25);
        userPasswordField.setBounds(125,150,200,25);
        loginButton.setBounds(125,200,100,25);
        loginButton.addActionListener(this);
        loginButton.setFocusable(false);
        resetButton.setBounds(225,200,100,25);
        resetButton.addActionListener(this);
        resetButton.setFocusable(false);

        frame.add(userIDLabel);
        frame.add(userPasswordLabel);
        frame.add(messageLabel);
        frame.add(userIDField);
        frame.add(userPasswordField);
        frame.add(loginButton);
        frame.add(resetButton);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(420,420);
        frame.setLayout(null);
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
        }
    }
}
