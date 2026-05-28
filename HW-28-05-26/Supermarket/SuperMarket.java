
import java.util.Stack;

public class SuperMarket 
{
    Stack billingItems = new Stack();
    
    
    void insert(String data)
    {
        billingItems.push(data);
    }
    void delete()
    {
        System.out.println(billingItems.pop()+" is deleted");
        billingItems.pop();
    }
    void viewLast()
    {
        System.out.println(billingItems.peek());
    }
    void display()
    {
        for(int i =0; i<billingItems.size();i++)
        {
            System.out.println(billingItems.get(i));
        }
    }
    
    
}
