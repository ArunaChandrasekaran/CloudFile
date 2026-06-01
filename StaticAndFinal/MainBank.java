
package StaticAndFinal;

import java.util.Scanner;

public class MainBank 
{
    static Scanner sc = new Scanner(System.in);
    public static void main(String[] args)
    {
        BankInterestCalculator bc = new BankInterestCalculator();
        
        System.out.print("ENTER THE PRINCIPAL AMOUNT: ");
        double principal = sc.nextDouble();
        
        System.out.println("NUMBER OF YEARS: ");
        int year = sc.nextInt();
        
        BankInterestCalculator.calculateInterest(principal, year);
        
        
        
    }
    
}
