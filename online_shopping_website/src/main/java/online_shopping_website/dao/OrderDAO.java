package online_shopping_website.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

import online_shopping_website.enums.*;
import online_shopping_website.model.*;
import online_shopping_website.services.MappingService;

public class OrderDAO extends WalletDAO{
	public ArrayList<DeliveryDetails> getCustomerDetails(int userId) throws SQLException, ClassNotFoundException {
		String sql = "SELECT * FROM customer_details WHERE userid = ?";
		try(Connection conn = DatabaseConnectionDAO.getConnection();
		PreparedStatement st = conn.prepareStatement(sql)){
		st.setInt(1, userId);
		ResultSet resultSet = st.executeQuery();
		return MappingService.mapToDeliveryDetails(resultSet);
		}
	}
	
	public int addCustomerDetails(DeliveryDetails detail)
			throws SQLException, ClassNotFoundException {
		int generatedId = -1;
		String sql = "INSERT INTO customer_details (userid, address, pincode) VALUES (?, ?, ?)";
		try (Connection connection = DatabaseConnectionDAO.getConnection(); PreparedStatement st = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			st.setInt(1, detail.userId);
			st.setString(2, detail.address);
			st.setInt(3, detail.pincode);
			st.executeUpdate();
			
			try (ResultSet resultSet = st.getGeneratedKeys()) {
	            if (resultSet.next()) {
	                generatedId = resultSet.getInt(1);
	            }
	        }
			
			return generatedId;
		}
	}

	public int updateCustomerDetails(DeliveryDetails detail)
			throws SQLException, ClassNotFoundException {
		String sql = "UPDATE customer_details SET address=?, pincode=? WHERE customer_detailsid=?";
		try (Connection connection = DatabaseConnectionDAO.getConnection(); PreparedStatement st = connection.prepareStatement(sql)) {
			st.setString(1, detail.address);
			st.setInt(2, detail.pincode);
			st.setInt(3, detail.deliveryDetailsId);
			return st.executeUpdate();
		}
	}
	
	public void addOrder(ArrayList<Cart> cartElements, int userId, int customerDetailsId) throws SQLException, ClassNotFoundException {

		String insertQuery = "INSERT INTO orders (productid, orderstatusid, addtime, customer_detailsid, quantity, productprice, userid) VALUES (?, ?, ?, ?, ?, ?, ?)";
		try (Connection connection =DatabaseConnectionDAO.getConnection();
				PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {

			for (Cart item: cartElements) {
				if (item.getProductStatusId()==ProductStatus.Available.getProductStatusId()) {
					int productId = item.getProductId();
					int uquantity = item.getProductQuantity();
					double productPrice = item.getPrice();
					insertStatement.setInt(1, productId);
					insertStatement.setInt(2, OrderStatus.Placed.getOrderStatusId());
					insertStatement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
					insertStatement.setDouble(4, customerDetailsId);
					insertStatement.setInt(5, uquantity);
					insertStatement.setDouble(6, productPrice);
					insertStatement.setDouble(7, userId);
					insertStatement.executeUpdate();
				}
			}
		}
	}
	
	public ArrayList<Order> getUserOrders(int userId) throws ClassNotFoundException, SQLException {
		String query = "SELECT o.*,c.address,c.pincode,i.productname,i.description, u.name FROM orders o "
				+ "JOIN customer_details c ON o.customer_detailsid = c.customer_detailsid and o.userid =? "
				+ "JOIN inventory i ON o.productid = i.productid "
				+ "JOIN users u ON o.userid = u.userid order by addtime ";
		return getOrders(userId, query);
	}
	
	public ArrayList<Order> getOrdersForDispatch(int userId ) throws ClassNotFoundException, SQLException {
		String query = "SELECT o.*,c.address,c.pincode,i.productname,i.description,u.name FROM orders o "
				+ "JOIN customer_details c ON o.customer_detailsid = c.customer_detailsid "
				+ "JOIN inventory i ON o.productid = i.productid and i.createdby =? "
				+ "JOIN users u ON o.userid = u.userid order by addtime ";
		return getOrders(userId, query);
	}

	public ArrayList<Order> getOrders(int Id, String query) throws ClassNotFoundException, SQLException {
		ResultSet resultSet = null;

		try(Connection connection = DatabaseConnectionDAO.getConnection();
		PreparedStatement statement = connection.prepareStatement(query)){
		statement.setInt(1, Id);
		resultSet = statement.executeQuery();
		
		return MappingService.mapToOrders(resultSet);
		}
	}

	public ArrayList<Order> getAllOrders() throws SQLException, ClassNotFoundException {
		String query = "SELECT o.*,c.address,c.pincode,i.productname,i.description, u.name FROM orders o "
				+ "JOIN customer_details c ON o.customer_detailsid = c.customer_detailsid "
				+ "JOIN inventory i ON o.productid = i.productid "
				+ "JOIN users u ON o.userid = u.userid order by addtime ";
		try(Connection connection = DatabaseConnectionDAO.getConnection();
		PreparedStatement statement = connection.prepareStatement(query)){
		ResultSet resultSet = statement.executeQuery();

		return MappingService.mapToOrders(resultSet);
		}
	}
	
	public HashMap<Integer, String> getOrderStatus() throws ClassNotFoundException, SQLException{
		String query = "SELECT * FROM orderstatus";
		try (Connection conn =  DatabaseConnectionDAO.getConnection(); PreparedStatement statement = conn.prepareStatement(query)) {
			ResultSet resultSet = statement.executeQuery();
			return MappingService.mapToOrderStatus(resultSet);
		}
	}
	
	public void updateOrderStatus(int orderId, OrderStatus orderStatus) throws SQLException, ClassNotFoundException {
		String updateQuery = "UPDATE orders SET orderstatusid = ? WHERE orderid = ?";
		try (Connection connection = DatabaseConnectionDAO.getConnection();
				PreparedStatement statement = connection.prepareStatement(updateQuery)) {
			statement.setInt(1, orderStatus.getOrderStatusId());
			statement.setInt(2, orderId);
			statement.executeUpdate();
		}

	}
}
