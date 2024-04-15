package online_shopping_system.services;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import online_shopping_system.Exceptions.UserNotifyException;
import online_shopping_system.POJO.Wallet;
import online_shopping_system.enums.ProductStatus;

public class CustomerService extends UserService {
	
	public void addCustomerDetails(int userId) throws ClassNotFoundException, SQLException {
		String address = InputValidationService.getStringInput("Enter your address : ");
		int pincode = InputValidationService.getIntegerInput("Enter your pincode : ");
		dbconnDao.addCustomerDetails(address, pincode, userId);
		System.out.println("Personal details added successfully.\n");
	}
	
	public void updateCustomerDetails() throws ClassNotFoundException, SQLException, UserNotifyException {
		int cdId = InputValidationService.getIntegerInput("Enter your address id : ");
		String address = InputValidationService.getStringInput("Enter your address : ");
		int pincode = InputValidationService.getIntegerInput("Enter your pincode : ");
		if(dbconnDao.updateCustomerDetails(address, pincode, cdId)==0) {
			throw new UserNotifyException("Given personal details id doesn't exist.\n");
		}
		System.out.println("Personal details updated successfully.\n");
	}
	
	public void viewCustomerDetails(int userId) throws ClassNotFoundException, SQLException {
		ResultSet resultSet = dbconnDao.getCustomerDetails(userId);
		System.out.println("\nDetails id Address\t Pincode");
		while(resultSet.next()) {
			int cdId = resultSet.getInt("customer_detailsid");
			String address = resultSet.getString("address");
			int pincode = resultSet.getInt("pincode");
			System.out.println(cdId + "\t" + address + "\t" + pincode);
		}
		resultSet.close();
	}

	public void updateCart(int userId) throws ClassNotFoundException, SQLException, UserNotifyException {
		int pid = InputValidationService.getIntegerInput("Enter the product id : ");
		if (!dbconnDao.isProductExistInInventory(pid) || !dbconnDao.isProductExistInCart(pid, userId)) {
			throw new UserNotifyException("Given product does not exist.\n");
		}

		int quantity = InputValidationService.getIntegerInput("Enter the product quantity : ");
		dbconnDao.updateProductQuantityInCart(pid, userId, quantity);
		System.out.println("Product in cart updated successfully");
	}

	public void addToCart(int userId) throws ClassNotFoundException, SQLException, UserNotifyException {
		int pid = InputValidationService.getIntegerInput("Enter product id : ");
		if (!dbconnDao.isProductExistInInventory(pid)) {
			throw new UserNotifyException("Given product does not exist.\n");
		} else if (dbconnDao.isProductExistInCart(pid, userId)) {
			throw new UserNotifyException("Given product already exist in cart.\n");
		}

		int quantity = InputValidationService.getIntegerInput("Enter the quantity : ");
		dbconnDao.addToCart(userId, pid, quantity);

		System.out.println("Product successfully added to cart.\n");

	}

	public void removeFromCart(int userId) throws ClassNotFoundException, SQLException, UserNotifyException {
		int productid = InputValidationService.getIntegerInput("Enter the product id : ");
		if (!dbconnDao.isProductExistInCart(productid, userId)) {
			throw new UserNotifyException("Given product does not exist in cart.\n");
		}

		dbconnDao.removeProductInCart(productid, userId);
		System.out.println("Product removed from cart successfully.\n");
	}

	public double viewCart(int userId) throws SQLException, ClassNotFoundException {
		System.out.println(" Customer Cart List\n");
		double totalAmount = 0;
		System.out.println("Cart id Product Name\t Price\t Quantity\t Product Status");
		try(ResultSet resultSet = dbconnDao.getCartElements(userId)){
		
		while (resultSet.next()) {
			int pid = resultSet.getInt("productid");

			String productName = resultSet.getString("productname");
			double price = resultSet.getDouble("price");
			int quantity = resultSet.getInt("quantity");
			String productStatus= resultSet.getString("productstatus");
			totalAmount += price * quantity;
			System.out.println(pid + "\t" + productName + "\t" + price + "\t" + quantity +"\t"+productStatus);
			}
		}

		System.out.println(" Total amount : " + totalAmount + "\n");
		return totalAmount;
	}

