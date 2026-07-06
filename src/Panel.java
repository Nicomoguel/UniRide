import javax.swing.*;
import java.util.ArrayList;
public class Panel{
    public Panel(User user, ArrayList<Passenger> passengers){
        JFrame frame = new JFrame("Prueba");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.add(new GUI(user, passengers, frame));
        frame.pack();

        frame.setSize(800,700);
        frame.setResizable(false);
        frame.setVisible(true);
    }
}
