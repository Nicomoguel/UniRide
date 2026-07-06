import javax.swing.*;
public class Panel{
    public static void main(String[] s){
        JFrame frame = new JFrame("Prueba");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.add(new GUI());
        frame.pack();

        frame.setSize(570,725);
        frame.setVisible(true);
    }
}
