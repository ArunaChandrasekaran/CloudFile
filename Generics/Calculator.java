
package generics.practice;
import java.util.Scanner;

/**
 *
 * @author aruna
 * 
 */
public class Calculator <T extends Number>
{
    
    public void add(T a, T b)
    {
        System.out.println(a.doubleValue() + b.doubleValue());
    }
    
    public void subtract(T a, T b)
    {
        System.out.println(a.doubleValue() - b.doubleValue());
    }

     
    
    public static void main(String[] args) 
    {
        Scanner sc = new Scanner(System.in);
        Calculator <Integer> b = new Calculator<>();
        Calculator <Double> b2 = new Calculator<>();
        
        b.add(5, 10);
        b.subtract(3,2);
        
        b2.add(5.0, 10.9);
        b2.subtract(4.3, 1.5);
        
    }
    
}
