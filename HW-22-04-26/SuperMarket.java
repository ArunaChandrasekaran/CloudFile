import java.util.Scanner;
class SuperMarket
{
public static void main(String args[])
{
	Scanner sc = new Scanner(System.in);
	
	System.out.print("ENTER THE NUMBER OF ITEMS YOU PURCHASED: ");
	int items = sc.nextInt();
	
	int price [] = new int[items];
	int total=0;
	
	for(int i=0; i<price.length; i++)
		{
			System.out.print("ENTER THE PRICE OF "+i+" ITEM: ");
			price[i] = sc.nextInt();
			total = total+price[i];
		}
		
		int billAmount;
		
		if(total>5000)
		{
			billAmount = total-(total*20/100);
			System.out.println("TOTAL AMOUNT:"+total);
			System.out.println("DISCOUNT: "+(total*20/100));
			System.out.println("PRICE OF ITEMS: "+billAmount);
		}
		else if(total>2000)
		{
			billAmount = total-(total*10/100);
			System.out.println("TOTAL AMOUNT:"+total);
			System.out.println("DISCOUNT: "+(total*10/100));
			System.out.println("PRICE OF ITEMS: "+billAmount);
		}
		else
		{
			System.out.print("YOU ARE NOT APPLICABLE FOR DISCOUNT, KINDLY PURCHASE "+(2000-total)+" MORE...");
		}
	}
}