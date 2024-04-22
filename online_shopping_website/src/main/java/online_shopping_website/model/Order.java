package online_shopping_website.model;

import java.sql.Timestamp;

import online_shopping_website.enums.OrderStatus;

public class Order extends Cart {
	public int orderId;
	public int orderStatusId;
	public DeliveryDetails deliveryDetails;
	public String userName;
	
	public Order(Cart cart, int OrderId) {
		super(cart.productId, cart.name, cart.description, cart.price, cart.productQuantity, new Timestamp(System.currentTimeMillis()));
		this.orderId = OrderId;
		this.orderStatusId=OrderStatus.Placed.getOrderStatusId();
	}
	
	public Order(int productId, String name, String descriprion, double price, int productQuantity, 
			Timestamp addedTime ,int orderId, int orderStatusId, String deliveryAddress,int pincode, String userName,int userId ) {
		super(productId, name, descriprion, price, productQuantity, addedTime);
		this.orderId = orderId;
		this.orderStatusId=orderStatusId;
		deliveryDetails = new DeliveryDetails(userId, deliveryAddress, pincode);
		this.userName=userName;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public int getOrderStatusId() {
		return orderStatusId;
	}

	public void setOrderStatusId(int orderStatusId) {
		this.orderStatusId = orderStatusId;
	}

	public String getDeliveryAddress() {
		return deliveryDetails.address;
	}

	public void setDeliveryAddress(String deliveryAddress) {
		this.deliveryDetails.address = deliveryAddress;
	}
	
	public int getDeliveryPincode() {
		return deliveryDetails.pincode;
	}

	public void setDeliveryPincode(int pincode) {
		this.deliveryDetails.pincode = pincode;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
}
