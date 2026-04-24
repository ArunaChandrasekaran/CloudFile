import java.util.Scanner;
class Prime
{
	public static void main(String args[])
	{
		Scanner sc = new Scanner(System.in);
		boolean result = true;
		do{
			
		System.out.print("ENTER WHICH NUMBER YOU WANT TO CHECK PRIME OR NOT: ");
		int n = sc.nextInt();
		
		if(n <= 0)
		{
			result = false;
			System.out.print("PROGRAM EXITED...");
			break;
		}
		
		Prime f1 = new Prime();
		
		f1.primeOrNot(n);
		
		}while(result);
		
	}
	
	void primeOrNot(int num)
	{
		if(num == 1)
		{
			System.out.println(num+" is neither a prime nor a composite");
		}
		else
		{
			boolean prime = true;
		
			for(int i = 2; i<=num/2; i++)
			{
				if(num%i == 0)
				{
					prime = false;
					break;
				}
			}
			if(prime)
			{
				System.out.println(num+" is  a prime number");
			}
			else
			{
				System.out.println(num+" is not a prime number");
			}
		}
	}
}