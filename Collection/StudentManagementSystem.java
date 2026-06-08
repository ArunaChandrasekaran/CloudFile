
package CollectionHw;

import java.util.ArrayList;
import java.util.Scanner;

public class StudentManagementSystem {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        ArrayList<Student> students = new ArrayList<>();

        while (true) {
            System.out.println("***********STUDENT MANAGEMENT SYSTEM*********");
            System.out.println("1. Add Student");
            System.out.println("2. View All Students");
            System.out.println("3. Search Student by Roll Number");
            System.out.println("4. Update Student Marks");
            System.out.println("5. Delete Student");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");

            int choice = sc.nextInt();

            switch (choice) {

                case 1:
                    System.out.print("Enter Roll Number: ");
                    int rollNo = sc.nextInt();

                    boolean exists = false;
                    for (Student s : students) {
                        if (s.getRollNo() == rollNo) {
                            exists = true;
                            break;
                        }
                    }

                    if (exists) {
                        System.out.println("Roll Number already exists!");
                        break;
                    }

                    sc.nextLine(); 

                    System.out.print("Enter Name: ");
                    String name = sc.nextLine();

                    System.out.print("Enter Age: ");
                    int age = sc.nextInt();

                    sc.nextLine();

                    System.out.print("Enter Course: ");
                    String course = sc.nextLine();

                    System.out.print("Enter Marks: ");
                    int marks = sc.nextInt();

                    students.add(new Student(rollNo, name, age, course, marks));

                    System.out.println("Student Added Successfully...");
                    break;

                case 2:
                    if (students.isEmpty()) {
                        System.out.println("No Student Records Found.");
                    } else {
                        System.out.println("\nStudent Records:");
                        for (Student s : students) {
                            System.out.println(s);
                        }
                    }
                    break;

                case 3:
                    System.out.print("Enter Roll Number to Search: ");
                    int searchRoll = sc.nextInt();

                    boolean found = false;

                    for (Student s : students) {
                        if (s.getRollNo() == searchRoll) {
                            System.out.println("\nStudent Found:");
                            System.out.println(s);
                            found = true;
                            break;
                        }
                    }

                    if (!found) {
                        System.out.println("Student Not Found.");
                    }
                    break;

                case 4:
                    System.out.print("Enter Roll Number: ");
                    int updateRoll = sc.nextInt();

                    found = false;

                    for (Student s : students) {
                        if (s.getRollNo() == updateRoll) {
                            System.out.print("Enter New Marks: ");
                            double newMarks = sc.nextDouble();
                            s.setMarks((int) newMarks);

                            System.out.println("Marks Updated Successfully!");
                            found = true;
                            break;
                        }
                    }

                    if (!found) {
                        System.out.println("Student Not Found.");
                    }
                    break;

                case 5:
                    System.out.print("Enter Roll Number to Delete: ");
                    int deleteRoll = sc.nextInt();

                    found = false;

                    for (Student s : students) {
                        if (s.getRollNo() == deleteRoll) {
                            students.remove(s);
                            System.out.println("Student Deleted Successfully!");
                            found = true;
                            break;
                        }
                    }

                    if (!found) {
                        System.out.println("Student Not Found.");
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

