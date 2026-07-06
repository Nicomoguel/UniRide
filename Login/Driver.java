public class Driver extends User {
    private boolean license;

    public Driver(String studentId, String password, String IDMEX, short age, short tolerance, boolean license) {
        super(studentId, password, IDMEX, age, tolerance);
        this.license = license;
    }

    public boolean isLicenseValid() {
        return license != false;
    }

    public void setLicense(boolean license) {
        this.license = license;
    }

    @Override
    public boolean isUserVerified() {
        return this.isLicenseValid() && this.IDMEX != null;
    }
}
