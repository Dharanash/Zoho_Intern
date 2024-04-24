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

	
	public void addToCart(int userId, int productId) throws ClassNotFoundException, SQLException {
		try (Connection conn = DatabaseConnectionDAO.getConnection();
				PreparedStatement statement = conn.prepareStatement(
						"INSERT INTO cart (productid, userid, productquantity, addeddate) VALUES (?, ?, ?, ?)")) {
			statement.setInt(1, productId);
			statement.setInt(2, userId);
			statement.setInt(3, 1);
			statement.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
			statement.executeUpdate();
		}
	}
	
	public ArrayList<Cart> getCartElements(int userId) throws ClassNotFoundException, SQLException {
		String query = "SELECT c.*, i.*, p.status FROM cart as c inner join inventory as i on i.productid=c.productid and c.userid=? "
			+	"inner join productstatus p on p.id=i.productstatusid";
		try(Connection conn = DatabaseConnectionDAO.getConnection();
		PreparedStatement st = conn.prepareStatement(query)){
		st.setInt(1, userId);
		ResultSet resultSet = st.executeQuery();
		return MappingService.mapToCartList(resultSet);
		}
	}
	
	public void removeProductInCart(int productId, int userId) throws ClassNotFoundException, SQLException {
		String query = "DELETE FROM cart WHERE productid = ? AND userid = ?";
		try (Connection conn = DatabaseConnectionDAO.getConnection(); PreparedStatement statement = conn.prepareStatement(query)) {
			statement.setInt(1, productId);
			statement.setInt(2, userId);
			statement.executeUpdate();
		}
	}
	
	public void updateProductQuantityInCart(int productId, int userId, int nquantity) throws ClassNotFoundException, SQLException {
		try (Connection conn = DatabaseConnectionDAO.getConnection();
				PreparedStatement st = conn
						.prepareStatement("UPDATE cart SET productquantity = ? WHERE userid = ? AND productid = ?")) {
			st.setInt(1, Math.abs(nquantity));
			st.setInt(2, userId);
			st.setInt(3, productId);
			st.executeUpdate();
		}
	}
	
	public boolean isProductExistInCart(int productId, int userId) throws ClassNotFoundException, SQLException {
		String query = "SELECT * FROM cart WHERE productid = ? AND userid= ?";
		try (Connection connection = DatabaseConnectionDAO.getConnection(); PreparedStatement st = connection.prepareStatement(query)) {

			st.setInt(1, productId);
			st.setInt(2, userId);
			ResultSet rs = st.executeQuery();
			if (rs.next()) {
				return true;
			}
			return false;
		}
	}
	
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

		String insertQuery = "INSERT INTO orders (productid, orderstatusid, addtime, customer_detailsid, productquantity, productprice, userid) VALUES (?, ?, ?, ?, ?, ?, ?)";
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
					insertStatement.setInt(5,Math.abs(uquantity));
					insertStatement.setDouble(6,Math.abs(productPrice));
					insertStatement.setInt(7, userId);
					insertStatement.executeUpdate();
				}
			}
		}
	}
	
	public ArrayList<Order> getUserOrders(int userId) throws ClassNotFoundException, SQLException {
		String query = "SELECT o.*,c.address,c.pincode,i.productname,i.description,i.quantity, u.name, os.* FROM orders o "
				+ "JOIN customer_details c ON o.customer_detailsid = c.customer_detailsid and o.userid =? "
				+ "join orderstatus os on o.orderstatusid=os.id "
				+ "JOIN inventory i ON o.productid = i.productid "
				+ "JOIN users u ON o.userid = u.userid order by addtime ";
		return getOrders(userId, query);
	}
	
	public ArrayList<Order> getOrdersForDispatch(int userId ) throws ClassNotFoundException, SQLException {
		String query = "SELECT o.*,c.address,c.pincode,i.productname,i.description,i.quantity,u.name, os.* FROM orders o "
				+ "JOIN customer_details c ON o.customer_detailsid = c.customer_detailsid "
				+ "join orderstatus os on o.orderstatusid=os.id "
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
		String query = "SELECT o.*,c.address,c.pincode,i.productname,i.description,i.quantity, u.name, os.* FROM orders o "
				+ "JOIN customer_details c ON o.customer_detailsid = c.customer_detailsid "
				+ "JOIN inventory i ON o.productid = i.productid "
				+ "join orderstatus os on o.orderstatusid=os.id "
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
	
	public Order getOrderFromId(int orderId) throws ClassNotFoundException, SQLException {
		String query = "SELECT o.*,c.address,c.pincode,i.productname,i.description,i.quantity, u.name, os.* FROM orders o "
				+ "JOIN customer_details c ON o.customer_detailsid = c.customer_detailsid and o.orderid=? "
				+ "JOIN inventory i ON o.productid = i.productid "
				+ "join orderstatus os on o.orderstatusid=os.id "
				+ "JOIN users u ON o.userid = u.userid";
		try(Connection connection =DatabaseConnectionDAO.getConnection();
		PreparedStatement statement = connection.prepareStatement(query)){
		statement.setInt(1, orderId);
		ResultSet resultSet = statement.executeQuery();
		return MappingService.mapToOrder(resultSet);
		}
	}
	
	public boolean deduceProductQuantityFromInventory(ArrayList<Cart> cartElements,int userId) throws ClassNotFoundException, SQLException {

	    String updateQuery = "UPDATE inventory SET quantity = ? WHERE productid = ?";
	    try (Connection connection = DatabaseConnectionDAO.getConnection()) {
	        connection.setAutoCommit(false);

	        try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
	            for (Cart cart : cartElements) {
	            	if(cart.getProductStatusId()==ProductStatus.Available.getProductStatusId()) {
	                int quantity = cart.getQuantity();
	                int productQuantity = cart.getProductQuantity();
	                if (quantity - productQuantity < 0) {
	                    connection.rollback();
	                    return false;
	                } else {
	                    int productId = cart.getProductId();
	                    int updatedQuantity = quantity - productQuantity;
	                    updateStatement.setInt(1, updatedQuantity);
	                    updateStatement.setInt(2, productId);
	                    updateStatement.executeUpdate();
	                }
	            	}
	            }
	            connection.commit();
	            return true;
	        } catch (SQLException e) {
	            connection.rollback();
	            throw e;
	        } finally {
	            connection.setAutoCommit(true);
	        }
	    }
	}
	
	public double releaseProductsToInventory(int orderId) throws ClassNotFoundException, SQLException {
		Order orderElements = getOrderFromId(orderId);
		String updateQuery = "UPDATE inventory SET quantity = quantity + ? WHERE productid = ?";
		try (Connection connection = DatabaseConnectionDAO.getConnection();
				PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {

			double totalAmount = 0;
			if (orderElements!=null) {
				int productId = orderElements.getProductId();
				int uquantity = orderElements.getProductQuantity();
				double price = orderElements.getPrice();
				updateStatement.setInt(1, uquantity);
				updateStatement.setInt(2, productId);
				updateStatement.executeUpdate();
				totalAmount += (double)uquantity * price;
			}
			return totalAmount;
		}

	}
}
