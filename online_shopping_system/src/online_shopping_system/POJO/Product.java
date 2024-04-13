package online_shopping_system.POJO;

import java.sql.Timestamp;

public class Product {
    public String name;
    public String description;
    public double price;
    public int quantity;
    public int createdBy;
    public int modifiedBy;
    public Timestamp createdTime;
    public Timestamp modifiedTime;

    public Product(String name, String description, double price, int quantity, int createdBy) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.createdBy = createdBy;
        this.modifiedBy = createdBy;
        this.createdTime = new Timestamp(System.currentTimeMillis());
        this.modifiedTime = new Timestamp(System.currentTimeMillis());
    }

    public void setModefiedBy(int modifiedBy){
        this.modifiedBy = modifiedBy;
    }

    public void setModifiedTime() {
        this.modifiedTime = new Timestamp(System.currentTimeMillis());
    }

}