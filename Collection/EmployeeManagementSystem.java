
package CollectionHw;

import java.util.ArrayList;
import java.util.Scanner;


public class EmployeeManagementSystem 
{
    
    public static void main(String[] args) 
    {
        Scanner sc = new Scanner(System.in);
        ArrayList<EmployeeInfo> employees = new ArrayList<>();
        
         while (true) {
            System.out.println("***********EMPLOYEE MANAGEMENT SYSTEM*********");
            System.out.println("1.ADDING EMPLOYEES");
            System.out.println("2.SEARCHING EMPLOYEES");
            System.out.println("3.UPDATING SALARY");
            System.out.println("4.DELETING EMPLOYEE DETAILS");
            System.out.println("5.CALCULATING NET SALARY");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");

            int choice = sc.nextInt();

            switch (choice) {

                case 1:
                    System.out.print("Enter EMPLOYEE ID: ");
                    int id = sc.nextInt();

                    boolean exists = false;
                    for (EmployeeInfo e : employees) {
                        if (e.getId() == id) {
                            exists = true;
                            break;
                        }
                    }

                    if (exists) {
                        System.out.println("Id already exists!");
                        break;
                    }

                    sc.nextLine(); 

                    System.out.print("Enter Name: ");
                    String name = sc.nextLine();
                    
                    System.out.print("Enter department: ");
                    String department = sc.nextLine();

                    System.out.print("Enter Salary: ");
                    double b_salary  = sc.nextDouble();

                    employees.add(new EmployeeInfo(id, name,department,b_salary));

                    System.out.println("Employee Added Successfully...");
                    break;

                case 2:
                    if (employees.isEmpty()) {
                        System.out.println("No EMPLOYEE Records Found.");
                    } else {
                     System.out.print("Enter ID Number to Search: ");
                    int searchID = sc.nextInt();
                        
                        for (EmployeeInfo e :employees) 
                        {
                            if (e.getId() == searchID)
                            {
                                System.out.println(e);
                            }
                        }
                    }
                    break;
                case 3:
                    System.out.print("Enter ID Number to Search: ");
                    int searchID = sc.nextInt();

                    boolean found = false;

                    for (EmployeeInfo e : employees) {
                        if (e.getId() == searchID) {
                            System.out.print("Enter updated salary: ");
                            double salary = sc.nextDouble();
                            e.setBasic_salary(salary);

                            System.out.println("Salary Updated Successfully!");
                            found = true;
                            break;
                        }
                    }

                    if (!found) {
                        System.out.println("Employee Not Found.");
                    }
                    break;

                case 4:
                    System.out.print("Enter ID Number to Delete: ");
                    int empid = sc.nextInt();

                    found = false;

                    for (EmployeeInfo e : employees) {
                        if (e.getId()== empid) {
                            employees.remove(e);
                            System.out.println("Employee Deleted Successfully!");
                            found = true;
                            break;
                        }
                    }

                    if (!found) {
                        System.out.println("Employee  Not Found.");
                    }
                    break;

                case 5:
                    System.out.print("Enter ID Number to Search: ");
                    int ID = sc.nextInt();

                    found = false;

                    for (EmployeeInfo e : employees) {
                        if (e.getId() == ID) {
                            double hra = (e.getBasic_salary()*(20/100));
                            double da = (e.getBasic_salary()*(10/100));
                            double pf = (e.getBasic_salary()*(5/100));
                            System.out.println(e.getBasic_salary()+hra+da-pf);
                            found = true;
                            break;
                        }
                    }

                    if (!found) {
                        System.out.println("Employee Not Found.");
                    }
                    break;
                    
                case 6:
                    System.out.println("Exiting Program...");
                    
                    System.exit(0);    

                default:
                    System.out.println("Invalid Choice! Try Again.");
            }
        }
    }
    
}
