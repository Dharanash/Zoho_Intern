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
import online_shopping_website.services.*;

public class InventoryDAO {
	public int getUserId(String email) throws ClassNotFoundException, SQLException {
		String query = "SELECT userid FROM users WHERE email = ?";
		int userId = -1;

		try (Connection connection = DatabaseConnectionDAO.getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, email);

			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				userId = resultSet.getInt("userid");
			}
		}

		return userId;
	}
	
	public void addProduct(DetailedProduct product) throws ClassNotFoundException, SQLException {
		String sql = "INSERT INTO inventory (productname, description, price, createdby, modifiedby, createdtime, modifiedtime, productstatusid, quantity) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?,?)";
		try (Connection connection = DatabaseConnectionDAO.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setString(1, product.name);
			statement.setString(2, product.description);
			statement.setDouble(3,Math.abs(product.price));
			statement.setInt(4, product.createdById);
			statement.setInt(5, product.modifiedById);
			statement.setTimestamp(6, product.createdTime);
			statement.setTimestamp(7, product.modifiedTime);
			statement.setInt(8, product.productStatusId);
			statement.setInt(9,Math.abs(product.quantity));
			statement.executeUpdate();
		}
		
	}

	public ArrayList<DetailedProduct> getInventory(int userId, Role role) throws ClassNotFoundException, SQLException {
		String query="";
		if (role == Role.Customer) {
			query = "SELECT productid, productname, description, price,quantity, productstatusid, p.status as productStatus FROM inventory "
					+ "left join productstatus p on productstatusid=p.id "
					+ "where productstatusid<3";
		} else if (role == Role.Manager) {
			query = "SELECT i.*, u1.email AS createdByEmail,u2.email AS modifiedByEmail, p.status as productStatus FROM inventory i "
					+ "LEFT JOIN users u1 ON i.createdby = u1.userid "
					+ "LEFT JOIN users u2 ON i.modifiedby = u2.userid "
					+ "left join productstatus p on productstatusid=p.id "
					+ "WHERE i.createdby = ? AND i.productstatusid <3";
		} else {
			query = "SELECT i.*, u1.email AS createdByEmail,u2.email AS modifiedByEmail, p.status as productStatus FROM inventory i "
					+ "LEFT JOIN users u1 ON i.createdby = u1.userid "
					+ "LEFT JOIN users u2 ON i.modifiedby = u2.userid "
					+ "left join productstatus p on productstatusid=p.id "
					+ "where productstatusid<3";
		}

		try(Connection conn = DatabaseConnectionDAO.getConnection();
		PreparedStatement statement = conn.prepareStatement(query)){
		if(role == Role.Manager) {
			statement.setInt(1, userId);
		}
		ResultSet set = statement.executeQuery();
		return MappingService.mapToProductList(set, role);
	  }
	}
	
	public void updateProduct(int productId, DetailedProduct updatedDetailedProduct) throws ClassNotFoundException, SQLException {
		try (Connection conn = DatabaseConnectionDAO.getConnection();
				PreparedStatement statement = conn.prepareStatement("UPDATE inventory "
						+ "SET productname = ?, description = ?, price = ?, modifiedBy = ?, modifiedTime = ?, productstatusid=?, quantity=? "
						+ "WHERE productid = ?")) {
			statement.setString(1, updatedDetailedProduct.name);
			statement.setString(2, updatedDetailedProduct.description);
			statement.setDouble(3,Math.abs(updatedDetailedProduct.price));
			statement.setInt(4, updatedDetailedProduct.modifiedById);
			statement.setTimestamp(5, updatedDetailedProduct.modifiedTime);
			statement.setInt(6, updatedDetailedProduct.productStatusId);
			statement.setInt(7,Math.abs(updatedDetailedProduct.quantity));
			statement.setInt(8, productId);

			statement.executeUpdate();
		}
	}
	
	public void removeProductFromInventory(int userId, int productId) throws ClassNotFoundException, SQLException {
		String query = "UPDATE inventory SET productstatusId=?, modifiedby=?, modifiedtime=? WHERE productid = ?";
		try (Connection conn =  DatabaseConnectionDAO.getConnection(); PreparedStatement statement = conn.prepareStatement(query)) {
			statement.setInt(1, ProductStatus.NotAvailable.getProductStatusId());
			statement.setInt(2, userId);
			statement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
			statement.setInt(4, productId);

			statement.executeUpdate();
		}
	}
	
	public HashMap<Integer, String> getProductStatus() throws ClassNotFoundException, SQLException{
		String query = "SELECT * FROM productstatus where id in (1, 2)";
		try (Connection conn =  DatabaseConnectionDAO.getConnection(); PreparedStatement statement = conn.prepareStatement(query)) {
			ResultSet resultSet = statement.executeQuery();
			return MappingService.mapToProductStatus(resultSet);
		}
	}
	
	public DetailedProduct getProductFromId(int productId, Role role) throws ClassNotFoundException, SQLException {
		String query = "SELECT i.*, p.status as productStatus FROM inventory as i inner join productstatus p on p.id=i.productstatusid and i.productid=? ";
		try(Connection conn = DatabaseConnectionDAO.getConnection();
		PreparedStatement st = conn.prepareStatement(query)){
		st.setInt(1, productId);
		ResultSet resultSet= st.executeQuery();
		return MappingService.mapToProduct(resultSet, role);
		}
	}
}
