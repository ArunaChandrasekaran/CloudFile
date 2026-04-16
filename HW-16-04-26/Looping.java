import java.util.Scanner;
class Looping
{
public static void main(String args[])
{
	Scanner sc = new Scanner(System.in);
	//1.print numbers from 10 to 1
	
	System.out.print("REVERSLY PRINTING NUMBERS FROM 10 TO 1: ");
	
	for(int i=10; i>=1; i--)
	{
		System.out.print(i+" ");
	}
	
	//2.print all even numbers between 1 and 50
	System.out.println();
	
	System.out.print("EVEN NUMBERS BETWEEN 1 AND 50 ARE: ");
	
	for(int i=1; i<=50; i++)
	{
		if(i%2==0)
		{
			System.out.print(i+" ");
		}
	}
	
	//3.number of digit in a given number
	System.out.println();
	
	System.out.print("ENTER ANY NUMBER TO FIND HOW MANY DIGITS IN IT: ");
	int num = sc.nextInt();
	int count = 0;
	
	while(num>0)
	{
		num = num/10;
		count++;
		
	}
	System.out.println();
	System.out.print("THE NUMBER OF DIGITS IN A GIVEN NUMBER IS: "+ count);
	
}
}