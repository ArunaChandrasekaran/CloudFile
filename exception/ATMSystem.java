
package exception;

import java.util.Scanner;

public class ATMSystem 
{
    
    public static void main(String[] args) throws InsufficientBalanceException
    {
        
        Scanner sc = new Scanner(System.in);
        
        double balance = 10000;
        int option;
        
        do
        {
            System.out.println("1.Enter the amount");
            System.out.println("2.check balance");
            System.out.println("3.exit");
            System.out.println("choose anyone from the above");
             
            option = sc.nextInt();
            
            switch(option)
            {
                case 1:
                {
                            try
                            {
                                System.out.println("enter the withdraw amount");
                               double withdraw = sc.nextDouble();
                               if(withdraw > balance)
                               {
                                    throw new InsufficientBalanceException(
                                    "Insufficient Balance! Available Balance: ₹" + balance);
                               }
                               
                                balance = balance- withdraw;
                        System.out.println("Withdrawal Successful!");
                        System.out.println("Remaining Balance: ₹" + balance);
                             }catch (InsufficientBalanceException e) 
                             {
                        System.out.println("Exception: " + e.getMessage());
                             }
                }break;
               case 2:
               {
                   System.out.println("Current Balance: ₹" + balance); break;    
                    
               }
               case 3:
               {
                    System.out.println("Thank you for using ATM");
                    break;
               }

                default:
                {
                    System.out.println("Invalid Choice! Please try again.");
                }
                    
                    
            }
         }while(option!=3);
    
}
}
