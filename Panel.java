import javax.swing.JFrame;
import java.util.ArrayList;
public class Panel{
    public Panel(User user, ArrayList<Passenger> passengers){
        JFrame frame = new JFrame("UniRide");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new GUI(user, passengers, frame));
        frame.setSize(820, 760);
        frame.setResizable(false);
        frame.setVisible(true);
    }
}
