package online_shopping_system.services;

import java.sql.ResultSet;
import java.sql.SQLException;

import online_shopping_system.DAO.DbConnectionDao;
import online_shopping_system.POJO.Product;
import online_shopping_system.POJO.User;
import online_shopping_system.POJO.Wallet;
import online_shopping_system.enums.OrderStatus;
import online_shopping_system.enums.Role;

public class UserService {
	protected static DbConnectionDao dbconnDao=new DbConnectionDao();

	public User login() throws ClassNotFoundException, SQLException {
		String userEmail = InputValidationService.getStringInput("Enter your email : ");
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

		String email = InputValidationService.getStringInput("Enter email : ");
		while (dbconnDao.isUserExist(email)) {
			System.out.println("This email already exist, try with another.\n");
			email = InputValidationService.getStringInput("Enter email : ");
		}
		String name = InputValidationService.getStringInput("Enter name : ");
		String password = InputValidationService.getStringInput("Enter password : ");
		int phoneNumber = InputValidationService.getIntegerInput("Enter contact number : ");
		return new User(name, password, role.getRoleId(), email, phoneNumber);
	}

	public void changePassword(User user) throws ClassNotFoundException, SQLException {
		String opassword = InputValidationService.getStringInput("Enter your old password : ");
		if (EncryptionService.isPasswordMatch(opassword, user.password)) {
			String npassword = InputValidationService.getStringInput("Enter your new password : ");
			user.password = npassword;
			dbconnDao.updateUserPassword(user);
			System.out.println("Password updated successfully\n");
			return;
		}

		System.out.println("Invalid Password\n");
		return;
	}

	public void viewInventory(int roleId) throws ClassNotFoundException, SQLException {
		ResultSet resultSet = dbconnDao.getInventory(roleId);

			while (resultSet.next()) {
				int pid = resultSet.getInt("productid");
				String name = resultSet.getString("productname");
				String description = resultSet.getString("description");
				double price = resultSet.getDouble("price");
				int quantity = resultSet.getInt("quantity");

				if (roleId != Role.Customer.getRoleId()) {
					String createdBy = dbconnDao.getUserName(resultSet.getInt("createdBy"));
					String modifiedBy = dbconnDao.getUserName(resultSet.getInt("modifiedBy"));
					java.sql.Timestamp createdTime = resultSet.getTimestamp("createdTime");
					java.sql.Timestamp modifiedTime = resultSet.getTimestamp("modifiedTime");

					System.out.println(pid + "\t" + name + "\t" + description + "\t" + price + "\t" + quantity + "\t"
							+ createdBy + "\t" + modifiedBy + "\t" + createdTime + "\t" + modifiedTime);
				} else {
					System.out.println(pid + "\t" + name + "\t" + description + "\t" + price + "\t" + quantity);
				}
				System.out.println();
			}
			resultSet.close();
	}

	public void viewOrderDetails() throws SQLException, ClassNotFoundException {
		int orderId = InputValidationService.getIntegerInput("Enter order id : ");
		if (!dbconnDao.isOrderExist(orderId)) {
			System.out.println("Given order id does not exist.");
			return;
		}
		System.out.println(" Order Details\n");
		double totalAmount = 0;
		try (ResultSet orderdetails = dbconnDao.getOrderDetails(orderId)) {

			while (orderdetails.next()) {
				int pid = orderdetails.getInt("productid");
				try (ResultSet productResult = dbconnDao.getProductFromId(pid)) {
					String productName = "No Longer Available";
					if (productResult.next()) {
						productName = productResult.getString("productname");
					}

					double price = orderdetails.getDouble("productprice");
					int quantity = orderdetails.getInt("quantity");
					totalAmount += price * quantity;
					System.out.println(productName + "\t" + price + "\t" + quantity);
				}
			}
		}
		System.out.println("Orders total amount : " + totalAmount + "\n");
	}

	public void cancelOrder(int userId) throws SQLException, ClassNotFoundException {
		int orderId = InputValidationService.getIntegerInput("Enter order id : ");
		try(ResultSet orderSt = dbconnDao.getOrderFromId(orderId)){
			if(orderSt.next()) {
				String orderStatus = orderSt.getString("status");
				if(orderStatus.equalsIgnoreCase(OrderStatus.Cancelled.toString())) {
					System.out.println("Order already cancelled.");
					return;
				}
				
			}
			else {
				System.out.println("Given order id does not exist.");
				return;
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
