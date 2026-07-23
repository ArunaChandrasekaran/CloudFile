/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package School;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.*;

/**
 *
 * @author aruna
 */
public class Dao
{
    
    public void insertdao(int id , String name, String course, String email,double fee) throws ClassNotFoundException, SQLException
    {
        Connection c= dbconnection();
        PreparedStatement ps= c.prepareStatement("insert into Students values(?,?,?,?,?)");
        ps.setInt(1, id);
        ps.setString(2, name);
        ps.setString(3,course);
        ps.setString(4,email);
        ps.setDouble(5, fee);
        int res =ps.executeUpdate();
        if(res>0)
        {
            System.out.println("inserted");
               
        }
        else
        {
           System.out.println("not inserted");
                
        }
        
    }
    
    public void updatedao(int id , String name, String course, String email,double fee) throws ClassNotFoundException, SQLException
    {
        Connection c= dbconnection();
        PreparedStatement ps= c.prepareStatement("update students set name = ? , course = ?, email = ?, fee = ? where id =? ");
        ps.setString(1, name);
        ps.setString(2, course);
        ps.setString(3,email);
        ps.setDouble(4,fee);
        ps.setInt(5, id);
        int res =ps.executeUpdate();
        if(res>0)
        {
            System.out.println("updated");
               
        }
        else
        {
           System.out.println("not updateddd");
                
        }
        
    }
    
    public void deletedao(int id) throws ClassNotFoundException, SQLException
    {
        Connection c= dbconnection();
        PreparedStatement ps= c.prepareStatement("delete from Students where id = ?");
        ps.setInt(1, id);
        
        int res =ps.executeUpdate();
        if(res>0)
        {
            System.out.println("deleted");
               
        }
        else
        {
           System.out.println("not deleted");
                
        }
        
    }
    public Connection dbconnection() throws ClassNotFoundException, SQLException
    {
        Class.forName("com.mysql.cj.jdbc.Driver");
        
        Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/School","root","Arunask@12");
        
        return c;
    }
}
