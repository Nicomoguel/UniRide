import javax.swing.*;
public class Panel{
    public Panel(User user){
        JFrame frame = new JFrame("Prueba");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.add(new GUI(user, frame));
        frame.pack();

        frame.setSize(800,700);
        frame.setResizable(false);
        frame.setVisible(true);
    }
}
