
package employeeManagement;

public class EmployeeObject 
{
    
    String name;
    double salary;
    
    EmployeeObject(String name, double salary)
    {
        this.name = name;
        this.salary= salary;
    }
    
    void displayInfo()
    {
        System.out.println("EMPLOYEE NAME:"+name);
        System.out.println("SALARY: "+salary);
    }
    
}
