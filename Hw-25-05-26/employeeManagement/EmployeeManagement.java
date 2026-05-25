
package employeeManagement;


public class EmployeeManagement 
{
    public static void main(String[] args) 
    {
        
        EmployeeObject e1 = new EmployeeObject("Aruna",50000.0);
        e1.displayInfo();
        
        Manager e2 = new Manager("IT");
        e2.displayInfo();
        
    }
}
