import javax.swing.JFrame;
import java.util.ArrayList;
/**
 * Clase que inicializa el panel principal de la aplicacion, solo se inicializa si la validacion de usuario fue exitosa.
 */
public class Panel{

    /**
     * Constructor de la clase, agrega valores como el nombre de la ventana y tamaño
     * @param user El usuario que inició sesión
     * @param passengers Conjunto de pasajeros con los que se realizará el matchmaking
     */
    public Panel(User user, ArrayList<Passenger> passengers){
        JFrame frame = new JFrame("UniRide");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new GUI(user, passengers, frame));
        frame.setSize(820, 760);
        frame.setResizable(false);
        frame.setVisible(true);
    }
}
