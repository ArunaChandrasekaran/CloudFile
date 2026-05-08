class LinearSearch
{
	public static void main(String []args)
	{
		LinearSearch obj = new LinearSearch();
		int arr[] = {10,20,30,40,50};
		int key = 30;
		
		System.out.println(obj.linear(arr,key,0));
			
	}
	
	int linear(int arr[],int target,int index)
	{
		if(index>arr.length-1)
		{
			return -1;
		}
		if(arr[index]==target)
		{
			return index;
		}
		
		return linear(arr,target,index+1);
	}
	
}