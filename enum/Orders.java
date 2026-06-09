
package enumpractice;


import java.util.Scanner;


public enum Orders {
    
        PLACED,
        SHIPPED,
        DELIVERED,
        CANCELLED;
        
        public static final Orders orderStatus = PLACED;
    
    public static void main(String args[])
    {
        
       Scanner sc = new Scanner(System.in);
       while(true){ 
        System.out.println("1.SHOW ORDER STATUS");
        System.out.println("2.CHECK IF ORDER IS COMPLETED");
        System.out.println("EXIT");
        System.out.println("CHOOSE ANYONE FROM THE ABOVE: ");
        int option = sc.nextInt();
        
        if(option ==1)
        {
            System.out.println(Orders.orderStatus.name());
        }
        else if(option==2)
        {
         
            boolean flag = Orders.orderStatus == Orders.DELIVERED;
            System.out.println(flag);
       
        }
        else
        {
            System.out.println("EXITED....");
            System.exit(0);
        }
    }
    }
    
}
