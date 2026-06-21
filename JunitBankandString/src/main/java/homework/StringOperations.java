
package homework;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;


public class StringOperations 
{
    Scanner sc = new Scanner(System.in);
    public static void main(String[] args)
    {
        
        StringOperations obj = new StringOperations();
        obj.menu();
        
        
    }
    
    public void menu()
    {
       while(true)
       {
        System.out.println("1.Find duplicate characters");
        System.out.println("2.Remove Space");
        System.out.println("3. Reverse String");
        System.out.println("choose anyone from the above: ");
        int option = sc.nextInt();
         sc.nextLine();
        
        if(option == 1)
        {
            findDuplicates();
        }
        else if(option == 2)
        {
            removeSpace();
        }
        else if(option == 3)
        {
            reverseString();
        }
        else
        {
            System.out.println("PROGRAM EXITED...");
            System.exit(0);
        }
    }
        
    }
    
   public  void findDuplicates()
    {
       
        System.out.print("enter a string to find duplicates: ");
        String input = sc.nextLine();
        
        Set<Character> seen = new HashSet<>();
        Set<Character> duplicate = new HashSet<>();

    for(char ch : input.toCharArray()) {
        if(!seen.add(ch)) {
            duplicate.add(ch);
        }
    }

    System.out.println("Duplicate characters: " + duplicate);
            
        }
    
    public void removeSpace()
    {
        System.out.print("enter a string to remove spaces: ");
        String input = sc.nextLine();
        
        for(int i =0; i<input.length(); i++)
        {
            if(input.indexOf(' ')==-1)
            {
                System.out.println(input);
                break;
            }
            else
            {
                if(input.charAt(i)!=' ')
                {
                    System.out.print(input.charAt(i));
                }
            }
            
        }
    }
    
    public void reverseString()
    {
        System.out.print("enter a string to reverse: ");
        String input = sc.nextLine();
        
        for(int i =input.length()-1; i>=0; i--)
        {
            
                System.out.print(input.charAt(i));
        }
    }
    
}
