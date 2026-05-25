/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vehicle_management;

/**
 *
 * @author aruna
 */
public class Motorcycle extends Vehicle 
{
    
    String type;
    public Motorcycle(String type) 
    {
        super("YAMAHA",2025);
        this.type = type;
        
    }
    
    void displayInfo()
    {
        System.out.println("BRAND: "+super.brand);
        System.out.println("YEAR: "+super.year);
        System.out.println("TYPE: "+type);
    }
    
    
    
    
    
}
