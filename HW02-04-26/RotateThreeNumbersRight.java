class RotateThreeNumbersRight
{
	public static void main(String args[])
	{
			int a = 5;
			int b = 15;
			int c = 25;
			
			System.out.println("Before swapping in A: " + a);
			System.out.println("Before swapping in B: " + b);
			System.out.println("Before swapping in C: " + c);
			
			a = a+b+c;
			b = a-(b+c);
			c = a-(b+c);
			a = a-(b+c);
			
			System.out.println("After swapping in A: " + a);
			System.out.println("After swapping in B: " + b);
			System.out.println("After swapping in C: " + c);
	
	
	}
}