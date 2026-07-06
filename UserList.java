import java.util.ArrayList;

public class UserList {
    private ArrayList<User> users = new ArrayList<>();
    private DatabaseManager dbManager;
    
    public UserList() {
        this.dbManager = new DatabaseManager();
        this.users = dbManager.loadUsers();
    }
    public void registerUser(User user) {
        users.add(user);
        System.out.println("Successfully saved");
    }
}
