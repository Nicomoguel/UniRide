import javax.swing.*;
public class Panel{
    public static void main(String[] s){
        JFrame frame = new JFrame("Prueba");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800,800);
        frame.add(new GUI());
        frame.pack();
        frame.setVisible(true);
    }
}
