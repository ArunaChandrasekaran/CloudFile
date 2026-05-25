
class Beta2
{
	public static void main(String args[])
	{
		
		int arr1 [] = {1,2,3,4};
		int arr2 [] = {1,2,3,4,5};
		
		Beta2 obj = new Beta2();
		
		obj.result(arr1);
		obj.result(arr2);
		
		
	}
	
	void result(int [] arr)
	{
		
		int mid = arr.length/2;
		int sum =0;
		
		if(arr.length%2==0)
		{
			for(int i =0; i<mid; i++)
			{
				sum = sum+arr[i];
			}
			
			System.out.println(sum);
		}
		else
		{
			for(int i =0; i<=mid; i++)
				{
					sum = sum+arr[i];
				}
				System.out.println(sum);
		}
		
	}
	
		
}
