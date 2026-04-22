import java.util.Scanner;
class SecondLargestInArray
{
	public static void main(String args[])
	{
		Scanner sc = new Scanner(System.in);
		int max1;
		int max2;
		
		System.out.print("print your array size: ");
		
		int size = sc.nextInt();
		int arr[] = new int [size];
		
		System.out.println("you can add your array element");
		
		for(int i=0; i<arr.length; i++)
		{
			arr[i] = sc.nextInt();
		}
		
		if(arr[0]>arr[1])
		{
			max1 = arr[0];
			max2 = arr[1];
		}
		else
		{
			max1 = arr[1];
			max2 = arr[0];
		}
		
		for(int i=2; i<arr.length;i++)
		{
			if(arr[i]>max1)
			{
				max2 = max1;
				max1 = arr[i];
			}
			else if(arr[i]>max2 && arr[i]!= max1)
			{
				max2 = arr[i];
			}
		}
		System.out.print("THE SECOND LARGEST NUMBER IS:"+ max2);
	}
}