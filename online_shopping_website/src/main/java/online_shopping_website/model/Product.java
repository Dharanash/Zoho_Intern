package online_shopping_website.model;

import java.sql.Timestamp;

import online_shopping_website.enums.ProductStatus;

public class Product {
	
	public int productId;
    public String name;
    public String description;
    public double price;
    public String productStatus;
    public int productStatusId;
    
    public Product(int productId, String name, String description, double price) {
        this.productId =-1;
    	this.name = name;
        this.description = description;
        this.price = price;
    }
    
    public Product(String name, String description, double price, int productStatusId) {
        this.productId =-1;
    	this.name = name;
        this.description = description;
        this.price = price;
        this.productStatusId = productStatusId;
    }
    
    public Product(int productId, String name, String description, double price,  String productStatus, int prodictStatusId) {
    	this.productId =productId;
    	this.name = name;
        this.description = description;
        this.price = price;
        this.productStatus = productStatus;
        this.productStatusId=prodictStatusId;
    }

    

	public int getProductId() {
		return productId;
	}

	public String getProductStatus() {
		return productStatus;
	}
	
	public void setProductStatus(ProductStatus productStatus) {
		 this.productStatus = productStatus.toString();
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getProductStatusId() {
		return productStatusId;
	}

	public void setProductStatusId(int productStatusId) {
		this.productStatusId = productStatusId;
	}

	public void setProductStatus(String productStatus) {
		this.productStatus = productStatus;
	}
    
    
}