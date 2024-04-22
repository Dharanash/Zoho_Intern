package online_shopping_website.services;

import java.util.ArrayList;

import online_shopping_website.enums.ProductStatus;
import online_shopping_website.model.Cart;
import online_shopping_website.model.DeliveryDetails;
import online_shopping_website.model.Order;
import online_shopping_website.model.Product;

public class UtilityService {
	
	public static <T extends Product> T getProductFromId(ArrayList<T> products, int productId) {
	    for (T item : products) {
	        if (item.getProductId() == productId) {
	            return item;
	        }
	    }
	    return null;
	}
	
	public static void removeProductFromId(ArrayList<? extends Product> products, int productId) {
		for (int i = 0; i < products.size(); i++) {
		    if (products.get(i).getProductId() == productId) {
		        products.remove(i);
		        break;
		    }
		}
	}
	
	public static boolean isProductExistInCart(int productId, ArrayList<Cart> cart) {
		for (Cart c : cart) {
	        if (c.getProductId() == productId) {
	            return true;
	        }
	    }
		return false;
	}
	
	public static double getTotalAmount(ArrayList<Cart> cart) {
		double totalAmount=0;
		for (Cart item : cart) {
	        if (item.getProductStatusId()==ProductStatus.Available.getProductStatusId()) {
	        	totalAmount+=item.price*item.productQuantity;
	        }
	    }
		
		return totalAmount;
	}
	
	public static DeliveryDetails getDeliveryDetailFromId(ArrayList<DeliveryDetails> details, int id) {
	    for (DeliveryDetails item : details) {
	        if (item.getDeliveryDetailsId() == id) {
	            return item;
	        }
	    }
	    return null;
	}
	
	public static Order getOrderFromId(ArrayList<Order> orders, int id) {
	    for (Order item : orders) {
	        if (item.getOrderId() == id) {
	            return item;
	        }
	    }
	    return null;
	}
	
	
}
