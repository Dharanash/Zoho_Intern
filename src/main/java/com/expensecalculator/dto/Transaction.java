package com.expensecalculator.dto;

import java.sql.Timestamp;

public class Transaction {
	public int transactionId;
	public int userId;
	public double amount;
	public String note;
	public String datetime;
	public int categoryId;
	public int typeId;
	public String category;
	public int autoAdderStatus;
	
	public Transaction(int userId, double amount, String note, String datetime, int categoryId, int typeId, int autoAdderStatus) {
		this.userId=userId;
		this.amount = amount;
		this.note = note;
		this.categoryId = categoryId;
		this.datetime=datetime;
		this.typeId=typeId;
		this.autoAdderStatus=autoAdderStatus;
	}
	
	public Transaction(int transactionId, int userId, double amount, String note, String datetime, int categoryId,  int typeId) {
		this.userId=userId;
		this.transactionId=transactionId;
		this.amount = amount;
		this.note = note;
		this.datetime=datetime;
		this.categoryId = categoryId;
		this.typeId=typeId;
	}
	
	public Transaction(int transactionId, int userId, double amount, String note, String datetime, int categoryId,  int typeId,  int autoAdderStatus) {
		this.userId=userId;
		this.transactionId=transactionId;
		this.amount = amount;
		this.note = note;
		this.datetime=datetime;
		this.categoryId = categoryId;
		this.typeId=typeId;
		this.autoAdderStatus=autoAdderStatus;
	}
	
	public Transaction(int transactionId, int userId, double amount, String note, String datetime, int categoryId, int typeId, String category, int autoAdderStatus) {
		this.userId=userId;
		this.transactionId=transactionId;
		this.amount = amount;
		this.note = note;
		this.datetime=datetime;
		this.categoryId = categoryId;
		this.category=category;
		this.typeId=typeId;
		this.autoAdderStatus=autoAdderStatus;
	}
}
