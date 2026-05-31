import java.util.Scanner;


public class Palindrome {
    
    public static void main(String[] args) 
    {
        Scanner sc = new Scanner(System.in);
        System.out.println("ENTER THE VALUE TO CHECK IT IS PALINDROME OR NOT:");
        String name = sc.next();
        
        Palindrome p = new Palindrome();
        p.checkPalindrome(name);
        
    }
    
    void checkPalindrome(String name)
    {
        boolean flag = true;
        int i = 0;
        int j = name.length()-1;
        
        while(i<j)
        {
            if(name.charAt(i)!=name.charAt(j))
            {
                System.out.println(name.charAt(i));
                flag = false;
                break;
             
                
            }
            i++;
            j--;
        }
        
        if(flag)
        {
            System.out.println("given name is a palindrome");
        }
        else
        {
            System.out.println("not a palindrome");
        }
    }
    
}
