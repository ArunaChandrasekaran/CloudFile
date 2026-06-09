
package enumpractice;


import java.util.Scanner;


public enum Days {
    
    Monday("working day"),
    Tuesday("working day"),
    Wednesday("working day"),
    Thursday("working day"),
    Friday("working day"),
    Saturday("Weekend"),
    Sunday("Weekend");
    
    public final String day;

    private Days(String day) {
        this.day = day;
    }
    
    String get()
    {
        return day;
    }
    
    public static void main(String args[])
    {
        
       Scanner sc = new Scanner(System.in);
       while(true){ 
        System.out.println("1.CHECK IF DAY IS WORKING DAY");
        System.out.println("2.CHECK IF DAY IS WEEKEND");
        System.out.println("3.DISPLAY ALL DAYS");
        System.out.println("EXIT");
        System.out.println("CHOOSE ANYONE FROM THE ABOVE: ");
        int option = sc.nextInt();
        
        if(option ==1)
        {
            String day = sc.next();//Monday
            Days enumDay = Days.valueOf(day);
            if(enumDay != Days.Sunday && enumDay!= Days.Saturday)
            {
                
                System.out.println(enumDay.get());
                
            }
            else
            {
                System.out.println("Not a working day");
            }
            
        }
        else if(option==2)
        {
         String day = sc.next();//Monday
            Days enumDay = Days.valueOf(day);
            if(enumDay == Days.Sunday || enumDay == Days.Saturday)
            {
                
                System.out.println(enumDay.get());
                
            }
            else
            {
                System.out.println("Not a weekend");
            }
       
        }
        else if(option==3)
        {
         Days [] arr = Days.values();
         
         for(Days d : arr)
         {
             System.out.println(d);
         }
        
       
        }
        else
        {
            System.out.println("EXITED....");
            System.exit(0);
        }
    }
    }
    
}
