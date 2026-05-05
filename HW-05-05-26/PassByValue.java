class PassByValueAndRefferance
{
	public static void main(String args [])
	{
		PassByValueAndRefferance obj = new PassByValueAndRefferance();
		int a = 40;
		int []arr = {45,50,55,60,65};
		
		obj.display(a);
		System.out.println(a);
		
		System.out.println("before modification of array");
		for(int i = 0; i<arr.length; i++)
		{
			System.out.print(arr[i]+" ");
		}
		
		System.out.println();
		
		System.out.println("after modification of array");
		
		obj.arr(arr);
		
	}
	
	void display(int a)
	{
		a = 50;
		System.out.println(a);
	}
	
	void arr(int [] arr)
	{
		arr[0] = 20;
		
		for(int i = 0; i<arr.length; i++)
		{
			System.out.print(arr[i]+" ");
		}
	}
	
}