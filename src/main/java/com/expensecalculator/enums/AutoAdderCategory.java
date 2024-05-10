package com.expensecalculator.enums;

public enum AutoAdderCategory {
	Day(1), Week(2), Month(3);

	private final int categoryId;

	AutoAdderCategory(int categoryId) {
	        this.categoryId = categoryId;
	    }

	public int getCategoryId() {
		return categoryId;
	}

	public static AutoAdderCategory getRoleFromId(int id) {
		for (AutoAdderCategory category : AutoAdderCategory.values()) {
			if (category.getCategoryId() == id) {
				return category;
			}
		}
		return null;
	}
}
