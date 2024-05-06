package com.expensecalculator.dto;

import java.sql.Timestamp;

public class Category {
	public int categoryId;
	public String category;
	public int transactionTypeId;
	public String transactionType;
	public Timestamp addDate;

	public Category(int categoryId, String category, Timestamp addDate) {
		this.categoryId = categoryId;
		this.category = category;
		this.addDate = addDate;
	}

	public Category(int categoryId, String category, int transactionTypeId, String transactionType, Timestamp addDate) {
		this.categoryId = categoryId;
		this.category = category;
		this.transactionTypeId = transactionTypeId;
		this.transactionType = transactionType;
		this.addDate = addDate;
	}
	
	
}
