public class Passenger extends User {
    public Passenger(String studentId, String password, String IDMEX, short age, short tolerance) {
        super(studentId, password, IDMEX, age, tolerance);
    }

    @Override
    public boolean isUserVerified() {
        return this.IDMEX != null;
    }
}
