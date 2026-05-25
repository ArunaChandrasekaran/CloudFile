class Beta3
{
	public static void main(String args[])
	{
		char ch[] = {'j','a','v','a'};
		
		
		
		Beta3 obj = new Beta3();
		obj.display(ch);
		
	}
	
	void display(char ch[])
	{
		int target = 2;
		for(int i=0; i<ch.length;i++)
		{
			if(i==target)
			{
				System.out.println(ch[i]);
			}
		}
	}
}