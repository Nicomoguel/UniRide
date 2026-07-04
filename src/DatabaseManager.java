import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.nio.file.FileSystems;

public class DatabaseManager {
    private final Path path = Paths.get("usuarios.txt");

    public void saveUsers(ArrayList<User> users) {
        try(BufferedWriter writer = Files.newBufferedWriter(path, StandardOpenOption.CREATE,StandardOpenOption.TRUNCATE_EXISTING)) {
            for(User u : users) {
                Node source = u.getSource();
                Node destination = u.getDestination();
                writer.write(u.getClass().getSimpleName() + "|" + u.getStudentId() + "|" + u.getPassword() + "|" + u.getIDMEX() + "|" + u.getAge() + "|" + u.getTolerance() + "|" + u.getUserPoints() + "|" + (u instanceof Driver ? ((Driver) u).isLicenseValid() : "false") + "|" + source.getX() + "|" + source.getY() + "|" + destination.getX() + "|" + destination.getY()); // + u.getSource() + u.getDestination()             
                writer.newLine();
            }
        }
        catch(IOException ex) {
           System.out.println(ex.getMessage()); 
        }
    }
    public ArrayList<User> loadUsers() {
        ArrayList<User> loadedUsers = new ArrayList<>(); //Lista temporal
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
                    // leemos ambos x,y
                    int x1 = Integer.parseInt(data[8]);
                    int y1 = Integer.parseInt(data[9]);
                    int x2 = Integer.parseInt(data[10]);
                    int y2 = Integer.parseInt(data[11]);
                    Driver driver = new Driver(id, pass, idmex, age, tolerance, license, new Node("src", x1, y1), new Node("dest", x2, y2));
                    driver.addReliabilityPoints(points);
                    loadedUsers.add(driver);

                } else if (userType.equals("Passenger")) {
                    String id = data[1];
                    String pass = data[2];
                    String idmex = data[3];
                    short age = Short.parseShort(data[4]);
                    short tolerance = Short.parseShort(data[5]);
                    int points = Integer.parseInt(data[6]);
                    int x1 = Integer.parseInt(data[7]);
                    int y1 = Integer.parseInt(data[8]);
                    int x2 = Integer.parseInt(data[9]);
                    int y2 = Integer.parseInt(data[10]);
                    Passenger passenger = new Passenger(id, pass, idmex, age, tolerance, new Node("src", x1, y1), new Node("dest", x2, y2));
                    passenger.addReliabilityPoints(points);
                    loadedUsers.add(passenger);
                }
            }
            System.out.println("Users successfully loaded from the file");
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error processing the database file: " + e.getMessage());
        }

        return loadedUsers;
    }
}
