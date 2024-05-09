package com.expensecalculator.dto;

import com.expensecalculator.enums.Role;

public class User {
	public int userId;
	public String name;
	public String password;
	public int roleId;
	public String email;
	public String phoneNumber;
	
	public User(int userId, String name, int roleId) {
		this.userId = userId;
		this.name = name;
        this.roleId = roleId;
	}
	
	public User(String name, String email, String phoneNumber) {
		this.name = name;
        this.email = email;
        this.phoneNumber =phoneNumber;
	}
	
	public User(String name, String password, String email, String phoneNumber) {
		this.userId = -1;
		this.name = name;
        this.password = password;
        this.email = email;
        this.roleId = Role.GeneralUser.getRoleId();
        this.phoneNumber =phoneNumber;
	}
	
	public User(int userId, String name, String password, int roleId, String email, String phoneNumber) {
		this.userId = userId;
		this.name = name;
        this.password = password;
        this.email = email;
        this.roleId = roleId;
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

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
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
