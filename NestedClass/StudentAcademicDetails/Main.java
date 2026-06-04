/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package StudentAcademicDetails;

import java.util.Arrays;
import java.util.Scanner;

/**
 *
 * @author aruna
 */
public class Main 
{
    static int average;
    static Scanner sc = new Scanner(System.in);
    static Institution.Student s = new Institution().new Student();
    public static void main(String[] args)
    {
        
        Main.menu();
              
    }
    
    static void menu()
    {
        int choice = 0;
        
        boolean isexit = true;
       
        do{
        System.out.println("1.ADD STUDENT");
        System.out.println("2.ADD MARKS");
        System.out.println("3.CALCULATE GRADE");
        System.out.println("4.DISPLAY STUDENT DETAILS");
        System.out.println("5.EXIT");
        
        System.out.print("choose anyone from above: ");
        choice = sc.nextInt();
        
        if(choice ==1)
        {
            Main.add();
        }else if(choice ==2 )
        {
            Main.markEnter();
        }
        else if(choice == 3)
        {
            Main.calculateGrade();
        }
        else if(choice == 4)
        {
            Main.display();
        }
        else
        {
            System.out.println("exited...");
            isexit =false;
        }
    }while(isexit);
        
    }
    static void add()
        {
            
            System.out.print("Enter student name: ");
            s.Student_name = sc.next();
            System.out.print("enter your roll number:");
            s.rollno = sc.nextInt();
            System.out.print("enter the number of subjects: ");
            s.number_of_subjects= sc.nextInt();
            s.marks = new int [s.number_of_subjects];
        }
    static void markEnter()
    {
        System.out.print("enter  your marks:");
             int total =0;
            for(int i=0; i<s.number_of_subjects ; i++)
            {
                s.marks[i] = sc.nextInt();
                total = total+s.marks[i];
            }
            average = total/s.number_of_subjects;
            System.out.println("****marks entered successfully****");
    }
    static void calculateGrade()
    {
        if(average>90)
        {
            System.out.println("GRADE A");
        }
        else if(average>=75 && average <= 89)
        {
            System.out.println("GRADE B");
        }
        else if(average >= 54 && average <= 74)
        {
            System.out.println("GRADE C");
        }
        else
        {
            System.out.println("FAIL");
        }
    }
    
    static void display()
    {
        System.out.println("Institution Name: "+Institution.institution_name);
        System.out.println("Student Name: "+s.Student_name);
        System.out.println("Roll No: "+s.rollno);
        System.out.println("Marks:"+Arrays.toString(s.marks));
        System.out.println("Average: "+average);
        calculateGrade();
    }
    
}
