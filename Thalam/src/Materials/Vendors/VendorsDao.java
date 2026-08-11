package Materials.Vendors;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class VendorsDao 
{
    
    public Connection dbconnect() throws ClassNotFoundException, SQLException
    {
    Class.forName("org.postgresql.Driver");
    Connection con=DriverManager.getConnection("jdbc:postgresql://localhost:5432/Thalam","postgres","Arunask@12");
    
    return con;
    }
    
    public void insert(VendorsModel v) throws SQLException, ClassNotFoundException
    {
         Connection con=  dbconnect();
       
       PreparedStatement ps= con.prepareStatement("insert into Vendors(Name, Phone,Email,AltPhone,Address)values(?,?,?,?,?)");
       
       ps.setString(1,v.getName());
       ps.setString(2, v.getPhone());
       ps.setString(3,v.getEmail());
       ps.setString(4,v.getAltPhone());
       ps.setString(5,v.getAddress());
       
       ps.executeUpdate();
    }

    public void update(VendorsModel v) throws SQLException, ClassNotFoundException {
        Connection con = dbconnect();
        PreparedStatement ps = con.prepareStatement(
                "UPDATE Vendors SET Name = ?, Phone = ?, Email = ?, AltPhone = ?, Address = ? "
                        + "WHERE id = ?");

        ps.setString(1, v.getName());
        ps.setString(2, v.getPhone());
        ps.setString(3, v.getEmail());
        ps.setString(4, v.getAltPhone());
        ps.setString(5, v.getAddress());
        ps.setInt(6, v.getId());
        ps.executeUpdate();

        ps.close();
        con.close();
    }

    public void delete(int id) throws SQLException, ClassNotFoundException {
        Connection con = dbconnect();
        PreparedStatement ps = con.prepareStatement("DELETE FROM Vendors WHERE id = ?");
        ps.setInt(1, id);
        ps.executeUpdate();
        ps.close();
        con.close();
    }
    
    public ArrayList<VendorsModel> view() throws ClassNotFoundException, SQLException
{
    ArrayList<VendorsModel> a=new ArrayList<>();
    Connection con = dbconnect();
    PreparedStatement ps=con.prepareStatement("select * from Vendors");
   ResultSet rs= ps.executeQuery();
   
   while(rs.next())
   {
    VendorsModel v=new VendorsModel();
   v.setName(rs.getString("name"));
   v.setPhone(rs.getString("phone"));
   v.setEmail(rs.getString("email"));
   v.setAltPhone(rs.getString("altPhone"));
   v.setAddress(rs.getString("address"));
   v.setId(rs.getInt("id"));
      
   a.add(v);
   }

   return a;
}
}
