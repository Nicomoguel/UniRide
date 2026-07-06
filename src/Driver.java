import java.util.LinkedList;
import java.util.List;

public class Driver extends User {
    private boolean license;
    private List<Node> route = new LinkedList<>();
    public Driver(String studentId, String password, String IDMEX, short age, short tolerance, boolean license, Node source, Node destination, Schedule schedule) { // Node source, Node destination
        super(studentId, password, IDMEX, age, tolerance, source, destination, schedule);
        this.license = license;
    }

    public boolean isLicenseValid() {
        return license != false;
    }

    public void setLicense(boolean license) {
        this.license = license;
    }

    public void setRoute(Graph graph){ 
        Dijkstra.shortestPath(graph, this.source);
        this.route = this.destination.getShortestPath();
    }

    public List<Node> getRoute(){
        return this.route;
    }

    @Override
    public boolean isUserVerified() {
        return this.isLicenseValid() && this.IDMEX != null;
    }
    @Override
    public boolean whichUser(){
        return false;
    }
}

