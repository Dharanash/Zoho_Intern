package online_shopping_system.POJO;

public class User {
	public int userId;
	public String name;
	public String password;
	public int role;
	public String email;
	public int phoneNumber;
	
	public User(String name, String password, int role, String email, int phoneNumber) {
		this.userId = -1;
		this.name = name;
        this.password = password;
        this.email = email;
        this.role = role;
        this.phoneNumber =phoneNumber;
	}
	
	public User(int userId, String name, String password, int role, String email, int phoneNumber) {
		this.userId = userId;
		this.name = name;
        this.password = password;
        this.email = email;
        this.role = role;
        this.phoneNumber =phoneNumber;
	}
	
}
