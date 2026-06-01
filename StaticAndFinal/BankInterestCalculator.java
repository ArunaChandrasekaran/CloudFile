
package StaticAndFinal;
 
 
public class BankInterestCalculator
{
    
    static final String BANK_NAME = "SBI BANK";
    static final float INTEREST_RATE = 7.5f;
    
    static void calculateInterest(double principal,int years)
    {
        double interest;
        
        interest = (principal*INTEREST_RATE*years)/100;
        System.out.println("BANK NAME: "+ BANK_NAME);
        System.out.println("PRINCIPAL AMOUNT: "+principal);
        System.out.println("years: "+years);
        System.out.println("INTEREST AMOUNT: "+interest);
        
        
    }
    
    
}
