import java.util.ArrayList;
import java.util.UUID;

public class Order {
    private String _orderId;
    public int totalAmount;
    public String orderStatus;
    public int createdTime;
    public ArrayList<Product> products;
    public Order(){
        _orderId = UUID.randomUUID().toString();
        products = new ArrayList<>();
        orderStatus= "Order Placed";
    }

    public String getOrderId(){
        return _orderId;
    }
}
