package online_shopping_system.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import online_shopping_system.POJO.Product;
import online_shopping_system.POJO.User;
import online_shopping_system.POJO.Wallet;
import online_shopping_system.enums.OrderStatus;
import online_shopping_system.services.EncryptionService;
import online_shopping_system.services.MappingService;

public class DbConnectionDao {
	private static final String url = "jdbc:mysql://127.0.0.1:3306/e_commerce_web";
	private static final String username = "root";
	private static final String password = "";

	public Connection getConnection() throws SQLException, ClassNotFoundException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection connection = DriverManager.getConnection(url, username, password);

		return connection;
	}

	public User checkUser(String userEmail, String password) throws ClassNotFoundException, SQLException {
		User user = null;
		String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
		try (Connection connection = getConnection(); PreparedStatement st = connection.prepareStatement(sql)) {
			st.setString(1, userEmail);
			st.setString(2, EncryptionService.encryptPassword(password));
			ResultSet rs = st.executeQuery();
			if (rs.next()) {
				user = MappingService.mapToUser(rs);
			}
			return user;
		}

	}

	public void updateUser(User user) throws SQLException, ClassNotFoundException {
		String sql = "UPDATE users SET name = ?, contactnumber = ? WHERE userid = ?";
		try (Connection connection = getConnection(); PreparedStatement st = connection.prepareStatement(sql)) {
			st.setString(1, user.name);
			st.setInt(2, user.phoneNumber);
			st.setInt(3, user.userId);
			st.executeUpdate();
		}
	}

	public void addCustomerDetails(String address, int pincode, int userId)
			throws SQLException, ClassNotFoundException {
		String sql = "INSERT INTO customer_details (userid, address, pincode) VALUES (?, ?, ?)";
		try (Connection connection = getConnection(); PreparedStatement st = connection.prepareStatement(sql)) {
			st.setInt(1, userId);
			st.setString(2, address);
			st.setInt(3, pincode);
			st.executeUpdate();
		}
	}

	public int updateCustomerDetails(String address, int pincode, int customerId)
			throws SQLException, ClassNotFoundException {
		String sql = "UPDATE customer_details SET address=?, pincode=? WHERE customerId=?";
		try (Connection connection = getConnection(); PreparedStatement st = connection.prepareStatement(sql)) {
			st.setString(1, address);
			st.setInt(2, pincode);
			st.setInt(3, customerId);
			return st.executeUpdate();
		}
	}

	public ResultSet getCustomerDetails(int userId) throws SQLException, ClassNotFoundException {
		String sql = "SELECT * FROM customer_details WHERE userid = ?";
		Connection conn = getConnection();
		PreparedStatement st = conn.prepareStatement(sql);
		st.setInt(1, userId);
		return st.executeQuery();
	}

	public ResultSet getCustomerDetailsFromId(int cid) throws SQLException, ClassNotFoundException {
		String sql = "SELECT * FROM customer_details WHERE customer_detailsid = ?";
		Connection conn = getConnection();
		PreparedStatement st = conn.prepareStatement(sql);
		st.setInt(1, cid);
		return st.executeQuery();
	}

	public boolean isUserExist(String email) throws SQLException, ClassNotFoundException {
		String sql = "SELECT * FROM users WHERE email = ?";
		try (Connection connection = getConnection(); PreparedStatement st = connection.prepareStatement(sql)) {
			st.setString(1, email);
			ResultSet rs = st.executeQuery();
			if (rs.next()) {
				return true;
			}
			return false;
		}
	}

	public void addUser(User user) throws SQLException, ClassNotFoundException {
		String sql = "INSERT INTO users (name, email, password, role, contactnumber) VALUES (?, ?, ?, ?, ?)";
		try (Connection connection = getConnection(); PreparedStatement st = connection.prepareStatement(sql)) {

			st.setString(1, user.name);
			st.setString(2, user.email);
			st.setString(3, EncryptionService.encryptPassword(user.password));
			st.setInt(4, user.role);
			st.setInt(5, user.phoneNumber);
			st.executeUpdate();
		}
	}

	public void updateUserPassword(User user) throws ClassNotFoundException, SQLException {
		String sql = "UPDATE users SET password = ? WHERE userid = ?";
		try (Connection connection = getConnection(); PreparedStatement st = connection.prepareStatement(sql)) {

			st.setString(1, EncryptionService.encryptPassword(user.password));
			st.setInt(2, user.userId);
			st.executeUpdate();
		}
	}

	public void addProduct(Product product) throws ClassNotFoundException, SQLException {
		String sql = "INSERT INTO inventory (productname, description, price, quantity, createdBy, modifiedBy, createdTime, modifiedTime) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setString(1, product.name);
			statement.setString(2, product.description);
			statement.setDouble(3, product.price);
			statement.setInt(4, product.quantity);
			statement.setInt(5, product.createdBy);
			statement.setInt(6, product.modifiedBy);
			statement.setTimestamp(7, product.createdTime);
			statement.setTimestamp(8, product.modifiedTime);

			statement.executeUpdate();
		}
	}

	public boolean isProductExistInInventory(int productId) throws ClassNotFoundException, SQLException {
		String query = "SELECT * FROM inventory WHERE productid = ?";
		try (Connection connection = getConnection(); PreparedStatement st = connection.prepareStatement(query)) {

			st.setInt(1, productId);
			ResultSet rs = st.executeQuery();
			if (rs.next()) {
				return true;
			}
			return false;
		}
	}

	public void updateProduct(int productId, Product updatedProduct) throws ClassNotFoundException, SQLException {
		try (Connection conn = getConnection();
				PreparedStatement statement = conn.prepareStatement("UPDATE inventory "
						+ "SET productname = ?, description = ?, price = ?, quantity = ?, modifiedBy = ?, modifiedTime = ? "
						+ "WHERE productid = ?")) {
			statement.setString(1, updatedProduct.name);
			statement.setString(2, updatedProduct.description);
			statement.setDouble(3, updatedProduct.price);
			statement.setInt(4, updatedProduct.quantity);
			statement.setInt(5, updatedProduct.modifiedBy);
			statement.setTimestamp(6, updatedProduct.modifiedTime);
			statement.setInt(7, productId);

			statement.executeUpdate();

		}
	}

	public void removeProductInInventory(int productId) throws ClassNotFoundException, SQLException {
		String query = "DELETE FROM inventory WHERE productid = ?";
		try (Connection conn = getConnection(); PreparedStatement statement = conn.prepareStatement(query)) {
			statement.setInt(1, productId);

			statement.executeUpdate();
		}
	}

	public ResultSet getInventory(int roleId) throws ClassNotFoundException, SQLException {
		String query = "";
		if (roleId == 3) {
			query = "SELECT productid, productname, description, price, quantity FROM inventory";
		} else {
			query = "SELECT * FROM inventory";
		}

		Connection conn = getConnection();
		PreparedStatement statement = conn.prepareStatement(query);
		return statement.executeQuery();
	}

	public String getUserName(int userId) throws ClassNotFoundException {
		String query = "SELECT name FROM users WHERE userid = ?";
		String username = "No Longer availble";

		try (Connection connection = getConnection();
			PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setInt(1, userId);

			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				username = resultSet.getString("name");
			}
		} catch (SQLException e) {
			System.err.println("Error getting username: " + e.getMessage());
		}

		return username;
	}

	public void addToCart(int userId, int productId, int quantity) throws ClassNotFoundException, SQLException {
		try (Connection conn = getConnection();
				PreparedStatement statement = conn.prepareStatement(
						"INSERT INTO cart (productid, userid, quantity, addtime) VALUES (?, ?, ?, ?)")) {
			statement.setInt(1, productId);
			statement.setInt(2, userId);
			statement.setInt(3, quantity);
			statement.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
			statement.executeUpdate();
		}
	}

	public boolean isProductExistInCart(int productId, int userId) throws ClassNotFoundException, SQLException {
		String query = "SELECT * FROM cart WHERE productid = ? AND userid= ?";
		try (Connection connection = getConnection(); PreparedStatement st = connection.prepareStatement(query)) {

			st.setInt(1, productId);
			st.setInt(2, userId);
			ResultSet rs = st.executeQuery();
			if (rs.next()) {
				return true;
			}
			return false;
		}
	}

	public void removeProductInCart(int productId, int userId) throws ClassNotFoundException, SQLException {
		String query = "DELETE FROM cart WHERE productid = ? AND userid = ?";
		try (Connection conn = getConnection(); PreparedStatement statement = conn.prepareStatement(query)) {
			statement.setInt(1, productId);
			statement.setInt(2, userId);
			statement.executeUpdate();
		}
	}

	public ResultSet getCartElements(int userId) throws ClassNotFoundException, SQLException {
		String query = "SELECT * FROM cart WHERE userid=?";
		Connection conn = getConnection();
		PreparedStatement st = conn.prepareStatement(query);
		st.setInt(1, userId);
		return st.executeQuery();
	}

	public ResultSet getProductFromId(int productId) throws ClassNotFoundException, SQLException {
		String query = "SELECT * FROM inventory WHERE productid=?";
		Connection conn = getConnection();
		PreparedStatement st = conn.prepareStatement(query);
		st.setInt(1, productId);
		return st.executeQuery();
	}

	public void updateProductQuantityInCart(int productId, int userId, int nquantity) throws ClassNotFoundException {
		try (Connection conn = getConnection();
				PreparedStatement st = conn
						.prepareStatement("UPDATE cart SET quantity = ? WHERE userid = ? AND productid = ?")) {
			st.setInt(1, nquantity);
			st.setInt(2, userId);
			st.setInt(3, productId);
			st.executeUpdate();
		} catch (SQLException e) {
			System.err.println("Error updating cart quantity: " + e.getMessage());
		}
	}

	public Wallet getWalletFromId(int userId) throws ClassNotFoundException {
		Wallet wallet = null;
		try (Connection conn = getConnection();
			PreparedStatement st = conn.prepareStatement("Select * from wallet WHERE userid = ?")) {
			st.setInt(1, userId);
			ResultSet rs = st.executeQuery();
			if (!rs.next()) {
				return addWallet(userId);
			}

			wallet = MappingService.mapToWallet(rs);
		} catch (SQLException e) {
			System.err.println("Error updating cart quantity: " + e.getMessage());
		}
		return wallet;
	}

	public Wallet addWallet(int userId) throws ClassNotFoundException, SQLException {
		Wallet wallet = null;
		try (Connection conn = getConnection();
				PreparedStatement st = conn.prepareStatement(
						"INSERT INTO wallet (balance, userid, points) VALUES (?, ?, ?)",
						PreparedStatement.RETURN_GENERATED_KEYS)) {
			wallet = new Wallet(userId);
			st.setDouble(1, wallet.balance); // Initial balance
			st.setInt(2, wallet.userId);
			st.setInt(3, wallet.point);
			st.executeUpdate();
			try (ResultSet gK = st.getGeneratedKeys()) {
				if (gK.next()) {
					int walletId = gK.getInt(1);
					wallet.walletId = walletId;
				}
			}
		}

		return wallet;
	}

	public int addOrder(int userId, int customerDetailsId) throws ClassNotFoundException {
		int orderId = -1;
		try (Connection conn = getConnection();
				PreparedStatement st = conn.prepareStatement(
						"INSERT INTO orders (userid, status, addtime, customer_detailsid) VALUES (?, ?, ?, ?)",
						PreparedStatement.RETURN_GENERATED_KEYS)) {
			st.setInt(1, userId);
			st.setString(2, OrderStatus.Placed.toString());
			st.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
			st.setInt(4, customerDetailsId);
			st.executeUpdate();
			try (ResultSet gK = st.getGeneratedKeys()) {
				if (gK.next()) {
					orderId = gK.getInt(1);
				}
			}
		} catch (SQLException e) {
			System.err.println("Error placing order : " + e.getMessage());
		}
		return orderId;
	}

	public void fillOrderDetailsFromCart(int orderId, int userId) throws SQLException, ClassNotFoundException {
		ResultSet cartElements = getCartElements(userId);

		String insertQuery = "INSERT INTO order_details (orderid, productid, quantity, productprice) VALUES (?, ?, ?, ?)";
		try (Connection connection = getConnection();
				PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {

			while (cartElements.next()) {
				int productId = cartElements.getInt("productid");
				int uquantity = cartElements.getInt("quantity");
				double productPrice = 0;
				try (ResultSet productSt = getProductFromId(productId)) {
					if (productSt.next()) {
						productPrice = productSt.getDouble("price");
					}
				}
				insertStatement.setInt(1, orderId);
				insertStatement.setInt(2, productId);
				insertStatement.setInt(3, uquantity);
				insertStatement.setDouble(4, productPrice);
				insertStatement.executeUpdate();
			}
		}
	}

	public void deduceProductQuantityFromInventory(int userId) throws ClassNotFoundException, SQLException {
		ResultSet cartElements = getCartElements(userId);

		String updateQuery = "UPDATE inventory SET quantity = ? WHERE productid = ?";
		try (Connection connection = getConnection();
				PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {

			while (cartElements.next()) {
				int productId = cartElements.getInt("productid");
				int uquantity = cartElements.getInt("quantity");
				ResultSet productResult = getProductFromId(productId);
				int updatedQuantity = 0;
				if (productResult.next()) {
					int actualQunatity = productResult.getInt("quantity");
					updatedQuantity = actualQunatity - uquantity;
					updateStatement.setInt(1, updatedQuantity);
					updateStatement.setInt(2, productId);
					updateStatement.executeUpdate();
				}
			}
		}
		cartElements.close();
	}

	public double releaseProductsToInventory(int orderId) throws ClassNotFoundException, SQLException {
		ResultSet orderElements = getOrderDetails(orderId);
		String updateQuery = "UPDATE inventory SET quantity = ? WHERE productid = ?";
		try (Connection connection = getConnection();
				PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {

			double totalAmount = 0;
			while (orderElements.next()) {
				int productId = orderElements.getInt("productid");
				int uquantity = orderElements.getInt("quantity");
				double price = orderElements.getDouble("productprice");
				ResultSet productResult = getProductFromId(productId);
				int updatedQuantity = 0;
				if (productResult.next()) {
					int actualQunatity = productResult.getInt("quantity");
					updatedQuantity = actualQunatity + uquantity;
					updateStatement.setInt(1, updatedQuantity);
					updateStatement.setInt(2, productId);
					updateStatement.executeUpdate();
				}
				totalAmount += uquantity * price;
			}
			orderElements.close();
			return totalAmount;
		}

	}

	public void updateWallet(Wallet wallet) throws ClassNotFoundException, SQLException {
		try (Connection conn = getConnection();
				PreparedStatement st = conn
						.prepareStatement("UPDATE wallet " + "SET balance = ?, points = ? " + "WHERE userid = ?")) {
			st.setDouble(1, wallet.balance);
			st.setInt(2, wallet.point);
			st.setInt(3, wallet.userId);

			st.executeUpdate();
		}
	}

	public ResultSet getOrders(int userId) throws ClassNotFoundException, SQLException {
		String query = "SELECT * FROM orders WHERE userid = ?";
		ResultSet resultSet = null;

		Connection connection = getConnection();
		PreparedStatement statement = connection.prepareStatement(query);
		statement.setInt(1, userId);
		resultSet = statement.executeQuery();

		return resultSet;
	}

	public ResultSet getAllOrders() throws SQLException, ClassNotFoundException {
		String query = "SELECT * FROM orders";
		Connection connection = getConnection();
		PreparedStatement statement = connection.prepareStatement(query);
		ResultSet resultSet = statement.executeQuery();

		return resultSet;
	}

	public ResultSet getOrderDetails(int orderId) throws SQLException, ClassNotFoundException {
		String query = "SELECT * FROM order_details WHERE orderid = ?";

		Connection connection = getConnection();
		PreparedStatement statement = connection.prepareStatement(query);
		statement.setInt(1, orderId);
		ResultSet resultSet = statement.executeQuery();

		return resultSet;
	}

	public boolean isOrderExist(int orderId) throws ClassNotFoundException, SQLException {
		String query = "SELECT * FROM orders WHERE orderid = ?";

		try (Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setInt(1, orderId);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				return true;
			}
			return false;
		}

	}

	public ResultSet getOrderFromId(int orderId) throws ClassNotFoundException, SQLException {
		String query = "SELECT * FROM orders WHERE orderid = ?";
		Connection connection = getConnection();
		PreparedStatement statement = connection.prepareStatement(query);
		statement.setInt(1, orderId);
		ResultSet resultSet = statement.executeQuery();
		return resultSet;
	}

	public void updateOrderStatus(int orderId, OrderStatus orderStatus) throws SQLException, ClassNotFoundException {
		String updateQuery = "UPDATE orders SET status = ? WHERE orderid = ?";
		try (Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement(updateQuery)) {
			statement.setString(1, orderStatus.toString());
			statement.setInt(2, orderId);
			statement.executeUpdate();
		}

	}

}
