import java.time.*;
import java.time.temporal.ChronoUnit;

public class Schedule {
    private LocalTime arrival;
    private LocalTime departure;


    public Schedule(LocalTime arrival, LocalTime departure) {
        this.arrival = arrival;
        this.departure = departure;
   }

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

    public long calculateTimeDifference(Schedule other) {
        LocalTime startTime = this.getArrival();
        LocalTime endTime = other.getArrival();
        return Math.abs(ChronoUnit.MINUTES.between(startTime,endTime));
    }

    public boolean isArrivalCompatible(Schedule other, short tolerance) {
      return this.calculateTimeDifference(other) <= tolerance;
    }
}
