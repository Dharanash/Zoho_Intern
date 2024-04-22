package online_shopping_website.enums;

public enum ProductStatus {
	Available(1),
	OutOfStock(2),
	NotAvailable(3);
	
	private final int productStatus;

	ProductStatus(int ProductStatus) {
        this.productStatus = ProductStatus;
    }

    public int getProductStatusId() {
        return productStatus;
    }
    
    public static ProductStatus getProductStatusFromId(int id) {
        for (ProductStatus status : ProductStatus.values()) {
            if (status.getProductStatusId() == id) {
                return status;
            }
        }
        return null;
    }
}
