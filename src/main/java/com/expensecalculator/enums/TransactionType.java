package com.expensecalculator.enums;

public enum TransactionType {
	Expense(1),
    Income(2);

    private final int typeId;

    TransactionType(int typeId) {
        this.typeId = typeId;
    }

    public int getTypeId() {
        return typeId;
    }
    
    public static TransactionType getTypeFromId(int id) {
        for (TransactionType type : TransactionType.values()) {
            if (type.getTypeId() == id) {
                return type;
            }
        }
        return null;
    }
}
