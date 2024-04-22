package online_shopping_website.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import online_shopping_website.model.Cart;
import online_shopping_website.services.MappingService;

public class CartDAO {
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
		String query = "SELECT c.*, i.productname, i.description, i.productstatusid, i.price, p.status FROM cart as c inner join inventory as i on i.productid=c.productid and c.userid=? "
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
			st.setInt(1, nquantity);
			st.setInt(2, userId);
			st.setInt(3, productId);
			st.executeUpdate();
		}
	}

}
