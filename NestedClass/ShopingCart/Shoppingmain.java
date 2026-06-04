
package ShopingCart;

import java.util.Arrays;
import java.util.Scanner;


public class Shoppingmain 
{
    static Scanner sc = new Scanner(System.in);
    static int n =0;
    static ShoppingCart.Item item = new ShoppingCart().new Item();
    public static void main(String[] args) 
    {
        
        
        menu();
    }
    static void menu()
    {
        System.out.print("ENTER THE NAME: ");
        
        ShoppingCart.cartOwnerName = sc.next();
         boolean isExit = true;
        do{
        System.out.println("1.ADD ITEM");
        System.out.println("2.UPDATE PRICE HISTORY");
        System.out.println("3.CALCULATE TOTAL BILL");
        System.out.println("4.DISPLAY CART DETAILS");
        System.out.println("5.EXIT");
        
        System.out.print("CHOOSE ANYONE FROM THE ABOVE:");
        int choice = sc.nextInt();
        
        if(choice == 1)
        {
            addItem();
        }
        else if(choice ==2)
        {
            updatePriceHistory();
        }
        else if(choice ==3)
        {
            calculateBill();
        }
        else if(choice == 4)
        {
            display();
        }else
        {
            System.out.println("EXITED....");
            isExit = false;
        }
        }while(isExit);
        
    }
    
    static void addItem()
     {
         System.out.print("enter the item name: ");
         item.itemName = sc.next();
         System.out.println("enter the quantity:");
         item.quantity = sc.nextInt();
         System.out.print("Enter Number of Price Updates: ");
            n = sc.nextInt();
          item.priceHistory = new double[n];
        
      }
    static void updatePriceHistory()
    {
        for(int i = 0; i<n;i++)
        {
            System.out.println("ENTER THE "+i+"updated price");
            item.priceHistory[i] = sc.nextDouble();
        }
        item.price = (int) item.priceHistory[n-1];
        System.out.println("price history stored successfully");
    }
    static void calculateBill()
    {
        System.out.println("total bill :"+item.price*item.quantity);
    }
    static void display()
    {
        System.out.println("Cart Owner: "+ ShoppingCart.cartOwnerName);
        System.out.println("Item Name: "+item.itemName);
        System.out.println("Quantity: "+item.quantity);
        System.out.println(Arrays.toString(item.priceHistory));
    }
        
}
