
package gamaexam;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class UsernamePasswordValidation
{      static Scanner sc = new Scanner(System.in);
    public static void main(String[] args) 
    {
        UsernamePasswordValidation obj = new UsernamePasswordValidation();
        System.out.println("enter your username:");
        String name = sc.next();
        System.out.println("enter password");
        String password = sc.next();
        
        if(obj.login(name, password))
        {
            System.out.println("LOGIN SUCCESSFUL");
        }
        else
        {
            System.out.println("INVALID USERNAME AND PASSWORD..");
        }
    }
    
    public boolean usernameValidation(String username)
    {
        
        if(username.length()<4)
        {
            return false;
            
        }
        
        for(int i =0; i<username.length();i++)
        {
            char ch = username.charAt(i);
            
            if(!(ch>='a'&&ch<='z'||ch>='A'&&ch<='Z'))
            {
                System.out.println("only contains letters");
                return false;
            }
        }
        return true;
    }
    public boolean passwordValidation(String password)
    {
        if(password.length()<6)
            {
                return false;
            
            }
        
         Set<String> resultSet = new HashSet();
              
              
         for(int i = 0; i < password.length(); i++) 
             {
            
               int character = password.charAt(i);
            
               if (character == ' ') 
               {
                return false;
               } 
                else if ((character >= 'a' && character <= 'z')) 
                {
                resultSet.add("hasLower");
                } 
                else if ((character >= 'A' && character <= 'Z')) 
                {
                resultSet.add("hasUpper");
                } 
                else if ((character >= '0' && character <= '9')) 
                {
                resultSet.add("hasNumeric");
                } 
                else 
                {
                resultSet.add("hasSymbols");
                }
        }
        return resultSet.size() == 4;
    }
    
    public boolean login(String username,String password)
    {
        return usernameValidation(username) && passwordValidation(password);
    }
    
}
