package Employees.Roles;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class RoleDao 
{
    
      public Connection dbconnect() throws ClassNotFoundException, SQLException
    {
    Class.forName("org.postgresql.Driver");
    Connection con=DriverManager.getConnection("jdbc:postgresql://localhost:5432/Thalam","postgres","Arunask@12");
    
    return con;
    }
    
    public void insert(RoleModel modelObject) throws SQLException, ClassNotFoundException
    {
         Connection con=  dbconnect();
       
       PreparedStatement ps= con.prepareStatement("insert into Roles(name,description)values(?,?)");
       
       ps.setString(1,modelObject.getName());
       ps.setString(2, modelObject.getDescription());
       
       
       ps.executeUpdate();
    }

    public void update(RoleModel modelObject) throws SQLException, ClassNotFoundException {
        Connection con = dbconnect();
        PreparedStatement ps = con.prepareStatement(
                "UPDATE Roles SET name = ?, description = ? WHERE id = ?");

        ps.setString(1, modelObject.getName());
        ps.setString(2, modelObject.getDescription());
        ps.setInt(3, modelObject.getId());
        ps.executeUpdate();

        ps.close();
        con.close();
    }

    public void delete(int id) throws SQLException, ClassNotFoundException {
        Connection con = dbconnect();
        PreparedStatement ps = con.prepareStatement("DELETE FROM Roles WHERE id = ?");
        ps.setInt(1, id);
        ps.executeUpdate();
        ps.close();
        con.close();
    }
    
    public ArrayList<RoleModel> view() throws ClassNotFoundException, SQLException
{
    ArrayList<RoleModel> a=new ArrayList<>();
    Connection con = dbconnect();
    PreparedStatement ps=con.prepareStatement("select * from Roles");
   ResultSet rs= ps.executeQuery();
   
   while(rs.next())
   {
   RoleModel modelObject=new RoleModel();
   modelObject.setName(rs.getString("name"));
   modelObject.setDescription(rs.getString("description"));
   modelObject.setId(rs.getInt("id"));
   
      
   a.add(modelObject);
   }

   return a;
}

    /** Creates Super Admin role if missing and returns its id. */
    public int ensureSuperAdminRole() throws ClassNotFoundException, SQLException {
        Connection con = dbconnect();
        PreparedStatement find = con.prepareStatement(
                "SELECT id FROM Roles WHERE LOWER(name) = LOWER(?) LIMIT 1");
        find.setString(1, "Super Admin");
        ResultSet rs = find.executeQuery();
        if (rs.next()) {
            int id = rs.getInt(1);
            rs.close();
            find.close();
            con.close();
            return id;
        }
        rs.close();
        find.close();

        PreparedStatement ins = con.prepareStatement(
                "INSERT INTO Roles(name, description) VALUES (?, ?)",
                java.sql.Statement.RETURN_GENERATED_KEYS);
        ins.setString(1, "Super Admin");
        ins.setString(2, "Full access administrator");
        ins.executeUpdate();
        ResultSet keys = ins.getGeneratedKeys();
        if (!keys.next()) {
            keys.close();
            ins.close();
            con.close();
            throw new SQLException("Failed to create Super Admin role");
        }
        int id = keys.getInt(1);
        keys.close();
        ins.close();
        con.close();
        return id;
    }
}
