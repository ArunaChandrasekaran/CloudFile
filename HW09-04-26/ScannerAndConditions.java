import java.util.Scanner;
class ScannerAndConditions
{
public static void main(String args [])
{
	//1.to check a number positive or negative
	
	Scanner sc = new Scanner(System.in);
	Scanner se = new Scanner(System.in);
	
	System.out.println("Lets Find given number is positive or negative,");
	System.out.println("Enter a Number: ");
	int number = sc.nextInt();
	
	if(number > 0)
	{
		System.out.println("you entered a positive number");
	}
	else if(number == 0)
	{
		System.out.println("given number is neither a positive,nor negative.its neutral");
	}
	else
	{
		System.out.println("you entered a negative number");
	}
	
	//2.to check a number odd or even
	
	System.out.println("Lets Find Given number is odd or even,");
	System.out.println("Enter a Number: ");
	int num = sc.nextInt();
	
	if(num%2==0)
	{
		System.out.println("you entered a even number");
	}
	else
	{
		System.out.println("you entered a odd number");
	}
	
	
	//3.print the day name based on number
	
	System.out.println("Lets Find which day according to a number ,");
	
	System.out.println("Enter a Number: ");
	int day = sc.nextInt();
	
	
	switch(day)
	{
		case 1:
		{
			System.out.println("Monday"); break;
		}
		case 2:
		{
			System.out.println("Tuesday"); break;
		}
		case 3:
		{
			System.out.println("Wednesday"); break;
		}
		case 4:
		{
			System.out.println("Thursday"); break;
		}
		case 5:
		{
			System.out.println("Friday"); break;
		}
		case 6:
		{
			System.out.println("Saturday"); break;
		}
		case 7:
		{
			System.out.println("Sunday"); break;
		}
		default:
		{
			System.out.println("you entered invalid input..."); 
			
		}
		
		}
		
		}
		
		
		




}