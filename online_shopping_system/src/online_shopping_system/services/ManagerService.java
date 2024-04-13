package online_shopping_system.services;

import java.sql.SQLException;

import online_shopping_system.POJO.Product;

public class ManagerService extends UserService {

	public void addProduct(int userId) throws ClassNotFoundException, SQLException {
		Product product = createProduct(userId);
		dbconnDao.addProduct(product);
		System.out.println("Product added successfully\n");
	}

	public Product createProduct(int createdBy) {
		String name = InputValidationService.getStringInput("Enter product name : ");
		String description = InputValidationService.getStringInput("Enter description : ");
		double price = InputValidationService.getDoubleInput("Enter price : ");
		int quantity = InputValidationService.getIntegerInput("Enter quantity : ");
		Product product = new Product(name, description, price, quantity, createdBy);

		return product;
	}

	public void updateProduct(int userid) throws ClassNotFoundException, SQLException {
		int pid = InputValidationService.getIntegerInput("Enter the product id : ");
		if (!dbconnDao.isProductExistInInventory(pid)) {
			System.out.println("Given product does not exist.\n");
			return;
		}

		Product product = createProduct(userid);
		dbconnDao.updateProduct(pid, product);

		System.out.println("Product updated successfully\n");
	}

	public void removeProduct() throws ClassNotFoundException, SQLException {
		int pid = InputValidationService.getIntegerInput("Enter the product id : ");
		if (!dbconnDao.isProductExistInInventory(pid)) {
			System.out.println("Given product does not exist.\n");
			return;
		}

		dbconnDao.removeProductInInventory(pid);

		System.out.println("Product with id : " + pid + " removed successfully\n");
	}

}
