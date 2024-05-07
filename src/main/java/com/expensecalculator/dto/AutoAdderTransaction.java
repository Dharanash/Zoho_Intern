package com.expensecalculator.dto;

import java.sql.Timestamp;

public class AutoAdderTransaction extends Transaction {
	public Timestamp nextAddDateTimestamp;

	public AutoAdderTransaction(int transactionId, int userId, double amount, String note, String datetime,
			int categoryId, int typeId, int autoAdderStatus, Timestamp timestamp) {
		super(transactionId, userId, amount, note, datetime, categoryId, typeId, autoAdderStatus);
		this.nextAddDateTimestamp=timestamp;
	}
	
}
