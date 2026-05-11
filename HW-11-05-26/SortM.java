import java.util.Scanner;
import java.util.Arrays;
class SortM
{
	Scanner sc = new Scanner(System.in);
	public static void main(String args[])
	{
		SortM obj = new SortM();
		obj.menu();
	}
	
	void menu()
	{
		System.out.print("ENTER THE SIZE OF YOUR ARRAY:");
		int size = sc.nextInt();
		int arr[] = new int [size];
		boolean res = true;
		
		do
		{
		System.out.println("1.ENTER ARRAY");
		System.out.println("2.SORTING IN ASCENDING ORDER");
		System.out.println("3.SORTING IN DESCENDING ORDER");
		System.out.println("4.FIND MEDIAN OF THE ARRAY");
		System.out.println("5.DISPLAY");
		System.out.println("6.Exit");
		
		System.out.println("ENTER ANYONE FROM THE ABOVE: ");
		int option = sc.nextInt();
		
		
		
		if(option==1)
		{
			create(arr);
		}
		else if(option==2)
		{
			int res2[]=ascendingM(arr);
			System.out.println(Arrays.toString(res2));
		}
		else if(option==3)
		{
			int res1[]=decendingM(arr);
			System.out.println(Arrays.toString(res1));
		}
		else if(option==4)
		{
			median(arr);
		}
		else if(option==5)
		{
			display(arr);
		}
		else
		{
			System.out.println("EXITED");
			res = false;
		}
		}while(res);
		
	}
		
		void create(int arr[])
		{
			for(int i=0; i<arr.length; i++)
			{
				System.out.print("ENTER THE "+i+" th element:");
				arr[i]=sc.nextInt();
			}
		}
		int [] ascendingM(int arr[])
		{
				if(arr.length<=1)
				{
					return arr;
				}
		
			int mid = arr.length/2;
		
			int left[] = ascendingM(Arrays.copyOfRange(arr,0,mid));
			int right[] = ascendingM(Arrays.copyOfRange(arr,mid,arr.length));
		
			return conquer(left,right);
		}
			
		int[] conquer(int left[],int right[])
		{
		int i =0;
		int k =0;
		int j =0;
		
		int newarr[] = new int[left.length+right.length];
		
		while(i<left.length && j < right.length)
		{
		if(left[i]<right[j])
			{
				newarr[k] = left[i];
				i++;
				k++;
			}
			else
			{
				newarr[k] = right[j];
				j++;
				k++;
			}
		}
		while(i<left.length)
		{
			newarr[k] = left[i];
				i++;
				k++;
		}
		while(j<right.length)
		{
			newarr[k]= right[j];
			j++;
			k++;
		}
		
		return newarr;
	}
		int [] decendingM(int arr[])
		{
				if(arr.length<=1)
				{
					return arr;
				}
		
			int mid = arr.length/2;
		
			int left[] = decendingM(Arrays.copyOfRange(arr,0,mid));
			int right[] = decendingM(Arrays.copyOfRange(arr,mid,arr.length));
		
			return conq(left,right);
		}
		int[] conq(int left[],int right[])
		{	
		int i =0;
		int k =0;
		int j =0;
		
		int newarr[] = new int[left.length+right.length];
		
		while(i<left.length && j < right.length)
		{
		if(left[i]>right[j])
			{
				newarr[k] = left[i];
				i++;
				k++;
			}
			else
			{
				newarr[k] = right[j];
				j++;
				k++;
			}
		}
		while(i<left.length)
		{
			newarr[k] = left[i];
				i++;
				k++;
		}
		while(j<right.length)
		{
			newarr[k]= right[j];
			j++;
			k++;
		}
		
		return newarr;
		}
		void median(int arr[])
		{
			int mid = arr.length/2;
			System.out.println(arr[mid]);
		}
		void display(int arr[])
		{
			for(int i=0; i<arr.length;i++)
			{
				System.out.print(arr[i]+" ");
			}
		}
}
