import java.util.Scanner;

class BankLoan
{
	public static void main(String args[])
	{
		Scanner sc = new Scanner(System.in);
		Scanner se = new Scanner(System.in);
		
		System.out.println("*******ENTER THE FOLLOWING DETAILS*******");
		
		System.out.print("ENTER YOUR NAME: ");
		String name = se.next();
		
		System.out.print("ENTER YOUR AGE: ");
		byte age = sc.nextByte();
		
		System.out.print("ENTER YOUR MONTHLY SALARY: ");
		int salary = sc.nextInt();
		
		System.out.print("ENTER YOUR CIBIL SCORE: ");
		int cibil= sc.nextInt();
		
		System.out.print("ARE YOU AN EXSISTING COSTOMER: ");
		
		boolean isExist = sc.nextBoolean();
		
		if(age>21)
		{
			if(salary>=20000)
			{
				if(cibil>=750)
				{
					if(isExist)
					{
						System.out.println(name+" YOU ARE ELIGIBLE FOR PREMIUM LOAN");
					}
					else
					{
						System.out.println(name+" YOU ARE ELIGIBLE FOR STANDARD LOAN");
					}
				}
				else if (cibil>650 && cibil<749)
				{
					System.out.println(name+" YOU ARE ELIGIBLE FOR LOW AMOUNT LOAN");
				}
				else if(cibil<650)
				{
					System.out.println(name+" YOU ARE NOT ELIGIBLE (LOW CIBIL SCORE)");
				}
			}
			else
			{
				System.out.println(name+" YOU ARE NOT ELIGIBLE(LOW SALARY)");
			}
		}
		else
		{
			System.out.println(name+" YOU ARE NOT ELIGIBLE, (AGE CRITERIA NOT SATISFIED)");
		}
	}
}