package online_shopping_website.model;

public class DeliveryDetails {
	public int deliveryDetailsId;
	public int userId;
	public String address;
	public int pincode;

	public DeliveryDetails(int id, int userId, String address, int pincode) {
		this.deliveryDetailsId=id;
		this.userId = userId;
		this.address = address;
		this.pincode = pincode;
	}
	
	public DeliveryDetails(int userId, String address, int pincode) {
		this.deliveryDetailsId=-1;
		this.userId = userId;
		this.address = address;
		this.pincode = pincode;
	}
	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public int getPincode() {
		return pincode;
	}
	public void setPincode(int pincode) {
		this.pincode = pincode;
	}

	public int getDeliveryDetailsId() {
		return deliveryDetailsId;
	}

	public void setDeliveryDetailsId(int deliveryDetailsId) {
		this.deliveryDetailsId = deliveryDetailsId;
	}
	
	
}
