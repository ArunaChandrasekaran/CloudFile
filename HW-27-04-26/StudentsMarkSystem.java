import java.util.Scanner;
class StudentsMarkSystem
{	
	Scanner sc = new Scanner(System.in);
	public static void main(String args[])
	{
		StudentsMarkSystem f1 = new StudentsMarkSystem();
		f1.menu();
	}
	
	void menu()
	{
		boolean result = true;
		System.out.print("ENTER THE ARRAY SIZE: ");
		int size = sc.nextInt();
		
		int arr[] = new int[size];
		
		
		
			System.out.println("ENTER YOUR MARKS...");
			
			for(int i=0; i<arr.length; i++)
			{
				arr[i] = sc.nextInt();
			}
			
			do
			{
			System.out.println("1.CALCULATE TOTAL");
			System.out.println("2.CALCULATE AVERAGE");
			System.out.println("3.FIND GRADE");
			System.out.println("4.EXIT");
		
		System.out.print("CHOOSE ANY ONE FROM THE ABOVE CHOICE:");
		int option = sc.nextInt();
		
		if(option==1)
		{
			System.out.println(total(arr));
		}
		else if(option==2)
		{
			System.out.println(average(arr));
		}
		else if(option==3)
		{
			grade(arr);
		}
		else
		{
			System.out.println("EXITED...");
			
			result = false;
			
		}
		}while(result);		
		
	}
	
	int total(int [] ar)
	{
		int sum = 0;
		for(int i=0; i<ar.length; i++)
		{
			sum = sum+ar[i];
		}
			return sum;
	}
	int average(int [] ar)
	{
		 int sum = total(ar);
		return sum/ar.length;	
	}
	
	void grade(int [] ar)
	{	
			int sum = total(ar);
		
		if(sum>450)
		{
			System.out.println('O');
		}
		else if(sum>400)
		{
			System.out.println('A');
		}
		else if(sum>350)
		{
			System.out.println('B');
		}
		else if(sum>300)
		{
			System.out.println('c');
		}
		
		else
		{
			System.out.println("NEED MORE MARKS TO GET GOOD GRADES...");
		
		}
	}
}