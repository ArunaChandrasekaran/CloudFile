
import java.util.LinkedList;
import java.util.Queue;

public class RestaurentOrders {
    Queue orders = new LinkedList();
    
    void addOrder(String orderItem)
    {
        orders.add(orderItem);
    }
    
    void serveOrder()
    {
        orders.remove();
    }
    
    void nextOrder()
    {
        System.out.println(orders.peek());
    }
        
    void displayAllOrders()
    {
        orders.forEach(order -> System.out.println(order));
    }
    
    
}
