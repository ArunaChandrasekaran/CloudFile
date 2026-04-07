class MixedOperator{
	public static void main(String args [])
	{
		int a = 10;
		int b = 5;
		int c = 15;
		
		System.out.println((a+b)== c);	//true(using arithmetic and relational operator)
		System.out.println((a+b)<= c && b<c);	//true(using arithmetic ,relational and logical operator)	
		System.out.println((a+b)!=(b+c) || b>c); 		//true(using arithmetic ,relational and logical operator)
		
		
	}
}