import java.time.*;
import java.time.temporal.ChronoUnit;

/**
 * Gestiona el horario de llegada y salida de los usuarios.
 * @author HoodCodeDepartment
 */
public class Schedule {
    private LocalTime arrival;
    private LocalTime departure;

    /**
     * Inicializa el horario con objetos {@link LocalTime}.
     * @param arrival Hora de llegada
     * @param departure Hora de salida
     */
    public Schedule(LocalTime arrival, LocalTime departure) {
        this.arrival = arrival;
        this.departure = departure;
   }

   /**
    * Inicializa el horario convirtiendo cadenas de texto a {@link LocalTime}.
    * @param arrival Hora de llegada en formato de texto (HH:mm)
    * @param departure Hora de salida en formato de texto (HH:mm)
    */
   public Schedule(String arrival, String departure) {
        this.arrival = LocalTime.parse(arrival);
        this.departure = LocalTime.parse(departure);
   }

    /**
     * Getter de la hora de llegada.
     * @return La hora de llegada
     */
    public LocalTime getArrival() {
        return arrival;
    }
    /**
     * Getter de la hora de salida.
     * @return La hora de salida
     */
    public LocalTime getDeparture() {
        return departure;
    }

    /**
     * Setter de la hora de llegada.
     * @param arrival La nueva hora de llegada
     */
    public void setArrival(LocalTime arrival) {
       this.arrival = arrival;
    }
    /**
     * Setter de la hora de salida.
     * @param departure La nueva hora de salida
     */
    public void setDeparture(LocalTime departure) {
        this.departure = departure;
    }

    /**
     * Calcula la diferencia en minutos entre la llegada de este horario y el obtenido.
     * @param other El otro horario a comparar
     * @return La diferencia absoluta en minutos entre ambas horas de llegada
     */
    public long calculateTimeDifference(Schedule other) {
        LocalTime startTime = this.getArrival();
        LocalTime endTime = other.getArrival();
        return Math.abs(ChronoUnit.MINUTES.between(startTime,endTime));
    }

    /**
     * Verifica si la llegada es compatible con otro horario según una tolerancia dada por el usuario.
     * @param other El otro horario a comparar
     * @param tolerance Tolerancia máxima permitida en minutos
     * @return {@code true} si la diferencia de horario está dentro de la tolerancia
     */
    public boolean isArrivalCompatible(Schedule other, short tolerance) {
      return this.calculateTimeDifference(other) <= tolerance;
    }
}
