import java.time.*;
import java.time.temporal.ChronoUnit;

/**
 * Gestiona el horario de llegada y salida de los usuarios.
 * * @author HodeCodeDepartment
 */
public class Schedule {
    private LocalTime arrival;
    private LocalTime departure;

    /** Inicializa el horario con objetos {@link LocalTime}. */
    public Schedule(LocalTime arrival, LocalTime departure) {
        this.arrival = arrival;
        this.departure = departure;
   }

   /** Inicializa el horario convirtiendo cadenas de texto a {@link LocalTime}. */
   public Schedule(String arrival, String departure) {
        this.arrival = LocalTime.parse(arrival);
        this.departure = LocalTime.parse(departure);
   }

    public LocalTime getArrival() {
        return arrival;
    }
    public LocalTime getDeparture() {
        return departure;
    }

    public void setArrival(LocalTime arrival) {
       this.arrival = arrival; 
    }
    public void setDeparture(LocalTime departure) {
        this.departure = departure;
    }

    /** Calcula la diferencia en minutos entre la llegada de este horario y el obtenido. */
    public long calculateTimeDifference(Schedule other) {
        LocalTime startTime = this.getArrival();
        LocalTime endTime = other.getArrival();
        return Math.abs(ChronoUnit.MINUTES.between(startTime,endTime));
    }

    /** Verifica si la llegada es compatible con otro horario según una tolerancia dada por el usuario. */
    public boolean isArrivalCompatible(Schedule other, short tolerance) {
      return this.calculateTimeDifference(other) <= tolerance;
    }
}
