package online_shopping_system.enums;

public enum Role {
    Admin(1),
    Manager(2),
    Customer(3);

    private final int roleId;

    Role(int roleId) {
        this.roleId = roleId;
    }

    public int getRoleId() {
        return roleId;
    }
}
