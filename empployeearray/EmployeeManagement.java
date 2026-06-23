/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package empployeearray;

import java.util.Scanner;


public class EmployeeManagement {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        Employee[] employees = new Employee[100];
        int count = 0;
        int choice;

        do {
            System.out.println("\n===== Employee Management System =====");
            System.out.println("1. Add Employee");
            System.out.println("2. View Employees");
            System.out.println("3. Search Employee by ID");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");

            choice = sc.nextInt();

            switch (choice) {

                case 1:
                    if (count < 100) {
                        System.out.print("Enter Employee ID: ");
                        int id = sc.nextInt();

                        sc.nextLine(); // consume newline

                        System.out.print("Enter Employee Name: ");
                        String name = sc.nextLine();

                        System.out.print("Enter Department: ");
                        String dept = sc.nextLine();

                        employees[count] = new Employee(id, name, dept);
                        count++;

                        System.out.println("Employee Added Successfully!");
                    } else {
                        System.out.println("Employee Storage Full!");
                    }
                    break;

                case 2:
                    if (count == 0) {
                        System.out.println("No Employees Found!");
                    } else {
                        System.out.println("Employee Details:");
                        for (int i = 0; i < count; i++) {
                            employees[i].displayDetails();
                        }
                    }
                    break;

                case 3:
                    System.out.print("Enter Employee ID to Search: ");
                    int searchId = sc.nextInt();

                    boolean found = false;

                    for (int i = 0; i < count; i++) {
                        if (employees[i].empId == searchId) {
                            System.out.println("\nEmployee Found:");
                            employees[i].displayDetails();
                            found = true;
                            break;
                        }
                    }

                    if (!found) {
                        System.out.println("Employee Not Found!");
                    }
                    break;

                case 4:
                    System.out.println("Thank You!");
                    break;

                default:
                    System.out.println("Invalid Choice!");
            }

        } while (choice != 4);

        sc.close();
    }
}
