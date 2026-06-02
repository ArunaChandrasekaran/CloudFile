
package exception;

import java.util.Scanner;


public class SmartLoginSystem 
{
    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);
        
        String username="aruna";
        String password ="123";
        
        int attempt =3;
        int choice;
        
        do
        {
            System.out.println("Login menu");
            System.out.println("1.Login");
            System.out.println("2.exit");
            System.out.println("enter your choice");
            
            choice = sc.nextInt();
            
            switch(choice)
            {
                case 1:
                
                    try{
                
                    while(attempt>0)
                    {
                    System.out.print("enter your username: ");
                    String name = sc.next();
                    System.out.println("enter your password");
                    String secret = sc.next();
                    
                    if(name.equals(username)&&secret.equals(password))
                    {
                        System.out.println("login successful!");
                        attempt =3;
                    }
                    else
                    {
                        attempt--;
                        if(attempt==0)
                        {
                            throw new  AccountLockedException(
                                    "accout locked maximum login count exceeded");
                        }
                        System.out.println("invalid credentials");
                        System.out.println("remaining attempt: "+attempt );
                    }
                        }
                    
                    } catch(AccountLockedException e)
                    {
                        System.out.println(e.getMessage());
                    }break;
                    
                case 2:
                    System.out.println("exiting system..."); break;
                    
                default:
                    System.out.println("invalid choice please try again"); break;
        }
    }while(choice !=2);
}
}
