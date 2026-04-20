class SumAndPrint
{
	public static void main(String args[])
	{
		
		//1. SUM OF THE FIRST TWO ELEMENTS
		int arr [] = {4,6,10};
		int sum = 0;
		
		for(int i=0; i<arr.length; i++)
		{
			if(i==0)
			{
			sum = sum + arr[i]+arr[i+1];
			}
		}
		System.out.println("SUM OF THE FIRST TWO ELEMENTS IN ARRAY: "+sum);
		
		//2.STORE AND PRINT
		
		int arr1 [] = {10,20,30};
		
		for(int i=0; i<arr1.length; i++)
		{
			System.out.print(arr1[i]+" ");
		}
	}
}