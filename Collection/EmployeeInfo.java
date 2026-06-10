
package CollectionHw;


public class EmployeeInfo
{
    
    private int employee_id;
    private String employee_name;
    private String department;
    private Double basic_salary;

    public EmployeeInfo(int employee_id, String employee_name, String department, Double basic_salary) {
        this.employee_id = employee_id;
        this.employee_name = employee_name;
        this.department = department;
        this.basic_salary = basic_salary;
    }
    
    public int getId()
    {
        
        return employee_id;
        
    }

    public void setBasic_salary(Double basic_salary) {
        this.basic_salary = basic_salary;
    }

    public Double getBasic_salary() {
        return basic_salary;
    }
    
    
    
    
    @Override
    public String toString() {
        return "EMPLOYEE ID: " + employee_id +
                "\nEMPLOYEE NAME     : " +employee_name  +
                "\n:DEPARTMENT " + department +
                "\nBASIC SALARY   : " + basic_salary ;
                  }
    
    
   
    
    
}
