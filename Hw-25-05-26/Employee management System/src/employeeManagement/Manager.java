
package employeeManagement;
public class Manager extends EmployeeObject
{
    
    String department;
    
   
    
    Manager(String department)
    {
        super("Sanjay", 80000.0);
        this.department = department;
    }
    
    void displayInfo()
    {
        System.out.println("EMPLOYEE NAME:"+super.name);
        System.out.println("SALARY: "+super.salary);
        System.out.println("department: " + department);
    }
    
    
}