	public void purchase(int userId) throws SQLException, ClassNotFoundException, UserNotifyException {
		double totalAmount = viewCart(userId);
		if (!InputValidationService.getYesOrNoInput("Do you want to continue (yes or no) : ",
				"Kindly enter from available options !\n")) {
			return;
		}

		Wallet wallet = dbconnDao.getWalletFromId(userId);
		if (wallet.balance - totalAmount < 0) {
			throw new UserNotifyException("You do not have sufficient amount to make this purchase!\n");
		}

		if (!checkValidPurchase(userId)) {
			throw new UserNotifyException("Some products quantity is not available in stock, so check and update your cart accordingly.\n");
		}
		int cid = getCustomerDetailsId(userId);
		ResultSet customerDetailsSet = dbconnDao.getCustomerDetailsFromId(cid, userId);
		if(!customerDetailsSet.next()) {
			throw new UserNotifyException("Given details id doesn't exist.\n");
		}
		dbconnDao.addOrder(userId, cid);
		dbconnDao.deduceProductQuantityFromInventory(userId);
		wallet.balance -= totalAmount;
		dbconnDao.updateWallet(wallet);
		System.out.println("Order placed successfully.\n");
	}
	
	public int getCustomerDetailsId(int userId) throws ClassNotFoundException, SQLException {
		viewCustomerDetails(userId);
		int cid = InputValidationService.getIntegerInput("Enter your details id : ");
		return cid;
	}

	public boolean checkValidPurchase(int userId) throws SQLException, ClassNotFoundException {
		ResultSet resultSet = dbconnDao.getCartElements(userId);
		
		while (resultSet.next()) {
			if(!resultSet.getString("productstatus").equals(ProductStatus.Available.toString())) {
				return false;
			}
			int quantity = resultSet.getInt("quantity");

			int pquantity = resultSet.getInt("productquantity");
			if (pquantity - quantity < 0) {
				return false;
			}
			
		}
		resultSet.close();
		
		return true;
	}

	public void viewOrders(int userId) throws SQLException, ClassNotFoundException {
		System.out.println(" Order History\n");
		ResultSet orders = dbconnDao.getUserOrders(userId);
		System.out.println("Order Id Delivery Address\t Pincode\t" 
				+"Product Name\t Quantity Price   AddedTime\t Order Status");
		while (orders.next()) {
			int orderId = orders.getInt("orderid");
			String deliveryAddress = orders.getString("address");
			int pincode = orders.getInt("pincode");
			String productName = orders.getString("productname");
			int quantity = orders.getInt("quantity");
			double price = orders.getDouble("price");
			Timestamp addedTime = orders.getTimestamp("addtime");
			String orderStatus = orders.getString("orderstatus");
			System.out.println(orderId + "\t" +deliveryAddress + "\t" +pincode + "\t" 
			+productName + "\t" +quantity + "\t" + price +"\t" + addedTime + "\t" + orderStatus);
			
		}
		orders.close();
		System.out.println();
	}

	public void addMoneyToWallet(int userId) throws ClassNotFoundException, SQLException {
		int amount = InputValidationService.getIntegerInput("Enter the amount to add in wallet : ");
		Wallet wallet = dbconnDao.getWalletFromId(userId);
		wallet.balance += amount;
		dbconnDao.updateWallet(wallet);

		System.out.println("Amount added to wallet successfully.\n");
	}

	public void viewWallet(int userId) throws ClassNotFoundException, SQLException {
		Wallet wallet = dbconnDao.getWalletFromId(userId);
		System.out.println("Balance : " + wallet.balance + " Points : " + wallet.point + "\n");
	}

	public void redeemPointsToWallet(int userId) throws ClassNotFoundException, SQLException {
		Wallet wallet = dbconnDao.getWalletFromId(userId);
		wallet.balance += wallet.point / 10;
		System.out.println("Total amount credited to wallet : " + wallet.point / 10);
		wallet.point = 0;
		dbconnDao.updateWallet(wallet);
		System.out.println("Balance : " + wallet.balance + " Points : " + wallet.point + "\n");
	}
}
