
import java.util.Scanner;


public class Main 
{
    Scanner sc = new Scanner(System.in);
    public static void main(String[] args) 
    {
         Main m = new Main();
         m.menu();
        
    }
    void menu()
    {
        RestaurentOrders ordersObj = new RestaurentOrders();
        boolean isExit = true;
        
        do{
        System.out.println("1.Add Order");
        System.out.println("2.Serve Order");
        System.out.println("3.Next Order");
        System.out.println("4.Disply All Orders");
        System.out.println("5.Exit");
        
        System.out.print("Enter Anyone From the Above option:");
        int option = sc.nextInt();
        
        if (option==1)
        {
            ordersObj.addOrder(sc.next());
        }
        else if(option==2)
        {
            
            ordersObj.serveOrder();
        }
        else if(option==3)
        {
            ordersObj.nextOrder();
        }
        else if(option==4)
        {
            ordersObj.displayAllOrders();
        }
        else
        {
            System.out.println("PROGRAM EXITED....");
            isExit = false;
        }
        }while(isExit);
    }
}
