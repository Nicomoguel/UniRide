/**
 * Almacena el resultado y los detalles de la evaluación de coincidencia
 * entre un conductor y un pasajero.
 * @author HoodCodeDepartment
 */
public class MatchResult {
    /** Pasajero evaluado. */
    private Passenger passenger;
    /** Distancia mínima (en metros) entre el pasajero y la ruta del conductor. */
    private double distanceMeters;
    /** Diferencia de horario (en minutos) entre conductor y pasajero. */
    private int minutesDifference;
    /** Estado del resultado de la coincidencia. */
    private MatchStatus status;


    /**
     * Inicializa el resultado de la coincidencia con sus métricas y estado.
     * @param passenger Pasajero evaluado
     * @param distanceMeters Distancia mínima a la ruta del conductor (metros)
     * @param minutesDifference Diferencia de horario en minutos
     * @param status Estado de la coincidencia
     */
    public MatchResult(Passenger passenger, double distanceMeters, int minutesDifference, MatchStatus status) {
        this.passenger = passenger;
        this.distanceMeters = distanceMeters;
        this.minutesDifference = minutesDifference;
        this.status = status;
    }

    /**
     * Getter del pasajero evaluado.
     * @return El pasajero de este resultado
     */
    public Passenger getPassenger() {
        return passenger;
    }

    /**
     * Getter de la distancia a la ruta del conductor.
     * @return Distancia mínima en metros
     */
    public double getDistanceMeters() {
        return distanceMeters;
    }

    /**
     * Getter de la diferencia de horario.
     * @return Diferencia de horario en minutos
     */
    public int getMinutesDifference() {
        return minutesDifference;
    }

    /**
     * Getter del estado de la coincidencia.
     * @return El estado ({@link MatchStatus}) de este resultado
     */
    public MatchStatus status() {
        return status;
    }
}
