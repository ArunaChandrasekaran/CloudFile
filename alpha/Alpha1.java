class Alpha1
{
	public static void main(String args[])
	{
		char ch [] ={'a','b','c','d'};
		
		Alpha1 obj = new Alpha1();
		obj.reverse(ch);
		
	
	}
	
	void reverse(char ch[])
	{
		for(int i =ch.length-1; i>=0; i--)
		{
			System.out.println(ch[i]);
		}
		
	}
		
	
}