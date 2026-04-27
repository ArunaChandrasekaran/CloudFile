import java.util.Scanner;
class Bankconsole
{	
	Scanner sc = new Scanner(System.in);
	int balance =0;
	public static void main(String args[])
	{
		Bankconsole f1 = new Bankconsole();
		f1.menu();
	}
	
	void menu()
	{
		boolean result=true;
		do
		{
		System.out.println("1.DEPOSIT");
		System.out.println("2.WITHDRAW");
		System.out.println("3.CHECK BALANCE");
		System.out.println("4.EXIT");
		
		System.out.print("CHOOSE ANYONE FROM THE ABOVE CHOICE: ");
		int option = sc.nextInt();
		
		if(option==1)
		{
			System.out.print("ENTER THE AMOUNT THAT YOU WANT TO DEPOSIT: ");
			int amt = sc.nextInt();
			deposit(amt);
		}
		else if(option==2)
		{
			System.out.print("ENTER THE AMOUNT THAT YOU WANT TO WITHDRAW: ");
			int amt = sc.nextInt();
			withdraw(amt);
		}
		else if(option==3)
		{
			checkBalance();
		}
		else
		{
			System.out.println("EXITED...");
			result = false;
		}
		}while(result);
		

	}
	
	void deposit(int amt)
	{
		System.out.println("YOU HAVE DEPOSITED Rs."+amt);
		balance = balance + amt;
	}
	void withdraw(int amt)
	{
		System.out.println("YOU HAVE WITHDRAW Rs."+amt);
		balance = balance - amt;
	}
	void checkBalance()
	{
		System.out.println("AVAILABLE BALANCE Rs."+balance);
	}
}