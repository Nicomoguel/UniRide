import java.util.List;
import java.util.ArrayList;

public class MatchMaker {
    private double maxDesviationMeters;

    public MatchMaker(double maxDesviationMeters) {
        this.maxDesviationMeters = maxDesviationMeters;
    }

    public MatchResult evaluateCompatibility(Driver driver, Passenger passenger) {
        Schedule ds = driver.getSchedule();
        Schedule ps = passenger.getSchedule();
        long minutesDifference = ds.calculateTimeDifference(ps);

        if(!checkSchedule(driver,passenger)) {
            return new MatchResult(passenger, 0.0, (int) minutesDifference, MatchStatus.REJECTED_SCHEDULE);
        }
        double distance = checkDistance(driver,passenger);
        if(distance > this.maxDesviationMeters) {
            return new MatchResult(passenger, distance, (int) minutesDifference, MatchStatus.REJECTED_DISTANCE);
        }

        return new MatchResult(passenger, distance, (int) minutesDifference, MatchStatus.MATCH);
    }

    public List<MatchResult> evaluateAll(Driver driver, List<Passenger> passengers) {
        ArrayList<MatchResult> list = new ArrayList<>();
        for(Passenger p : passengers) {
            list.add(evaluateCompatibility(driver,p));
        }
        return list;
    }

    private boolean checkSchedule(Driver driver, Passenger passenger) {
        Schedule ds = driver.getSchedule();
        Schedule ps = passenger.getSchedule();
        short t = passenger.getTolerance();
        return ds.isArrivalCompatible(ps,t);
    }

    private double checkDistance(Driver driver, Passenger passenger) {
        double minDistance = Double.MAX_VALUE;
        Node passengerNode = passenger.getSource();

        for (Node d : driver.getRoute()) {
            double dx = d.getX() - passengerNode.getX();
            double dy = d.getY() - passengerNode.getY();
            double distance = Math.sqrt(dx*dx + dy*dy);
            if (distance < minDistance) {
                minDistance = distance;
            }
        }
        return minDistance;
    }
}
