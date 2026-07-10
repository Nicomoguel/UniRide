/**
 * Representa a un usuario de tipo pasajero en el sistema.
 * @author HoodCodeDepartment
 */
public class Passenger extends User {
    /**
     * Crea una nueva instancia de pasajero heredando de {@link User}.
     * @param studentId Matrícula del pasajero
     * @param password Contraseña del pasajero
     * @param IDMEX Código de identificación
     * @param age Edad del pasajero
     * @param tolerance Cantidad de tiempo (min) que el pasajero está dispuesto a esperar
     * @param source Ubicación de origen
     * @param destination Ubicación de destino
     * @param schedule Horario de salida y llegada
     */
    public Passenger(String studentId, String password, String IDMEX, short age, short tolerance, Node source, Node destination, Schedule schedule) { // Node source, Node destination
        super(studentId, password, IDMEX, age, tolerance, source, destination, schedule);
    }

    /**
     * Verifica si el pasajero tiene un identificador (IDMEX) registrado.
     * @return {@code true} si el pasajero tiene IDMEX registrado
     */
    @Override
    public boolean isUserVerified() {
        return this.IDMEX != null;
    }

    /**
     * Metodo auxiliar para verificar que tipo de usuario es, usado en {@link User}.
     * @return {@code true} por tratarse de un Passenger
     */
    @Override
    public boolean whichUser(){
        return true;
    }
}
