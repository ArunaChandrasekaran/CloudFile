class MultidimensionalArray
{
	public static void main(String args[])
	{
		int arr1[][] = {{1,2},{3,4}};
		int arr2[][] = {{5,6},{7,8}};
		
		int arr3 [][] = new int[arr1.length][arr1.length];
		
		for(int i=0; i<arr1.length; i++)
		{
			for(int j=0; j<arr1[i].length; j++)
			{
				arr3[i][j]=arr1[i][j]+arr2[i][j];
			}
		}
		
		System.out.println("ADDING TWO 2D ARRAY AS RESULT IS BELOW:");
		
		for(int i=0; i<arr3.length; i++)
		{
			for(int j=0; j<arr3[i].length; j++)
			{
				System.out.print(arr3[i][j]+" ");
			}
			System.out.println();
		}
		
		int findLargest[][] = {{1,8,3},{4,2,6}};
		int largest=findLargest[0][0];
		
		for(int i=0; i<arr3.length; i++)
		{
			for(int j=0; j<arr3[i].length; j++)
			{
			     if(findLargest[i][j]>largest)
				 {
					 largest = findLargest[i][j];
				 }
			}
			System.out.println();
		}
		System.out.println(largest);
	}
}