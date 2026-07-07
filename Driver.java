import java.util.LinkedList;
import java.util.List;
/**
 * Clase que representa a un usuario que puede ofrecer raid a otro en la aplicación, hereda de {@link User}.
 */
public class Driver extends User {
    private boolean license;
    private int desviation;
    private List<Node> route = new LinkedList<>();
    /**
     * Constructor de Driver, llama al constructor de la superclase que es {@link User} y pasa todos los datos necesarios y los atributos exclusivos de Driver los asigna
     * @param studentId Matrícula
     * @param password Constraseña
     * @param IDMEX Código de identificación
     * @param desviation Cantidad en metros que está dispuesto a desviarse para dar raid
     * @param age Edad
     * @param tolerance Cantidad en tiempo que el Driver esta dispuesto a esperar
     * @param license Valor que indica si el Driver tiene licencia o no
     * @param source Ubicación de origen
     * @param destination Ubicación de destino
     * @param schedule Horario de salida y llegada
     */
    public Driver(String studentId, String password, String IDMEX, int desviation, short age, short tolerance, boolean license, Node source, Node destination, Schedule schedule) { // Node source, Node destination
        super(studentId, password, IDMEX, age, tolerance, source, destination, schedule);
        this.license = license;
        this.desviation = desviation;
    }
    /**
     * Método público que determina si el usuario tiene licencia o no
     * @return {@code true} si el Driver tiene licencia válida
     */
    public boolean isLicenseValid() {
        return license != false;
    }
    /**
     * Setter que permite decidir si el usuario tiene licencia o no
     * @param license Valor que indica si el Driver tiene licencia
     */
    public void setLicense(boolean license) {
        this.license = license;
    }
    /**
     * Getter de la desviación que el Driver puede desplazarse
     * @return Desviación
     */
    public int getDesviation() {
        return desviation;
    }

    /**
     * Getter de la ruta del driver (Calculada por Dijkstra)
     * @return Ruta del driver
     */
    public List<Node> getRoute(){
        return this.route;
    }

    /**
     * Setter de la ruta del driver
     * @param route La ruta de nodos que seguirá el Driver
     */
    public void setRoute(List<Node> route){
        this.route = route;
    }

    /**
     * Método sobre escrito de User que nos dice si el usuario esta verificado o no, con IDMEX y la licencia
     * @return {@code true} si el Driver tiene licencia válida e IDMEX registrado
     */
    @Override
    public boolean isUserVerified() {
        return this.isLicenseValid() && this.IDMEX != null;
    }
    /**
     * Método que nos ayuda a diferenciar si el User es un Driver o Passenger
     * @return {@code false} por tratarse de un Driver
     */
    @Override
    public boolean whichUser(){
        return false;
    }

}
