/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package School;

/**
 *
 * @author aruna
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.*;

public class Service
{
 
    public void updateservice(int id ,String name,String course,String email,double fee) throws ClassNotFoundException, SQLException
    {
        Dao d=new Dao();
        d.updatedao(id, name, course,email,fee);
    }
    
    public void insertservice(int id ,String name,String course,String email,double fee) throws ClassNotFoundException, SQLException
    {
        Dao d=new Dao();
        d.insertdao(id, name, course,email,fee);
    }
    
    public void deleteservice(int id) throws ClassNotFoundException, SQLException
    {
        Dao d=new Dao();
        d.deletedao(id);
    }
}
