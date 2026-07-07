import java.util.ArrayList;

/**
 * Interfaz que define las operaciones de persistencia de usuarios,
 * implementada por la clase encargada de guardar y cargar usuarios.
 * @author HoodCodeDepartment
 */
public interface UserRepository {
    /**
     * Guarda la lista de usuarios en la base de datos.
     * @param users Lista de usuarios a guardar
     */
    void saveUsers(ArrayList<User> users);

    /**
     * Carga los usuarios almacenados en la base de datos.
     * @return La lista de usuarios recuperados
     */
    ArrayList<User> loadUsers();

    /**
     * Obtiene la lista de pasajeros cargados en memoria.
     * @return Lista de objetos Passenger
     */
    ArrayList<Passenger> getPassengers();
}
