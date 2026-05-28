
import java.util.Scanner;
import java.util.Stack;


public class Main 
{
    Scanner sc = new Scanner(System.in);
    public static void main(String[] args) 
    {
         Main m = new Main();
         m.menu();
        
    }
    void menu()
    {
        SuperMarket s = new SuperMarket();
        boolean isExit = true;
        
        do{
        System.out.println("1.Add items");
        System.out.println("2.Undo Last Item");
        System.out.println("3.View Last Scanned Items");
        System.out.println("4.Disply All");
        System.out.println("5.Exit");
        
        System.out.print("Enter Anyone From the Above option:");
        int option = sc.nextInt();
        
        if (option==1)
        {
            s.insert(sc.next());
        }
        else if(option==2)
        {
            
            s.delete();
        }
        else if(option==3)
        {
            s.viewLast();
        }
        else if(option==4)
        {
            s.display();
        }
        else
        {
            System.out.println("PROGRAM EXITED....");
            isExit = false;
        }
        }while(isExit);
    }
}
