package online_shopping_website.model;

import java.sql.Timestamp;

public class Cart extends Product {
	public int productQuantity;
	public Timestamp addedTime;
	
	
	public Cart(int productId, String name, String description, double price, String productStatus, int productStatusId) {
		super(productId, name, description, price, productStatus, productStatusId);
		this.productQuantity= 1;
		this.addedTime = new Timestamp(System.currentTimeMillis());
	}
	
	public Cart(int productId, String name, String description, double price, String productStatus,int productStatusId, int productQuantity) {
		super(productId, name, description, price, productStatus, productStatusId);
		this.productQuantity= productQuantity;
		this.addedTime = new Timestamp(System.currentTimeMillis());
	}
	
	public Cart(int productId, String name, String description, double price, String productStatus,int productStatusId, int productQuantity, Timestamp timestamp) {
		super(productId, name, description, price, productStatus, productStatusId);
		this.productQuantity= productQuantity;
		this.addedTime = timestamp;
	}
	
	public Cart(int productId, String name, String description, double price,int productQuantity, Timestamp timestamp) {
		super(productId, name, description, price);
		this.productQuantity= productQuantity;
		this.addedTime = timestamp;
	}

	public int getProductQuantity() {
		return productQuantity;
	}

	public void setProductQuantity(int productQuantity) {
		this.productQuantity = productQuantity;
	}

	public Timestamp getAddedTime() {
		return addedTime;
	}

	public void setAddedTime(Timestamp addedTime) {
		this.addedTime = addedTime;
	}
	
}
