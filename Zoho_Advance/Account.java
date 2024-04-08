
public class Account {
    String name;
    String userName;
    String password;
    String phoneNumber;
    String email;
    String role;
    String createdBy;
    String modefiedBy;
    public Account(String name, String userName, String password, String phoneNumber, String email, String role, String createdBy, String modefiedBy) {
        this.name = name;
        this.userName = userName;
        this.password = Utiliy.hashPassword(password);
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.role = role;
        this.createdBy= createdBy;
        this.modefiedBy = modefiedBy;
    }

    public Account(String role){
        this.role = role;
    }




  
}
