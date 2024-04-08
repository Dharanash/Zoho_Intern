import java.util.HashMap;

public class UserControl {

    public void validateUser(){
        System.out.println("Welcome to online shopping system\n");
        Database database = new Database();
        UserServices userServices = new UserServices();

        boolean exit = false;
        while(!exit){
            int option = InputValidationService.getIntegerInput(" 1 - Login\n 2 - Register\n 3 - Exit\nEnter your option : ");
            switch (option) {
                case 1:
                String userName = InputValidationService.getStringInput("Enter your user name : ");
                String password = InputValidationService.getStringInput("Enter your password : ");
                System.out.println();
                Account account = checkUser(database.accounts, userName, password);
                if(account != null){
                    implementUserControl(database, account, userServices);
                }
                    break;
                case 2:
                    register(database.accounts, userServices);
                    break;
                case 3:
                    exit=true;
                    break;
                default:
                    System.out.println("Kindly enter from available options!\n");
                    break;
            }
        }
    }

    public void implementUserControl(Database database, Account account, UserServices userServices){
        if(account.role=="admin"){
            implementAdminControl(database, account, userServices);
        }
        else if (account.role == "manager"){
            implementManagerControl(database, account, userServices);
        }
        else if(account.role=="customer"){
            Customer customer = (Customer) account;
            implementCustomerControl(database, customer, userServices);
        }
        
    }

    public void implementAdminControl(Database database, Account account, UserServices userServices){
        boolean logout = false;
        while(!logout){
            int userOption= InputValidationService.getIntegerInput(" 1 - Change Password\n 2 - Update Profile\n 3 - Create Manager\n 4 - Update Manager\n"+
             " 5 - Remove Manager\n 6 - Add Invertory\n 7 - Update Invertory\n 8 - Remove Invertory\n 9 - View Inventory\n 10 - Logout\nEnter your option : ");
             System.out.println();
            switch (userOption) {
                case 1:
                userServices.changePassword(account);
                    break;
                case 2:
                userServices.updateProfile(account);
                    break;
                case 3:
                userServices.createManager(database.accounts, account);
                    break;
                case 6:
                userServices.addInventory(database.inventory, account);
                    break;
                case 7:
                userServices.updateInventory(database.inventory, account);
                    break;
                case 8:
                userServices.removeInventory(database.inventory, account);
                    break;
                case 9:
                userServices.viewInventory(database.inventory);
                    break;
                case 10:
                    logout=true;
                    break;
                default:
                    break;
            }
        }
    }

    public void implementManagerControl(Database database, Account account, UserServices userServices){
        boolean logout = false;
        while(!logout){
            int userOption= InputValidationService.getIntegerInput(" 1 - Change Password\n 2 - Update Profile\n"+
             " 3 - Add Invertory\n 4 - Update Invertory\n 5 - Remove Invertory\n 6 - View Inventory\n 7 - Logout\nEnter your option : ");
            System.out.println();
             switch (userOption) {
                case 1:
                    userServices.changePassword(account);
                    break;
                case 2:
                    userServices.updateProfile(account);
                    break;
                case 3:
                userServices.addInventory(database.inventory, account);
                    break;
                case 4:
                userServices.updateInventory(database.inventory, account);
                    break;
                case 5:
                userServices.removeInventory(database.inventory, account);
                    break;
                case 6:
                userServices.viewInventory(database.inventory);
                    break;
                case 7:
                    logout=true;
                    break;
                default:
                    break;
            }
        }
    }

    public void implementCustomerControl(Database database, Customer customer, UserServices userServices){
        boolean logout = false;
        while(!logout){
            int userOption= InputValidationService.getIntegerInput("\n 1 - Change Password\n 2 - Update Profile\n 3 - View Product\n 4 - Add to cart\n"+
             " 5 - Remove from cart\n 6 - View cart\n 7 - Purchase\n 8 - Cancel Order\n 9 - View purchase history\n 10 - Add money\n 11 - View wallet\n 12 - Redeem wallet\n 13 - Logout\nEnter your option : ");
            System.out.println();
             switch (userOption) {
                case 1:
                userServices.changePassword(customer);
                    break;
                case 2:
                userServices.updateProfile(customer);
                    break;
                case 3:
                userServices.viewProducts(database.inventory);
                    break;
                case 4:
                userServices.addToCart(database.inventory, customer);
                    break;
                case 5:
                userServices.removeFromCart(customer.cart, customer);
                    break;
                case 6:
                userServices.viewCart(customer.cart);
                    break;
                case 7:
                userServices.purchase(database.inventory, customer.cart, customer);
                    break;
                case 8:
                userServices.cancelOrder(database.inventory, customer);
                    break;
                case 9:
                userServices.viewPurchase(customer.orders);
                    break;
                case 10:
                userServices.addMoneyToWallet(customer);
                    break;
                case 11:
                userServices.viewWallet(customer);
                    break;
                case 13:
                    logout=true;
                    break;
                default:
                    break;
            }
        }

    }

    public Account checkUser(HashMap<String, Account> account, String userName, String password){
        if(account.containsKey(userName)){
            if(Utiliy.isPasswordCrt(password, account.get(userName).password)){
                return account.get(userName);
            }
        }
        System.out.println("Invalid user name / password\n");
        return null;
    }

    
    public Customer register(HashMap<String, Account> accounts, UserServices userServices){
        Customer customer = new Customer("customer");
        userServices.createAccount(accounts, customer, "customer", "customer");
        accounts.put(customer.userName, customer);
        System.out.println("User Registered succesfully\n");
        return customer;
    }

}
