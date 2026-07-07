/**
 * Clase abstracta que define la estructura y comportamiento común 
 * de los usuarios del sistema.
 * * @author HodeCodeDepartment
 */
public abstract class User {
    private String studentId;
    private String password;
    private short tolerance;
    protected String IDMEX;
    protected short age;
    protected int userPoints;
    protected Node source;
    protected Node destination;
    protected Schedule schedule;

    /** Inicializa un nuevo usuario con la información básica. */
    public User(String studentId, String password, String IDMEX, short age, short tolerance, Node source, Node destination, Schedule schedule) { // Node source, Node destination
        this.studentId = studentId;
        this.password = password;
        this.IDMEX = IDMEX;
        this.age = age;
        this.tolerance = tolerance;
        this.source = source;
        this.destination = destination;
        this.schedule = schedule;
        userPoints = 0;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getPassword() {
        return password;
    }

    public String getIDMEX() {
        return IDMEX;
    }

    public short getAge() {
        return age;
    }

    public short getTolerance() {
        return tolerance;
    }

    public int getUserPoints() {
        return userPoints;
    }

    public Node getSource(){
        return source;
    }
    
    public Node getDestination(){
        return destination;
    }

    public Schedule getSchedule(){
        return schedule;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setTolerance(short tolerance) {
        this.tolerance = tolerance;
    }

    public void setIDMEX(String IDMEX) {
        this.IDMEX = IDMEX;
    }

    public void setUserPoints(int points) {
        this.userPoints = points;
    }

    /** Valida las credenciales del usuario. */
    public boolean login(String studentId, String password) {
        return this.studentId.equals(studentId) && this.password.equals(password);
    }

    /** Incrementa los puntos de confiabilidad del usuario y retorna el total actualizado. */
    public int addReliabilityPoints(int points) {
        this.userPoints += points;
        return userPoints;
    }
    /** Indica si el usuario ha completado su proceso de verificación. */
    public abstract boolean isUserVerified();
    /** Retorna información sobre el tipo de usuario. */
    public abstract boolean whichUser();
}
