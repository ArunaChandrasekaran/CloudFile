
package enumpractice;

import java.util.Scanner;


public enum Signal 
{
    
    Red("STOP"),
    Yellow("WAIT"),
    Green("GO");
    
    public final String signalLight;

    private Signal(String signalLight) 
    {
        this.signalLight = signalLight;
    }  
    
    String get()
    {
        return signalLight;
    }
    
    public static void main(String args[])
    {
        Scanner sc = new Scanner(System.in);
       while(true){ 
        System.out.println("1.RED");
        System.out.println("2.YELLOW");
        System.out.println("3.GREEN");
        System.out.println("EXIT");
        System.out.println("CHOOSE ANYONE FROM THE ABOVE: ");
        int option = sc.nextInt();
        
        if(option ==1)
        {
         Signal s = Signal.Red;
        
        System.out.println(s.get());
            
        }
        else if(option==2)
        {
         Signal s = Signal.Yellow;
        
        System.out.println(s.get());
       
        }
        else if(option==3)
        {
         Signal s = Signal.Green;
        
        System.out.println(s.get());
       
        }
        else
        {
            System.out.println("EXITED....");
            System.exit(0);
        }
    }
        
    }
    
    
    
}
