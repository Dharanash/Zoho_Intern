package com.expensecalculator.enums;

public enum AutoAdderStatus {
	Checked(1), Unchecked(2), Repeated(3);

	private final int statusId;

	AutoAdderStatus(int statusId) {
	        this.statusId = statusId;
	    }

	public int getStatusId() {
		return statusId;
	}

	public static AutoAdderStatus getRoleFromId(int id) {
		for (AutoAdderStatus status : AutoAdderStatus.values()) {
			if (status.getStatusId() == id) {
				return status;
			}
		}
		return null;
	}
}
