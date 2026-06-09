/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package generics.practice;

import enumpractice.Orders;
import java.util.Scanner;

/**
 *
 * @author aruna
 * 
 */
public class Box <T>
{
    
    private T data;
    
    public void setValue(T data)
    {
        this.data = data;
    }
    
    public T get()
    {
        return data;
    }
     
    
    public static void main(String[] args) 
    {
        Scanner sc = new Scanner(System.in);
        Box <Object> b = new Box<>();
         
      while(true)
      {
        System.out.println("1.Store int");
        System.out.println("2.Store String");
        System.out.println("3.DISPLAY Value");
        System.out.println("EXIT");
        System.out.println("CHOOSE ANYONE FROM THE ABOVE: ");
        int option = sc.nextInt();
        
        if(option ==1)
        {
            System.out.println("enter the integer value: ");
            int value = sc.nextInt();
            b.setValue(value);
            System.out.println("value setted successfully");
          
        }
        else if(option==2)
        {
         
           System.out.println("enter the String value: ");
            String value = sc.next();
            b.setValue(value);
            System.out.println("value setted successfully");
       
        }
        else if(option==3)
        {
            System.out.println("Stored value: "+b.get());
        }
        else
        {
            System.out.println("EXITED....");
            System.exit(0);
        }
      }
    }
    
}
