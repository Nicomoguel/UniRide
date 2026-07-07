/**
 * Representa a un usuario de tipo pasajero en el sistema.
 * * @author HodeCodeDepartment
 */
public class Passenger extends User {
    /** Crea una nueva instancia de pasajero heredando de {@link User}. */
    public Passenger(String studentId, String password, String IDMEX, short age, short tolerance, Node source, Node destination, Schedule schedule) { // Node source, Node destination
        super(studentId, password, IDMEX, age, tolerance, source, destination, schedule);
    }

    /** Verifica si el pasajero tiene un identificador (IDMEX) registrado para . */
    @Override
    public boolean isUserVerified() {
        return this.IDMEX != null;
    }

    /** Metodo auxiliar para verificar que tipo de usuario es, usado en {@link User}. */
    @Override
    public boolean whichUser(){
        return true;
    }
}
