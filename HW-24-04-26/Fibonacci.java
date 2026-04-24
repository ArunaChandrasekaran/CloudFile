class Fibonacci{
	public static void main(String args [])
	{
		Fibonacci f1 = new Fibonacci();
		f1.fibo(6);
	}
	
	void fibo(int n)
	{
		int first = 0;
		int second = 1;
		int third;
		
		System.out.print(first+" ");
		System.out.print(second+" ");
		
		for(int i =3; i<=n ; i++)
		{
			third = first+second;
			System.out.print(third+" ");
			first = second;
			second = third;
		}
	}
}