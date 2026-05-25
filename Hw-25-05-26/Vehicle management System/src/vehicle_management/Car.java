/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vehicle_management;

/**
 *
 * @author aruna
 */
public class Car extends Vehicle 
{
    int seats;

    public Car(int seats) {
        super("TOYOTO", 2025);
        this.seats = seats;
    }
    
    void displayInfo()
    {
         System.out.println("BRAND: "+brand);
        System.out.println("YEAR: "+year);
        System.out.println("SEATS: "+seats);
    }
}
