import java.util.Scanner;
class Delete
{
	public static void main(String args [])
	{
		Scanner sc = new Scanner(System.in);
		System.out.print("enter the size of array ");
		int size = sc.nextInt();
		int [] old = new int[size];
		int [] newer = new int[old.length-1];
		
		for(int i=0; i<old.length;i++)
		{
			old[i]=sc.nextInt();
		}
		
		System.out.print("enter which position you want to delete:");
		int position = sc.nextInt();
		
		for(int i=0; i<position-1;i++)
		{
			newer[i]=old[i];
		}
		
		
		
		for(int i=position-1; i<newer.length;i++)
		{
			newer[i]=old[i+1];
			
		}
		
		
		for(int i=0; i<newer.length;i++)
		{
			System.out.println(newer[i]);
		}
		
	}
	
}