
package empployeearray;


import java.util.Scanner;

class Employee {
    int empId;
    String empName;
    String department;

    Employee(int empId, String empName, String department) {
        this.empId = empId;
        this.empName = empName;
        this.department = department;
    }

    void displayDetails() {
        System.out.println("Employee ID : " + empId);
        System.out.println("Employee Name : " + empName);
        System.out.println("Department : " + department);
       
    }
}