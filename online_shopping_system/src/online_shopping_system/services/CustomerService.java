package online_shopping_system.services;

import java.sql.ResultSet;
import java.sql.SQLException;

import online_shopping_system.POJO.Wallet;

public class CustomerService extends UserService {
	
	public void addCustomerDetails(int userId) throws ClassNotFoundException, SQLException {
		String address = InputValidationService.getStringInput("Enter your address : ");
		int pincode = InputValidationService.getIntegerInput("Enter your pincode : ");
		dbconnDao.addCustomerDetails(address, pincode, userId);
		System.out.println("Personal details added successfully.\n");
	}
	
	public void updateCustomerDetails() throws ClassNotFoundException, SQLException {
		int cdId = InputValidationService.getIntegerInput("Enter your address id : ");
		String address = InputValidationService.getStringInput("Enter your address : ");
		int pincode = InputValidationService.getIntegerInput("Enter your pincode : ");
		if(dbconnDao.updateCustomerDetails(address, pincode, cdId)==0) {
			System.out.println("Given personal details id doesn't exist.\n");
			return;
		}
		System.out.println("Personal details updated successfully.\n");
	}
	
	public void viewCustomerDetails(int userId) throws ClassNotFoundException, SQLException {
		ResultSet resultSet = dbconnDao.getCustomerDetails(userId);
		while(resultSet.next()) {
			int cdId = resultSet.getInt("customer_detailsid");
			String address = resultSet.getString("address");
			int pincode = resultSet.getInt("pincode");
			System.out.println(cdId + "\t" + address + "\t" + pincode);
		}
		resultSet.close();
	}

	public void updateCart(int userId) throws ClassNotFoundException, SQLException {
		int pid = InputValidationService.getIntegerInput("Enter the product id : ");
		if (!dbconnDao.isProductExistInInventory(pid) || !dbconnDao.isProductExistInCart(pid, userId)) {
			System.out.println("Given product does not exist.\n");
			return;
		}

		int quantity = InputValidationService.getIntegerInput("Enter the product quantity : ");
		dbconnDao.updateProductQuantityInCart(pid, userId, quantity);
		System.out.println("Product in cart updated successfully");
	}

	public void addToCart(int userId) throws ClassNotFoundException, SQLException {
		int pid = InputValidationService.getIntegerInput("Enter product id : ");
		if (!dbconnDao.isProductExistInInventory(pid)) {
			System.out.println("Given product does not exist.\n");
			return;
		} else if (dbconnDao.isProductExistInCart(pid, userId)) {
			System.out.println("Given product already exist in cart.\n");
			return;
		}

		int quantity = InputValidationService.getIntegerInput("Enter the quantity : ");
		dbconnDao.addToCart(userId, pid, quantity);

		System.out.println("Product successfully added to cart.\n");

	}

	public void removeFromCart(int userId) throws ClassNotFoundException, SQLException {
		int productid = InputValidationService.getIntegerInput("Enter the product id : ");
		if (!dbconnDao.isProductExistInCart(productid, userId)) {
			System.out.println("Given product does not exist in cart.\n");
			return;
		}

		dbconnDao.removeProductInCart(productid, userId);
		System.out.println("Product removed from cart successfully.\n");
	}

	public double viewCart(int userId) throws SQLException, ClassNotFoundException {
		System.out.println(" Customer Cart List\n");
		double totalAmount = 0;
		try(ResultSet resultSet = dbconnDao.getCartElements(userId)){
		
		while (resultSet.next()) {
			int pid = resultSet.getInt("productid");
			try(ResultSet productResult = dbconnDao.getProductFromId(pid)){
			if (!productResult.next()) {
				System.out.println(pid + "\t No Longer Available");
				continue;
			}

			String productName = productResult.getString("productname");
			double price = productResult.getDouble("price");
			int quantity = resultSet.getInt("quantity");
			totalAmount += price * quantity;
			System.out.println(pid + "\t" + productName + "\t" + price + "\t" + quantity);
			}
		}}

		System.out.println(" Total amount : " + totalAmount + "\n");
		return totalAmount;
	}

	public void purchase(int userid) throws SQLException, ClassNotFoundException {
		double totalAmount = viewCart(userid);
		if (!InputValidationService.getYesOrNoInput("Do you want to continue (yes or no) : ",
				"Kindly enter from available options !\n")) {
			return;
		}

		Wallet wallet = dbconnDao.getWalletFromId(userid);
		if (wallet.balance - totalAmount < 0) {
			System.out.println("You do not have sufficient amount to make this purchase!\n");
			return;
		}

		if (!checkValidPurchase(userid)) {
			System.out.println("Some products quantity is not available in stock, so check and update your cart accordingly.\n");
			return;
		}
		int cid = getCustomerDetailsId(userid);
		ResultSet customerDetailsSet = dbconnDao.getCustomerDetailsFromId(cid);
		if(!customerDetailsSet.next()) {
			System.out.println("Given details id doesn't exist.\n");
			return;
		}
		int orderId = dbconnDao.addOrder(userid, cid);
		dbconnDao.fillOrderDetailsFromCart(orderId, userid);
		dbconnDao.deduceProductQuantityFromInventory(userid);
		wallet.balance -= totalAmount;
		wallet.point += totalAmount / 10;
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
			int pid = resultSet.getInt("productid");
			int quantity = resultSet.getInt("quantity");
			ResultSet productSet = dbconnDao.getProductFromId(pid);
			if (!productSet.next()) {
				return false;
			}

			int pquantity = productSet.getInt("quantity");
			if (pquantity - quantity < 0) {
				return false;
			}
			productSet.close();
		}
		resultSet.close();
		
		return true;
	}

	public void viewOrders(int userId) throws SQLException, ClassNotFoundException {
		System.out.println(" Order History\n");
		ResultSet orders = dbconnDao.getOrders(userId);

		while (orders.next()) {
			int orderId = orders.getInt("orderid");
			String status = orders.getString("status");
			java.sql.Timestamp addedTime = orders.getTimestamp("addtime");

			System.out.println(orderId + "\t" + status + "\t" + addedTime);
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

	public void viewWallet(int userId) throws ClassNotFoundException {
		Wallet wallet = dbconnDao.getWalletFromId(userId);
		System.out.println("Balance : " + wallet.balance + "Points : " + wallet.point + "\n");
	}

	public void redeemPointsToWallet(int userId) throws ClassNotFoundException, SQLException {
		Wallet wallet = dbconnDao.getWalletFromId(userId);
		wallet.balance += wallet.point / 10;
		System.out.println("Total amount credited to wallet : " + wallet.point / 10);
		wallet.point = 0;
		dbconnDao.updateWallet(wallet);
		System.out.println("Balance : " + wallet.balance + "Points : " + wallet.point + "\n");
	}
}
