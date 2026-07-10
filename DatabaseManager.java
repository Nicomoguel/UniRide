import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.nio.file.FileSystems;
import java.time.*;

/**
 * Clase encargada de la serialización y deserialización de la clase User y 
 * cada uno de sus atributos según su subclase.
 * @author HoodCodeDepartment
 */
public class DatabaseManager implements UserRepository {
    /** Ruta del archivo de texto utilizado como base de datos. */
    private final Path path = Paths.get("usuarios.txt");
    /** Lista interna para almacenar los pasajeros recuperados. */
    private ArrayList<Passenger> passengers = new ArrayList<>();
    /**
     * Guarda la lista de usuarios en el archivo de texto, separando los
     * atributos de cada subclase separados por '|'.
     * @param users Lista de usuarios a guardar en la base de datos
     */
    public void saveUsers(ArrayList<User> users) {
        try(BufferedWriter writer = Files.newBufferedWriter(path, StandardOpenOption.CREATE,StandardOpenOption.TRUNCATE_EXISTING)) {
            for(User u : users) {
                Node source = u.getSource();
                Node destination = u.getDestination();
                Schedule schedule = u.getSchedule();
                writer.write(u.getClass().getSimpleName() + "|" + u.getStudentId() + "|" + u.getPassword() + "|" + u.getIDMEX() + "|" + u.getAge() + "|" + u.getTolerance() + "|" + u.getUserPoints() + "|" + (u instanceof Driver ? ((Driver) u).isLicenseValid() : "false") + "|" + source.getX() + "|" + source.getY() + "|" + destination.getX() + "|" + destination.getY() + "|" + schedule.getArrival().toString() + "|" + schedule.getDeparture().toString() + "|" + (u instanceof Driver ? ((Driver) u).getDesviation() : "0"));
                writer.newLine();
            }
        }
        catch(IOException ex) {
           System.out.println(ex.getMessage()); 
        }
    }

    /**
     * Lee el archivo de base de datos e instancia los objetos correspondientes 
     * ({@code Driver} o {@code Passenger}) con su información.
     * @return La lista de usuarios recuperados de la base de datos.
     */
    public ArrayList<User> loadUsers() {
        ArrayList<User> loadedUsers = new ArrayList<>();
        if (!Files.exists(path)) {
            System.out.println("The file does not exist. Starting with an empty database...");
            return loadedUsers; 
        }

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] data = line.split("\\|");
                String userType = data[0];

                if (userType.equals("Driver")) {
                    String id = data[1];
                    String pass = data[2];
                    String idmex = data[3];
                    short age = Short.parseShort(data[4]);
                    short tolerance = Short.parseShort(data[5]);
                    int points = Integer.parseInt(data[6]);
                    boolean license = Boolean.parseBoolean(data[7]);
                    int x1 = Integer.parseInt(data[8]);
                    int y1 = Integer.parseInt(data[9]);
                    int x2 = Integer.parseInt(data[10]);
                    int y2 = Integer.parseInt(data[11]);
                    LocalTime arrival = LocalTime.parse(data[12]);
                    LocalTime departure = LocalTime.parse(data[13]);
                    int desviation = (data.length > 14) ? Integer.parseInt(data[14]) : 0;

                    Driver driver = new Driver(id, pass, idmex, desviation, age, tolerance, license, new Node("src", x1, y1), new Node("dest", x2, y2), new Schedule(arrival, departure));
                    driver.addReliabilityPoints(points);
                    loadedUsers.add(driver);

                } else if (userType.equals("Passenger")) {
                    String id = data[1];
                    String pass = data[2];
                    String idmex = data[3];
                    short age = Short.parseShort(data[4]);
                    short tolerance = Short.parseShort(data[5]);
                    int points = Integer.parseInt(data[6]);
                    int x1 = Integer.parseInt(data[8]);
                    int y1 = Integer.parseInt(data[9]);
                    int x2 = Integer.parseInt(data[10]);
                    int y2 = Integer.parseInt(data[11]);
                    LocalTime arrival = LocalTime.parse(data[12]);
                    LocalTime departure = LocalTime.parse(data[13]);

                    Passenger passenger = new Passenger(id, pass, idmex, age, tolerance, new Node("src", x1, y1), new Node("dest", x2, y2), new Schedule(arrival, departure));
                    
                    passenger.addReliabilityPoints(points);
                    passengers.add(passenger);
                    loadedUsers.add(passenger);
                }
            }
            System.out.println("Users successfully loaded from the file");
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error processing the database file: " + e.getMessage());
        }

        return loadedUsers;
    }

    /**
     * Obtiene la lista de pasajeros que han sido cargados en memoria.
     * @return Lista de objetos {@code Passenger}.
     */
    public ArrayList<Passenger> getPassengers(){
        return passengers;
    }

}
