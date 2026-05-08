class BinarySearch
{
	public static void main(String []args)
	{
		BinarySearch obj = new BinarySearch();
		int arr[] = {5,10,15,20,25,30};
		int key = 20;
	
		int left = 0;
		int right = arr.length-1;
		
		System.out.println(obj.binary(arr,key,left,right));
			
	}
	int binary(int arr[],int target,int left,int right)
	{
		if(left>right)
		{
			return -1;
		}
		
		int mid = (left+right)/2;
		
		if(arr[mid]==target)
		{
			return mid;
		}
		if(arr[mid]<target)
		{
			return binary(arr,target,mid+1,right);
		}
		
		return binary(arr,target,left,mid-1);
		
	}
}