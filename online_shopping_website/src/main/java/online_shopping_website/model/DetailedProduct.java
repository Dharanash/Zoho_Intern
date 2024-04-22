package online_shopping_website.model;

import java.sql.Timestamp;

public class DetailedProduct extends Product {
	public String createdBy;
    public String modifiedBy;
    public Timestamp createdTime;
    public Timestamp modifiedTime;
    
    public DetailedProduct(int productId, String name, String description, double price, String createdBy, String modifiedBy, Timestamp createdTime, Timestamp modified, String productStatus, int productStatusId) {
        super(productId, name, description, price,productStatus, productStatusId);
        this.createdBy = createdBy;
        this.modifiedBy = modifiedBy;
        this.createdTime = createdTime;
        this.modifiedTime = modified;
    }
    
    public DetailedProduct(String name, String description, double price, String createdBy, int productStatusId) {
    	super(name, description, price, productStatusId);
        this.createdBy = createdBy;
        this.modifiedBy = createdBy;
        this.createdTime = new Timestamp(System.currentTimeMillis());
        this.modifiedTime = new Timestamp(System.currentTimeMillis());
    }
    
    public DetailedProduct(int productId, String name, String description, double price,  String productStatus, int prodictStatusId) {
    	super(productId, name, description, price, productStatus, prodictStatusId);
    }
    
    public void setModefiedBy(String modifiedBy){
        this.modifiedBy = modifiedBy;
    }

    public void setModifiedTime() {
        this.modifiedTime = new Timestamp(System.currentTimeMillis());
    }
    
    public String getCreatedBy() {
		return createdBy;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Timestamp getCreatedTime() {
		return createdTime;
	}

	public Timestamp getModifiedTime() {
		return modifiedTime;
	}
}
