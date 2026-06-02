
package exception;

import java.util.Scanner;


public class StudentMarkSystem
{
    public static void main(String[] args) throws MarkException
    {
        Scanner sc = new Scanner(System.in);
        
        int mark=0;
        int choice;
        
        do
        {
            System.out.println("1.enter student mark");
            System.out.println("2.display student mark");
            System.out.println("enter your choice");
            choice = sc.nextInt();
            
            switch(choice)
            {
                case 1:
                    try{
                    System.out.print("enter mark: ");
                    mark = sc.nextInt();
                    if(mark<=100&&mark>0)
                    {
                        System.out.println("your mark stored successfully..");
                    }
                    else
                    {
                        throw new MarkException(
                        "please enter the valid marks!..");
                    }
                    }catch(MarkException e)
                            {
                                System.out.println(e.getMessage());
                            } break;
                    
                case 2:
                    System.out.println("your mark "+mark);
                    
                    
            }
            
        }while(choice<=2&& choice>0);   
    }
    
}
