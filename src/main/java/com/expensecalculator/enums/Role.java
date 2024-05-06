package com.expensecalculator.enums;

public enum Role {
    Admin(1),
    GeneralUser(2);

    private final int roleId;

    Role(int roleId) {
        this.roleId = roleId;
    }

    public int getRoleId() {
        return roleId;
    }
    
    public static Role getRoleFromId(int id) {
        for (Role role : Role.values()) {
            if (role.getRoleId() == id) {
                return role;
            }
        }
        return null;
    }
}
