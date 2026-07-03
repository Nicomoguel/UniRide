import javax.swing.*;
public class Panel{
    public Panel(User user){
        JFrame frame = new JFrame("Prueba");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.add(new GUI(user));
        frame.pack();

        frame.setSize(570,725);
        frame.setVisible(true);
    }
}
