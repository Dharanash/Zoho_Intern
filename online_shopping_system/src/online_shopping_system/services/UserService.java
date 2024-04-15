package online_shopping_system.services;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import online_shopping_system.DAO.DbConnectionDao;
import online_shopping_system.Exceptions.UserNotifyException;
import online_shopping_system.POJO.Product;
import online_shopping_system.POJO.User;
import online_shopping_system.POJO.Wallet;
import online_shopping_system.enums.OrderStatus;
import online_shopping_system.enums.Role;

public class UserService {
	protected static DbConnectionDao dbconnDao=new DbConnectionDao();

	public User login() throws ClassNotFoundException, SQLException {
		String userEmail = InputValidationService.getValidEmail("Enter your email : ");
		String password = InputValidationService.getStringInput("Enter your password : ");
		System.out.println();
		return dbconnDao.checkUser(userEmail, password);
	}

	public void register() throws ClassNotFoundException, SQLException {
		User user = createuser(Role.Customer);
		dbconnDao.addUser(user);
		System.out.println("Registered Successfully.\n");
	}
	
	public void updateProfile(User user) throws ClassNotFoundException, SQLException {
		user.name = InputValidationService.getStringInput("Enter new name : ");
		user.phoneNumber = InputValidationService.getIntegerInput("Enter new phone number : ");
		dbconnDao.updateUser(user);
		System.out.println("Profile updated Successfully.\n");
	}

	public User createuser(Role role) throws ClassNotFoundException, SQLException {

		String email = InputValidationService.getValidEmail("Enter email : ");
		while (dbconnDao.isUserExist(email)) {
			System.out.println("This email already exist, try with another.\n");
			email = InputValidationService.getStringInput("Enter email : ");
		}
		String name = InputValidationService.getStringInput("Enter name : ");
		String password = InputValidationService.getStringInput("Enter password : ");
		int phoneNumber = InputValidationService.getIntegerInput("Enter contact number : ");
		return new User(name, password, role.getRoleId(), email, phoneNumber);
	}

	public void changePassword(User user) throws ClassNotFoundException, SQLException, UserNotifyException {
		String opassword = InputValidationService.getStringInput("Enter your old password : ");
		if (!EncryptionService.isPasswordMatch(opassword, user.password)) {
			throw new UserNotifyException("Invalid password!");
		}

		String npassword = InputValidationService.getStringInput("Enter your new password : ");
		user.password = npassword;
		dbconnDao.updateUserPassword(user);
		System.out.println("Password updated successfully\n");
		return;
	}

	public void viewInventory(int userId, int roleId) throws ClassNotFoundException, SQLException {
		ResultSet resultSet = dbconnDao.getInventory(userId, roleId);
		if(roleId != Role.Customer.getRoleId()) {
			System.out.println("Product Id Product Name\t Description\t Price\t Quantity\t"
					+ "CreatedBy\t ModifiedBy\t CreatedTime\t ModifiedTime\t ProductStatus");
		}
		else {
			System.out.println("Product Id Name\t Description\t Price\t Quantity");
		}

			while (resultSet.next()) {
				int pid = resultSet.getInt("productid");
				String name = resultSet.getString("productname");
				String description = resultSet.getString("description");
				double price = resultSet.getDouble("price");
				int quantity = resultSet.getInt("quantity");

				if (roleId != Role.Customer.getRoleId()) {
					String createdBy = resultSet.getString("createdByName");
					String modifiedBy = resultSet.getString("modifiedByName");
					Timestamp createdTime = resultSet.getTimestamp("createdTime");
					Timestamp modifiedTime = resultSet.getTimestamp("modifiedTime");
					String productStatus = resultSet.getString("productstatus");
					System.out.println(pid + "\t" + name + "\t" + description + "\t" + price + "\t" + quantity + "\t"
							+ createdBy + "\t" + modifiedBy + "\t" + createdTime + "\t" + modifiedTime+"\t" + productStatus);
				} else {
					System.out.println(pid + "\t" + name + "\t" + description + "\t" + price + "\t" + quantity);
				}
				System.out.println();
			}
			resultSet.close();
	}

	public void cancelOrder(int userId) throws SQLException, ClassNotFoundException, UserNotifyException {
		int orderId = InputValidationService.getIntegerInput("Enter order id : ");
		try(ResultSet orderSt = dbconnDao.getOrderFromId(orderId)){
			if(orderSt.next()) {
				String orderStatus = orderSt.getString("orderstatus");
				if(!orderStatus.equalsIgnoreCase(OrderStatus.Placed.toString())) {
					throw new UserNotifyException("Order already "+orderStatus);
				}
				
			}
			else {
				throw new UserNotifyException("Given order id does not exist.");
			}
		}

		double amount =dbconnDao.releaseProductsToInventory(orderId);
		Wallet wallet = dbconnDao.getWalletFromId(userId);
		dbconnDao.updateOrderStatus(orderId, OrderStatus.Cancelled);
		wallet.addToWalletBalance(amount);
		dbconnDao.updateWallet(wallet);
		System.out.println("Order with id " + orderId + " cancelled successfully.\n");
	}

}
