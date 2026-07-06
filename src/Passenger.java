public class Passenger extends User {
    public Passenger(String studentId, String password, String IDMEX, short age, short tolerance, Node source, Node destination, Schedule schedule) { // Node source, Node destination
        super(studentId, password, IDMEX, age, tolerance, source, destination, schedule);
    }

    @Override
    public boolean isUserVerified() {
        return this.IDMEX != null;
    }
    @Override
    public boolean whichUser(){
        return true;
    }
}
