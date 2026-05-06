import java.util.Scanner;
class Insert
{
	public static void main(String args [])
	{
		Scanner sc = new Scanner(System.in);
		System.out.print("enter the size of array ");
		int size = sc.nextInt();
		int [] old = new int[size];
		int [] newer = new int[old.length+1];
		
		for(int i=0; i<old.length;i++)
		{
			old[i]=sc.nextInt();
		}
		
		System.out.print("enter which position you want to insert:");
		int position = sc.nextInt();
		System.out.print("enter which data you want to insert:");
		int data = sc.nextInt();
		
		for(int i=0; i<position-1;i++)
		{
			newer[i]=old[i];
		}
		
		newer[position-1] = data;
		
		for(int i=position; i<newer.length;i++)
		{
			newer[i]=old[i-1];
		}
		
		
		for(int i=0; i<newer.length;i++)
		{
			System.out.println(newer[i]);
		}
		
	}
	
}