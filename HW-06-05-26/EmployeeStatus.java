import java.util.Scanner;
class EmployeeStatus
{
	Scanner sc = new Scanner(System.in);
	
	
	public static void main(String args[])
	{
		EmployeeStatus obj = new EmployeeStatus();
		
		
		obj.menu();
		
	}
	
	void menu()
	{
		boolean result = true;
		int EmployeeId[] = new int[0];
		do{
		System.out.println("1.ADD EMPLOYEE ID");
		System.out.println("2.REMOVE EMPLOYEE ID");
		System.out.println("3.DISPLAY EMPLOYEE IDs");
		System.out.println("4.EXIT");
		
		System.out.print("ENTER YOUR CHOICE:");
		int option = sc.nextInt();
		
		if(option == 1)
		{
			EmployeeId = add(EmployeeId);
		}
		else if(option == 2)
		{
			EmployeeId = remove(EmployeeId);
		}
		else if(option == 3)
		{
			display(EmployeeId);
		}
		else
		{
			System.out.println("exited...");
			result = false;
		}
		}while(result);
	}
	int[] add(int arr[])
	{
		System.out.print("ENTER YOUR EMPLOYEE ID:");
		int id = sc.nextInt(); 
		
		int newArr[] = new int[arr.length + 1];
		for(int i=0; i<arr.length;i++)
		{
			newArr[i] = arr[i];
		}
		
		newArr[arr.length] = id;
		
		System.out.println("YOUR EMPLOYEE ID ADDED SUCCESFULLY...");
		return newArr;
	}
	int[] remove(int arr[])
	{
		System.out.print("ENTER EMPLOYEE ID TO DELETE: ");
		int id = sc.nextInt();

		//Find position
		int position = -1;

		for(int i = 0; i < arr.length; i++)
		{
			if(arr[i] == id)
			{
				position = i + 1;
				break;
			}
		}
		// If not found
		if(position == -1)
		{
			System.out.println("ID NOT FOUND");
			return arr;
		}

		//Create new array
		int newer[] = new int[arr.length - 1];

		//Copy before position
		for(int i = 0; i < position - 1; i++)
		{
			newer[i] = arr[i];
		}

		//Shift after position
	
		for(int i = position - 1; i < newer.length; i++)
		{
        newer[i] = arr[i + 1];
		}

		System.out.println("ID REMOVED SUCCESSFULLY");

		return newer;
	}
	
	void display(int arr[])
	{
		for(int i =0;i<arr.length;i++)
		{
			System.out.println(arr[i]);
		}
	}
}