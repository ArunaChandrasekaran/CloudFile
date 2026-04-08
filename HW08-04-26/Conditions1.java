class Conditions1{
	public static void main(String args[])
	{
		
		char signal = 'G';
		
		//1. smart traffic signal system 
		
		if(signal == 'G')
		{
			System.out.println("GO");
		}
		else if(signal == 'S')
		{
			System.out.println("STOP");
		}
		else if(signal == 'Y')
		{
			System.out.println("Ready");
		}
		else
		{
			System.out.println("Entered a invalid character");
		}
		
		//2.Movie ticket price calculator
		
		int age = 15;
		
		if(age < 12)
		{
			System.out.println("your age is below 12, so the ticket amount is: " + age);
		}
		else if (age>12 && age<=59 )
		{
			System.out.println("your age is above 12, and within 59,so the ticket amount is: " + age );
		}
		else if (age>=60 )
		{
			System.out.println("your age is above 60,so the ticket amount is: " + age );
		}
		
		
		//3.Mobile battery status checker
		
		int batteryLimit = 35;
		
		if(batteryLimit >= 80)
		{
			System.out.println("Battery full");
		}
		else if(batteryLimit >=30 && batteryLimit <=79)
		{
			System.out.println("Battery normal");
		}
		else if(batteryLimit<30)
		{
			System.out.println("Low battery,charge now");
		}
		
		
		
	}
	
	
}