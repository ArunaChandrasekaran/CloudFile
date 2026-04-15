import java.util.Scanner;
class Loops{
	public static void main(String args[])
	{
		//1.for loop
		
		
		for(int i=1; i<=10; i++)
		{
			
			System.out.println(i);
		}
		
		
		
		//2.while loop(find the number of digits)
		
		Scanner sc = new Scanner(System.in);
		
		System.out.print("ENTER ANY NUMBER TO FIND THE NUMBER OF DIGITS:");
		
		int number = sc.nextInt();
		int digit = number;
		int count = 0;
		
		while(digit>0)
		{
			digit = digit/10;
			count++;
		}
		System.out.println("GIVEN NUMBER IS: "+number+" NUMBER OF DIGITS :"+count);
		
		//3.do while loop(5 table)
		
		int series = 1;
		
		do
		{
			System.out.println("5 * "+series+" = "+5*series);
			series++;
		}while(series<=10);
		
	}
}