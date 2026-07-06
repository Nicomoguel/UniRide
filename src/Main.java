public class Main{
    public static void main(String[] s){
       IDAndPasswords idandPasswords = new IDAndPasswords();
       LoginPage loginPage = new LoginPage(idandPasswords.getLoginInfo());
    }
}
