
package collection.hw;

import java.util.ArrayList;

public class ListworkOut 
{
   static  ArrayList <Integer>l = new ArrayList();
    
    
    public static void main(String[] args) 
    {
        l.add(10);
        l.add(20);
        l.add(40);
        l.add(50);
        l.add(60);
        
        
        //adding element to the correct index..
        l.add(2,30);
        
        //removing element by using value..
        l.remove(Integer.valueOf(40));
        
        //update element
        l.set(0, 70);
        
        
        //iterate through for each
        for(Object obj:l)
        {
            System.out.println(obj);
        }
        
    }
    
}
