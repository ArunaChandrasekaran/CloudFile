/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package samplejdbc;
import java.util.Scanner;
import java.sql.*;

/**
 *
 * @author aruna
 */
public class Samplejdbc {

    Scanner sc = new Scanner(System.in);
    Scanner se = new Scanner(System.in);
    public static void main(String[] args) throws ClassNotFoundException, SQLException 
    {
      Samplejdbc s = new Samplejdbc();
      s.menu();
    }
    
    void menu() throws ClassNotFoundException, SQLException
    {
        boolean res = true;
        do{
        System.out.println("choose any one option from the below:");
        System.out.println("1.INSERT");
        System.out.println("2.DISPLAY");
        System.out.println("3.UPDATE");
        System.out.println("4.DELETE");
        System.out.println("5.EXIT");
        
        int option = sc.nextInt();
        
        if(option==1)
        {
            System.out.print("enter the id:");
            int id=sc.nextInt();
            System.out.print("enter the name :");
            String name=se.nextLine();
            System.out.print("enter the phonenumber:");
            long phone=sc.nextLong();
            insert(id, name, phone);
            
        }
        else if(option == 2)
        {
           display();
        }
        else if(option ==3)
        {
            System.out.print("enter the id:");
            int id=sc.nextInt();
            System.out.print("enter the name :");
            String name=se.nextLine();
            System.out.print("enter the phonenumber:");
            long phone=sc.nextLong();
            update(id,name,phone);
            
        }
        else if(option == 4)
        {
            System.out.print("enter the id:");
            int id=sc.nextInt();
            delete(id);
            
        }
        else
        {
            System.out.println("PROGRAM EXITED...");
            res = false;
        }
        }while(res);
    }
    
    public Connection dbconnection() throws ClassNotFoundException, SQLException
    {
            Class.forName("org.postgresql.Driver");
            Connection c=DriverManager.getConnection("jdbc:postgresql://localhost:5432/J56JDBC","postgres","Arunask@12");
            return c;
    }

    public void insert(int id,String name,long phone) throws ClassNotFoundException, SQLException
    {
    Connection c=dbconnection();
    PreparedStatement ps= c.prepareStatement("insert into students values (?,?,?)");
    ps.setInt(1, id);
    ps.setString(2, name);
    ps.setLong(3, phone);
    int res=ps.executeUpdate();
    if(res>0)
    {
        System.out.println("inserted");
    }
    else
    {
        System.out.println("not inseted");
    }
    }
    
    public void display() throws ClassNotFoundException, SQLException
    {
        Connection c = dbconnection();
        Statement s=c.createStatement();
        ResultSet res= s.executeQuery("select * from students");
        while(res.next())
        {
            System.out.println("id"+res.getInt("id"));
            System.out.println("name"+res.getString("name"));
            System.out.println("phonenumber"+res.getLong("phonenumber"));
            
        }
        
    }
    
    
    public void update(int id,String name,long phone) throws ClassNotFoundException, SQLException
        {
             Connection c=dbconnection();
            PreparedStatement ps=c.prepareStatement("update students set name = ?, phonenumber= ? where id = ?");
            ps.setInt(3, id);
            ps.setString(1, name);
            ps.setLong(2, phone);
            int res=ps.executeUpdate();
            if(res>0)
            {
                System.out.println("updated");
            }
            else
            {
                System.out.println("not updateddd");
            }
         }
    
    public void delete(int id) throws ClassNotFoundException, SQLException
    {
         Connection c=dbconnection();
         PreparedStatement ps=c.prepareStatement("delete from students where id =?");
         ps.setInt(1, id);
         int res=ps.executeUpdate();
         
         if(res>0)
            {
                System.out.println("deleted");
            }
            else
            {
                System.out.println("not deleted");
            }
    }
}
