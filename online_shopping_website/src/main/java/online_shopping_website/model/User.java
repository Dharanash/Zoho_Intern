package online_shopping_website.model;

import online_shopping_website.enums.Role;

public class User {
	public int userId;
	public String name;
	public String password;
	public Role role;
	public String email;
	public String phoneNumber;
	
	public User(String name, String password, String email, String phoneNumber) {
		this.userId = -1;
		this.name = name;
        this.password = password;
        this.email = email;
        this.role = Role.Customer;
        this.phoneNumber =phoneNumber;
	}
	
	public User(String name, String password, Role role, String email, String phoneNumber) {
		this.userId = -1;
		this.name = name;
        this.password = password;
        this.email = email;
        this.role = role;
        this.phoneNumber =phoneNumber;
	}
	
	public User(int userId, String name, String password, Role role, String email, String phoneNumber) {
		this.userId = userId;
		this.name = name;
        this.password = password;
        this.email = email;
        this.role = role;
        this.phoneNumber =phoneNumber;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}


	
}
