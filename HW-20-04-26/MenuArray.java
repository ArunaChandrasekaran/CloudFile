import java.util.Scanner;
class MenuArray
{
	public static void main(String args [])
	{
		Scanner sc = new Scanner(System.in);
		
		System.out.print("ENTER THE SIZE OF AN ARRAY: ");
		int size = sc.nextInt();
		int arr[] = new int[size];
		boolean result = true;
		
		do{
		
		System.out.println("1.INSERT ELEMENTS");
		System.out.println("2.DISPLAY ELEMENTS");
		System.out.println("3.COUNT EVEN ELEMENTS");
		System.out.println("4.COUNT ODD ELEMENTS");
		
		System.out.print("CHOOSE ANY ONE FROM ABOVE: ");
		int choice = sc.nextInt();
	
		
		if(choice == 1)			//1.INSERT
		{
			System.out.println("NOW YOU CAN INSERT YOUR VALUE");
			for(int i=0; i<arr.length; i++)
			{
				arr[i] =sc.nextInt();
			}
			System.out.println("VALUES WERE INSERTED");
		}
		else if(choice == 2)	//2.DISPLAY
		{
			for(int i=0; i<arr.length; i++)
			{
				System.out.print(arr[i]+" ");
			}
			System.out.println("VALUES WERE DISPLAYED");
		
		}
		else if(choice == 3)	//3.COUNT EVEN ELEMENTS
		{		int evenCount = 0;
			for(int i=0; i<arr.length; i++)
			{
				if(arr[i]%2==0)
				{
					evenCount++;
				}
				
			}
			System.out.println("COUNT OF THE EVEN ELEMENTS: "+ evenCount);
		
		}
		else if(choice == 4)	//4.COUNT OF THE ODD ELEMENTS
		{		int oddCount = 0;
			for(int i=0; i<arr.length; i++)
			{
				if(arr[i]%2!=0)
				{
					oddCount++;
				}
				
			}
			System.out.println("COUNT OF THE ODD ELEMENTS: "+ oddCount);
		
		}
		else
		{
			System.out.println("EXITED...");
			result = false;
		}
		}while(result);
			
	}
}