/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package School;

import java.util.Scanner;

/**
 *
 * @author aruna
 */
import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.*;

public class Main {

    static Scanner sc = new Scanner(System.in);
    static Scanner se = new Scanner(System.in);
  
    static int id;
    static String name;
    static String email;
    static String course;
    static double fee;

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        
       
        Main m = new Main();
        
        m.menu();

    }
    public void menu() throws ClassNotFoundException, SQLException
    {
         Service s = new Service();
         boolean res = true;
        
         do{
        System.out.println("choose anyone option from the below:");
        System.out.println("1.INSERT");
        System.out.println("2.UPDATE");
        System.out.println("3.DELETE");
        System.out.println("4.EXIT");

        int option = sc.nextInt();
        
        
        if(option == 1)
        {
            info();
            s.insertservice(id, name, course, email, fee);
        }
        
        else if(option == 2)
        {
            info();
            s.updateservice(id, name, course, email, fee);
            
        }
        
        else if(option == 3)
        {
            System.out.print("enter the id :");
            id = sc.nextInt();
            s.deleteservice(id);
        }
        else
        {
            res = false;
            System.out.println("exited....");
        }
         }while(res);
        
    }

    public static void info() {
        System.out.print("enter the id :");
        id = sc.nextInt();
        System.out.print("enter the name ");
        name = se.nextLine();
        System.out.print("enter the email ");
        email = se.nextLine();
        System.out.print("enter the course ");
        course = se.nextLine();
        System.out.print("enter the fee ");
        fee = se.nextDouble();
    }

}
