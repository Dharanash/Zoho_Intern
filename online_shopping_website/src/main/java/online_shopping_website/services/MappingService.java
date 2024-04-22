package online_shopping_website.services;

import online_shopping_website.enums.Role;
import online_shopping_website.model.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

public class MappingService {

	public static User mapToUser(ResultSet result) throws SQLException {
		User user = null;
		int userid = result.getInt("userid");
		String name = result.getString("name");
		String password = result.getString("password");
		Role role = Role.getRoleFromId(result.getInt("role"));
		String email = result.getString("email");
		String phoneNumber = result.getString("phonenumber");
		user = new User(userid, name, password, role, email, phoneNumber);

		return user;
	}

	public static ArrayList<DetailedProduct> mapToProductList(ResultSet result, Role role) throws SQLException {
		ArrayList<DetailedProduct> products = new ArrayList<>();
		while (result.next()) {
			int productId = result.getInt("productid");
			String name = result.getString("productname");
			String description = result.getString("description");
			double price = result.getDouble("price");
			int productStatusId = result.getInt("productstatusid");
			String productStatus = result.getString("productStatus");
			if (role == Role.Customer) {
				products.add(new DetailedProduct(productId, name, description, price, productStatus, productStatusId));
				continue;
			}
			String createdby = result.getString("createdByEmail");
			String modifiedby = result.getString("modifiedByEmail");
			Timestamp createdTime = result.getTimestamp("createdtime");
			Timestamp modifiedTime = result.getTimestamp("modifiedtime");
			products.add(new DetailedProduct(productId, name, description, price, createdby, modifiedby, createdTime,
					modifiedTime, productStatus, productStatusId));
		}

		return products;

	}

	public static ArrayList<Cart> mapToCartList(ResultSet result) throws SQLException {
		ArrayList<Cart> cart = new ArrayList<>();
		while (result.next()) {
			int productId = result.getInt("productid");
			String name = result.getString("productname");
			String description = result.getString("description");
			double price = result.getDouble("price");
			int productStatusId = result.getInt("productstatusid");
			String productStatus = result.getString("status");
			int quantity = result.getInt("productquantity");
			Timestamp addeddate = result.getTimestamp("addeddate");
			cart.add(
					new Cart(productId, name, description, price, productStatus, productStatusId, quantity, addeddate));

		}

		return cart;
	}

	public static HashMap<Integer, String> mapToProductStatus(ResultSet resultSet) throws SQLException {
		HashMap<Integer, String> map = new HashMap<Integer, String>();
		while (resultSet.next()) {
			map.put(resultSet.getInt("id"), resultSet.getString("status"));
		}
		return map;
	}
	
	public static HashMap<Integer, String> mapToOrderStatus(ResultSet resultSet) throws SQLException {
		HashMap<Integer, String> map = new HashMap<Integer, String>();
		while (resultSet.next()) {
			map.put(resultSet.getInt("id"), resultSet.getString("status"));
		}
		return map;
	}

	public static Wallet mapToWallet(ResultSet result) throws SQLException {
		double balance = result.getDouble("balance");
		int userId = result.getInt("userid");
		int point = result.getInt("points");

		return new Wallet(balance, userId, point);
	}

	public static ArrayList<DeliveryDetails> mapToDeliveryDetails(ResultSet result) throws SQLException {
		ArrayList<DeliveryDetails> deliveryDetails = new ArrayList<>();
		while (result.next()) {
			int customer_detailsid = result.getInt("customer_detailsid");
			int userId = result.getInt("userid");
			String address = result.getString("address");
			int pincode = result.getInt("pincode");
			deliveryDetails.add(new DeliveryDetails(customer_detailsid, userId, address, pincode));
		}

		return deliveryDetails;
	}
	
	public static ArrayList<Order> mapToOrders(ResultSet result) throws SQLException {
		ArrayList<Order> order = new ArrayList<>();
		while (result.next()) {
			int productId = result.getInt("productid");
			String name = result.getString("productname");
			String description = result.getString("description");
			String userName = result.getString("name");
			String address = result.getString("address");
			int pincode = result.getInt("pincode");
			int orderId = result.getInt("orderid");
			int orderStatusId = result.getInt("orderstatusid");
			int quantity = result.getInt("quantity");
			Double price = result.getDouble("productprice");
			Timestamp addedTime= result.getTimestamp("addtime");
			int userId = result.getInt("userid");
			order.add(new Order(productId, name, description,price,quantity,addedTime,orderId, orderStatusId, address, pincode, userName, userId));
		}

		return order;
	}
}
