package online_shopping_website.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class DetailedProduct extends Product {
	public int createdById;
	public int modifiedById;
	public String createdBy;
    public String modifiedBy;
    public Timestamp createdTime;
    public Timestamp modifiedTime;
    
    public DetailedProduct(int productId, String name, String description, double price,int quantity,int createdById, String createdBy,int modifiedById, String modifiedBy, Timestamp createdTime, Timestamp modified, String productStatus, int productStatusId) {
        super(productId, name, description, price,quantity,productStatus, productStatusId);
        this.createdById=createdById;
        this.modifiedById= modifiedById;
        this.createdBy = createdBy;
        this.modifiedBy = modifiedBy;
        this.createdTime = createdTime;
        this.modifiedTime = modified;
    }
    
    public DetailedProduct(String name, String description, double price,int quantity, int createdBy, int productStatusId) {
    	super(name, description, price,quantity, productStatusId);
    	this.createdById = createdBy;
        this.modifiedById = createdBy;
        this.createdTime = new Timestamp(System.currentTimeMillis());
        this.modifiedTime = new Timestamp(System.currentTimeMillis());
    }
    
    public DetailedProduct(int productId, String name, String description, double price,int quantity,  String productStatus, int prodictStatusId) {
    	super(productId, name, description, price,quantity, productStatus, prodictStatusId);
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

	public int getCreatedById() {
		return createdById;
	}

	public void setCreatedById(int createdById) {
		this.createdById = createdById;
	}

	public int getModifiedById() {
		return modifiedById;
	}

	public void setModifiedById(int modifiedById) {
		this.modifiedById = modifiedById;
	}
	
}
