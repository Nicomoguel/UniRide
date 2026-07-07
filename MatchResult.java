/**
 * Almacena el resultado y los detalles de la evaluación de coincidencia 
 * entre un conductor y un pasajero.
 * * @author HodeCodeDepartment
 */
public class MatchResult {
    private Passenger passenger;
    private double distanceMeters;
    private int minutesDifference;
    private MatchStatus status;


    /** Inicializa el resultado de la coincidencia con sus métricas y estado. */
    public MatchResult(Passenger passenger, double distanceMeters, int minutesDifference, MatchStatus status) {
        this.passenger = passenger;
        this.distanceMeters = distanceMeters;
        this.minutesDifference = minutesDifference;
        this.status = status;
    }

    public Passenger getPassenger() {
        return passenger;
    }

    public double getDistanceMeters() {
        return distanceMeters;
    }

    public int getMinutesDifference() {
        return minutesDifference;
    }

    public MatchStatus status() {
        return status;
    }
}
