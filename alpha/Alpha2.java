import java.util.Scanner;
class Alpha2
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
		
		int count=0;
		
		for(int i=0; i<arr.length;i++)
		{
			if(arr[i]==40)
			{
				count++;
			}
		}
		
		System.out.println("COUNT OF THE 40 IS: "+count);
	}
}