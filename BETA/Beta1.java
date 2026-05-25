
class Beta1
{
	public static void main(String args[])
	{
		
		
		char ch1 [] = {'h'};
		char ch2 [] = {'h','e','l','l','o'};
		
		Beta1 obj = new Beta1();
		System.out.println(obj.finalResult(ch1,ch2));
		
		
	}
	
		String finalResult(char ch1 [],char ch2[])
		{
			for(int i = 0; i<ch1.length;i++)
				{
					if(ch1.length!=ch2.length || ch1[i]!=ch2[i])
					{
						return "NOT SAME";
					}
					
				}
				
				return "SAME";
		}
		
}