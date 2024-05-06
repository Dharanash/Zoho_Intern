package com.expensecalculator.dto;

import java.sql.Timestamp;

public class Transaction {
	public int transactionId;
	public int userId;
	public double amount;
	public String note;
	public String date;
	public String time;
	public int categoryId;
	public int typeId;
	public String category;
	
	public Transaction(int userId, double amount, String note, int categoryId, int typeId) {
		this.userId=userId;
		this.amount = amount;
		this.note = note;
		this.categoryId = categoryId;
		this.typeId=typeId;
	}
	
	public Transaction(int userId, double amount, String note, String date, String time, int categoryId, int typeId) {
		this.userId=userId;
		this.amount = amount;
		this.note = note;
		this.date=date;
		this.time=time;
		this.categoryId = categoryId;
		this.typeId=typeId;
	}
	
	public Transaction(int transactionId, int userId, double amount, String note, String date, String time, int categoryId,  int typeId) {
		this.userId=userId;
		this.transactionId=transactionId;
		this.amount = amount;
		this.note = note;
		this.date=date;
		this.time=time;
		this.categoryId = categoryId;
	}
	
	public Transaction(int transactionId, int userId, double amount, String note, String date, String time, int categoryId, int typeId, String category) {
		this.userId=userId;
		this.transactionId=transactionId;
		this.amount = amount;
		this.note = note;
		this.date=date;
		this.time=time;
		this.categoryId = categoryId;
		this.category=category;
		this.typeId=typeId;
	}
}
