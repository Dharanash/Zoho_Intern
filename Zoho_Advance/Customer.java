import java.util.HashMap;

public class Customer extends Account{
    public Customer(String name, String userName, String password, String phoneNumber, String email, String role,
            String createdBy, String modefiedBy) {
        super(name, userName, password, phoneNumber, email, role, createdBy, modefiedBy);
        cart = new HashMap<>();
        orders = new HashMap<>();
        wallet=0;
    }

    public Customer(String role){
        super(role);
        cart = new HashMap<>();
        orders = new HashMap<>();
        wallet=0;
    }
    int wallet;
    HashMap<String, Product> cart;
    HashMap<String, Order> orders;
    
    
}
