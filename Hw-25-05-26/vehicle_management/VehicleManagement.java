
package vehicle_management;


public class VehicleManagement 
{
    
    public static void main(String[] args) {
        
        Car c1 = new Car(5);
        Motorcycle m = new Motorcycle("sports");
        
        c1.displayInfo();
        m.displayInfo();
    
    }
    
}
