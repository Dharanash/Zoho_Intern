package online_shopping_system.controller;

import java.sql.SQLException;

import online_shopping_system.DAO.DbConnectionDao;
import online_shopping_system.POJO.User;
import online_shopping_system.enums.Role;
import online_shopping_system.services.AdminService;
import online_shopping_system.services.CustomerService;
import online_shopping_system.services.InputValidationService;
import online_shopping_system.services.ManagerService;
import online_shopping_system.services.UserService;

public class UserControl {

	private UserService userService;
	private AdminService adminService;
	private ManagerService managerService;
	private CustomerService customerService;

	public UserControl() {
		userService = new UserService();
		adminService = new AdminService();
		managerService = new ManagerService();
		customerService = new CustomerService();
	}

	public void validateUser() {
		System.out.println("Welcome to online shopping system\n");

		boolean exit = false;
		try {
			while (!exit) {

				int option = InputValidationService
						.getIntegerInput(" 1 - Login\n 2 - Register\n 3 - Exit\nEnter your option : ");
				switch (option) {
				case 1:
					User user = userService.login();
					if (user != null) {
						implementUserControl(user);
					} else {
						System.out.println("Invalid user email/password !\n");
					}
					break;
				case 2:
					userService.register();
					break;
				case 3:
					exit = true;
					break;
				default:
					System.out.println("Kindly enter from available options!\n");
					break;

				}
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void implementUserControl(User user) throws ClassNotFoundException {
		if (user.role == Role.Admin.getRoleId()) {
			implementAdminControl(user);
		} else if (user.role == Role.Manager.getRoleId()) {
			implementManagerControl(user);
		} else if (user.role == Role.Customer.getRoleId()) {
			implementCustomerControl(user);
		}

	}

	public void implementAdminControl(User user) throws ClassNotFoundException {
		boolean logout = false;
		while (!logout) {
			try {
				int userOption = InputValidationService.getIntegerInput(
						" 1 - Change Password\n 2 - View Orders\n 3 - Approve Order\n 4 - View Order Details\n 5 - Add Manager\n"
								+ " 6 - Add Products\n 7 - Update Products\n 8 - Remove Products\n 9 - View Inventory\n 10 - Update Profile\n 11 - Logout\nEnter your option : ");
				System.out.println();
				switch (userOption) {
				case 1:
					adminService.changePassword(user);
					break;
				case 2:
					adminService.viewAllOrders();
					break;
				case 3:
					adminService.approveOrder();
					break;
				case 4:
					adminService.viewOrderDetails();
					break;
				case 5:
					adminService.addManager();
					break;
				case 6:
					adminService.addProduct(user.userId);
					break;
				case 7:
					adminService.updateProduct(user.userId);
					break;
				case 8:
					adminService.removeProduct();
					break;
				case 9:
					adminService.viewInventory(user.role);
					break;
				case 10:
					adminService.updateProfile(user);
					break;
				case 11:
					logout = true;
					break;
				default:
					break;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void implementManagerControl(User user) throws ClassNotFoundException {
		boolean logout = false;
		while (!logout) {
			try {
				int userOption = InputValidationService.getIntegerInput(" 1 - Change Password\n"
						+ " 2 - Add Product\n 3 - Update Product\n 4 - Remove Product\n 5 - View Inventory\n 6 - Update Profile\n 7 - Logout\nEnter your option : ");
				System.out.println();
				switch (userOption) {
				case 1:
					adminService.changePassword(user);
					break;
				case 2:
					managerService.addProduct(user.userId);
					break;
				case 3:
					managerService.updateProduct(user.userId);
					break;
				case 4:
					managerService.removeProduct();
					break;
				case 5:
					managerService.viewInventory(user.role);
					break;
				case 6:
					managerService.updateProfile(user);
					break;
				case 7:
					logout = true;
					break;
				default:
					break;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void implementCustomerControl(User user) throws ClassNotFoundException {
		boolean logout = false;
		while (!logout) {
			try {
				int userOption = InputValidationService.getIntegerInput(
						"\n 1 - Change Password\n 2 - Update Profile\n 3 - View Product\n 4 - Add to cart\n"
								+ " 5 - Remove from cart\n 6 - Update cart\n 7 - View cart\n 8 - Purchase\n 9 - Cancel Order\n 10 - View purchase history\n"
								+ " 11 - View order details\n 12 - Add money\n 13 - View wallet\n 14 - Redeem wallet\n 15 - Add Delivery address\n 16 - Update delivery address\n"
								+ " 17 - Logout\nEnter your option : ");
				System.out.println();
				switch (userOption) {
				case 1:
					customerService.changePassword(user);
					break;
				case 2:
					customerService.updateProfile(user);
					break;
				case 3:
					customerService.viewInventory(user.role);
					break;
				case 4:
					customerService.addToCart(user.userId);
					break;
				case 5:
					customerService.removeFromCart(user.userId);
					break;
				case 6:
					customerService.updateCart(user.userId);
					break;
				case 7:
					customerService.viewCart(user.userId);
					break;
				case 8:
					customerService.purchase(user.userId);
					break;
				case 9:
					customerService.cancelOrder(user.userId);
					break;
				case 10:
					customerService.viewOrders(user.userId);
					break;
				case 11:
					customerService.viewOrderDetails();
					break;
				case 12:
					customerService.addMoneyToWallet(user.userId);
					break;
				case 13:
					customerService.viewWallet(user.userId);
					break;
				case 14:
					customerService.redeemPointsToWallet(user.userId);
					break;
				case 15:
					customerService.addCustomerDetails(user.userId);
					break;
				case 16:
					customerService.updateCustomerDetails();
					break;
				case 17:
					logout = true;
					break;
				default:
					break;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}
}
