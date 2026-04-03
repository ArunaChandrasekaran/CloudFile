class SwappingThreeNumber
{
	public static void main(String args[])
	{
			int a = 10;
			int b = 20;
			int c = 30;
			
			System.out.println("Before swapping in A: " + a);
			System.out.println("Before swapping in B: " + b);
			System.out.println("Before swapping in C: " + c);
			
			a = a+b+c;
			c = a-(b+c);
			b = a-(b+c);
			a = a-(b+c);
			
			System.out.println("After swapping in A: " + a);
			System.out.println("After swapping in B: " + b);
			System.out.println("After swapping in C: " + c);
	
	
	}
}