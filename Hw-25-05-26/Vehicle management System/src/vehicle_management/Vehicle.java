
package vehicle_management;

public class Vehicle 
{
    
    String brand;
    int year;
    
    Vehicle(String brand,int year)
    {
        this.brand=brand;
        this.year=year;
     }
    
    void displayInfo()
    {
        System.out.println("BRAND: "+brand);
        System.out.println("YEAR: "+year);
    }
    
}
