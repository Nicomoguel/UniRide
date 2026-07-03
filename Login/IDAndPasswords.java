import java.util.HashMap;
public class IDAndPasswords{
    HashMap<String, String> loginData = new HashMap<String, String>();
    public IDAndPasswords(){
        loginData.put("nicomoguel", "761890");
        loginData.put("marianita", "090393");
        loginData.put("tilin", "67");
    }

    protected HashMap getLoginInfo(){
        return loginData;
    }
}
