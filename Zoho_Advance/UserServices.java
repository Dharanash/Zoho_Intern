import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserServices {

    public void changePassword(Account account){
        System.out.println();
        String opassword = InputValidationService.getStringInput("Enter your old password : ");
        if(Utiliy.isPasswordCrt(opassword, account.password)){
            String npassword = InputValidationService.getStringInput("Enter your new password : ");
            account.password = Utiliy.hashPassword(npassword);
            System.out.println("Password updated successfully\n");
            return;
        }

        System.out.println("Invalid Password\n");
        return;
    }

    public void updateProfile(Account account){
        account.name=InputValidationService.getStringInput("Enter name : ");
        account.email=InputValidationService.getStringInput("Enter email : ");
        account.phoneNumber=InputValidationService.getStringInput("Enter phonenumber : ");
        System.out.println("Profile updated successfully\n");
    }

    public void createManager(HashMap<String, Account> accounts, Account account ){
        Account manager = new Account("manager");
        createAccount(accounts, manager, account.role, account.role);
        accounts.put(manager.userName, manager);
        System.out.println("Manager created successfully\n");
    }

    public Account createAccount(HashMap<String, Account> accounts, Account account, String createdBy, String modefiedBy){
        account.name=InputValidationService.getStringInput("Enter name : ");
        String userName =InputValidationService.getStringInput("Enter user name : ");
        while(accounts.containsKey(userName)){
            System.out.println("This user name already exist, try with another.\n");
            userName = InputValidationService.getStringInput("Enter user name : ");
        }
        account.userName = userName;
        account.password=Utiliy.hashPassword(InputValidationService.getStringInput("Enter password : "));
        account.email=InputValidationService.getStringInput("Enter email : ");
        account.phoneNumber=InputValidationService.getStringInput("Enter phonenumber : ");
        account.createdBy = createdBy;
        account.modefiedBy = modefiedBy;
        return account;
    }

    public void addInventory(HashMap<String, Product> inventory, Account account){
        Product product = createProduct(account.name, account.name);
        inventory.put(product.name, product);
        System.out.println("Inventory added successfully\n");
    } 

    public void updateInventory(HashMap<String, Product> inventory, Account account){
        String pname = InputValidationService.getStringInput("Enter the product name : ");
        if(!inventory.containsKey(pname)){
            System.out.println("Given product does not exist.\n");
            return;
        }

        Product product = inventory.get(pname);
        product.description = InputValidationService.getStringInput("Enter description : ");
        product.price = InputValidationService.getIntegerInput("Enter price : ");
        product.quantity = InputValidationService.getIntegerInput("Enter quantity : ");
        product.modifiedBy = account.name;

        System.out.println("Inventory updated successfully\n");
    }

    public void removeInventory(HashMap<String, Product> inventory, Account account){
        String pname = InputValidationService.getStringInput("Enter the product name : ");
        if(!inventory.containsKey(pname)){
            System.out.println("Given product does not exist.\n");
            return;
        }

        inventory.remove(pname);

        System.out.println("Inventory removed successfully\n");
    }

    public void viewInventory(HashMap<String, Product> inventory){
        for(Map.Entry<String, Product> m: inventory.entrySet()){
            System.out.println(m.getValue().name+" "+m.getValue().price+" "+m.getValue().quantity+" "+m.getValue().createdBy+" "+m.getValue().modifiedBy);
        }
        System.out.println();
    }

    public Product createProduct(String createdBy, String modefiedBy){
        String name=InputValidationService.getStringInput("Enter name : ");
        String description=InputValidationService.getStringInput("Enter description : ");
        int price=InputValidationService.getIntegerInput("Enter price : ");
        int quantity=InputValidationService.getIntegerInput("Enter quantity : ");
        Product product = new Product(name, description, price, quantity, createdBy, modefiedBy);

        return product;
    }


    public void viewProducts(HashMap<String, Product> products){
        System.out.println(" Available Product List\n");
        for(Map.Entry<String, Product> m: products.entrySet()){
            System.out.println(m.getValue().name+" "+m.getValue().description+" "+m.getValue().price+" "+m.getValue().quantity+" ");
        }
    }

    public void addToCart(HashMap<String, Product> products, Customer customer){
        String pname=InputValidationService.getStringInput("Enter product name : ");
        if(!products.containsKey(pname)){
            System.out.println("Given product does not exist.\n");
            return;
        }

        int quantity = InputValidationService.getIntegerInput("Enter the quantity : ");
        Product productInStock = products.get(pname);
        if(productInStock.quantity-quantity<0){
            System.out.println("Given quantity is not available in stock.\n");
            return;
        }

        Product userProduct = new Product(pname, productInStock.description, productInStock.price, quantity, customer.name, customer.name);
        customer.cart.put(pname, userProduct);
        System.out.println("Product successfully added to cart.\n");
        
    }

    public void removeFromCart(HashMap<String, Product> cart, Customer customer){
        String pname = InputValidationService.getStringInput("Enter the product name : ");
        if(!cart.containsKey(pname)){
            System.out.println("Given product does not exist in cart.\n");
            return;
        }

        cart.remove(pname);

        System.out.println("Product removed from cart successfully.\n");
    } 

    public void viewCart(HashMap<String, Product> cart){
        System.out.println(" Customer Cart List\n");
        for(Map.Entry<String, Product> m: cart.entrySet()){
            System.out.println(m.getValue().name+" "+m.getValue().description+" "+m.getValue().price+" "+m.getValue().quantity+" ");
        }
        System.out.println();
    }

    public void purchase(HashMap<String, Product> products, HashMap<String, Product> cart, Customer customer){
        int totalAmount = Utiliy.getTotalAmount(cart);
        System.out.println(" Total amount for this purchase : INR "+totalAmount+"\n");
        if(!InputValidationService.getYesOrNoInput("Do you want to continue (yes or no) : ", "Kindly enter from available options !\n")){
            return;
        }

        if(customer.wallet-totalAmount<0){
            System.out.println("You do not have sufficient amount to make this purchase!\n");
            return;
        }

        if(!checkValidPurchase(products, cart)){
            System.out.println("Some products become out of stock, so check and update your purchase accordingly.\n");
            return;
        }
        addFromInventory(products, cart);
        customer.wallet -= totalAmount;
        Order order = new Order();
        order.products =  new ArrayList<>(cart.values());
        cart.clear();
        order.totalAmount = totalAmount;
        customer.orders.put(order.getOrderId(), order);
        System.out.println("Order placed successfully.\n");
    }

    public void cancelOrder(HashMap<String, Product> products, Customer customer){
        String oderId = InputValidationService.getStringInput("Enter the order id to cancel : ");
        if(!customer.orders.containsKey(oderId)){
            System.out.println("Given order ID does not exist.\n");
            return;
        }

        Order order = customer.orders.get(oderId);
        releaseProducts(products, order.products);
        customer.wallet += order.totalAmount;
        order.orderStatus = "Cancelled";

        System.out.println("Order cancelled successfully.\n");
    }

    public void releaseProducts(HashMap<String, Product> inventory, ArrayList<Product> products){
        for(Product p : products){
            if(inventory.containsKey(p.name)){
                inventory.get(p.name).quantity+=p.quantity;
            }
        }
    }

    public void addFromInventory(HashMap<String, Product> inventory, HashMap<String, Product> cart){
        for(Map.Entry<String, Product> p: cart.entrySet()){
            if(inventory.containsKey(p.getKey())){
                inventory.get(p.getKey()).quantity-=p.getValue().quantity;
            }
        }
    }

    public boolean checkValidPurchase(HashMap<String, Product> inventory, HashMap<String, Product> cart){
        for(Map.Entry<String, Product> m: cart.entrySet()){
            if(inventory.containsKey(m.getKey())){
                if(inventory.get(m.getKey()).quantity-m.getValue().quantity<0){
                    return false;
                }
            }
        }
        return true;
    }

    public void viewPurchase(HashMap<String, Order> orders){
        System.out.println(" Order History\n");
        for(Map.Entry<String, Order> m: orders.entrySet()){
            System.out.println(m.getKey()+" "+m.getValue().products.size()+" "+m.getValue().totalAmount+" "+m.getValue().orderStatus+" ");
        }
        System.out.println();
    }

    public void addMoneyToWallet(Customer customer){
        int amount = InputValidationService.getIntegerInput("Enter the amount to add in wallet : ");
        customer.wallet += amount;

        System.out.println("Amount added to wallet successfully.\n");
    }

    public void viewWallet(Customer customer){
        System.out.println("Total balance amount in wallet : "+customer.wallet+"\n");
    }

}
