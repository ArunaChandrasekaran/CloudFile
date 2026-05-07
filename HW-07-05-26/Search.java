import java.util.Scanner;
class Search
{
	Scanner sc = new Scanner(System.in);
	public static void main(String args[])
	{
		Search obj = new Search();
		
		
		
		obj.menu();
	}
	
	void menu()
	{
		boolean res = true;
		System.out.print("ENTER THE SIZE OF THE ARRAY: ");
		int size = sc.nextInt();
		
		int arr[] = new int[size];
		
		do{
		System.out.println("1.INSERT ELEMENTS");
		System.out.println("2.DISPLAY ELEMENTS");
		System.out.println("3.LINEAR SEARCH");
		System.out.println("4.BINARY SEARCH");
		System.out.println("5.EXIT");
		
		System.out.print("CHOOSE ANYONE FROM THE ABOVE CHOICE: ");
		int option = sc.nextInt();
		
		if(option==1)
		{
			insert(arr);
		}
		else if(option==2)
		{
			display(arr);
		}
		else if(option==3)
		{
			System.out.print("enter the value you are looking for:");
			int target = sc.nextInt();
			System.out.println(linear(arr,target));
		}
		else if(option==4)
		{
			System.out.print("enter the value you are looking for:");
			int value = sc.nextInt();
			System.out.println(binary(arr,value));
		}
		else
		{
			System.out.println("EXITED...");
			res = false;
		}
		}while(res);
	}
	
	void insert(int arr[])
	{
		for(int i=0; i<arr.length; i++)
		{
			System.out.println("enter the "+i+"th index value:");
			arr[i] = sc.nextInt();
		}
	}
	void display(int arr[])
	{
		for(int i=0; i<arr.length; i++)
		{
			System.out.println(arr[i]);
		}
	}
	int linear(int arr[],int target)
	{
		for(int i=0; i<arr.length; i++)
		{
			if(arr[i]==target)
			{
				return i;
			}
		}
		return -1;
		
	}
	
	int binary(int arr[],int target)
	{
		int low = 0;
		int high = arr.length-1;
		
		while(low<=high)
		{
			int mid = (low+high)/2;
			
			if(arr[mid]==target)
			{
				return mid;
			}
			else if(arr[mid]<target)
			{
				low = mid+1;
			}
			else
			{
				high = mid-1;
			}
		}
		return -1;
	}
}