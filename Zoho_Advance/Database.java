import java.util.HashMap;

public class Database {
    HashMap<String, Product> inventory;
    HashMap<String,  Account> accounts;
    public Database() {
        this.inventory = new HashMap<>();
        this.accounts = new HashMap<>();
        addAdmin();
    }

    public void addAdmin(){
        accounts.put("admin", new Account("admin", "admin", "admin", "123", "admin@gamil.com", "admin", "admin", "admin"));
    }

    
}
