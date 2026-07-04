import javax.swing.*;
public class WelcomePage{
    JFrame frame  = new JFrame();
    JLabel welcomeLabel = new JLabel("Main");
    public WelcomePage(String userID){
        welcomeLabel.setBounds(0,0,200,350);
        welcomeLabel.setText("Hello " + userID);
        frame.add(welcomeLabel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(420,420);
        frame.setLayout(null);
        frame.setVisible(true);
    }
}
