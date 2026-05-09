import java.util.Scanner;
class Alpha3
{
	public static void main(String args[])
	{
		Scanner sc = new Scanner(System.in);
		
		System.out.print("ENTER YOUR SIZE OF ARRAY: ");
		int size = sc.nextInt();
		
		int arr[] = new int[size];
		
		for(int i=0; i<arr.length;i++)
		{
			System.out.print("ENTER THE "+i+" index value:");
			arr[i]=sc.nextInt();
		}
		
		int sum=0;
		
		for(int i=0; i<arr.length;i++)
		{
			sum = sum+arr[i];
		}
		
		System.out.println("SUM OF THE ARRAY IS: "+sum);
	}
}