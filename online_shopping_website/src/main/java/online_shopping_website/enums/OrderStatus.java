package online_shopping_website.enums;

public enum OrderStatus {
	Placed(1),
	Dispatched(2),
	Cancelled(3);
	
	private final int orderStatusId;

	OrderStatus(int orderStatusId) {
        this.orderStatusId = orderStatusId;
    }

    public int getOrderStatusId() {
        return orderStatusId;
    }
    
    public static OrderStatus getOrderStatusFromId(int id) {
        for (OrderStatus status : OrderStatus.values()) {
            if (status.getOrderStatusId() == id) {
                return status;
            }
        }
        return null;
    }
}
