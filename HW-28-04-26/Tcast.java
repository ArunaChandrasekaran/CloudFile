class Tcast
{
	public static void main(String args [])
	{
		//1.read int value and convert it into a double
		
		int small = 25;
		double big = small;
		
		System.out.println("int value and convert it into a double is "+big);
		
		//2.character to integer conversion
		
		char letter = 'a';
		int letter1 = letter;
		
		System.out.println("character to integer conversion is "+letter1);
		
		//3.double to integer conversion
		
		double doub = 45.89;
		int num = (int)doub;
		System.out.println("double to integer conversion is "+num);
		
		//4.integer to byte conversion
		
		int number = 130;
		byte tiny = (byte)number;
		System.out.println("integer to byte conversion is "+tiny);
	}
}