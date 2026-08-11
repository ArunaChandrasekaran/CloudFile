package Clients;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author aruna
 */
public class ClientsDao {

    public Connection dbconnect() throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        return DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/Thalam", "postgres", "Arunask@12");
    }

    public void insert(ClientsModel c) throws SQLException, ClassNotFoundException {
        Connection con = dbconnect();
        PreparedStatement ps = con.prepareStatement(
                "INSERT INTO Clients(Name, Phone, Email, AltPhone, Address) VALUES (?,?,?,?,?)");

        ps.setString(1, c.getName());
        ps.setString(2, c.getPhone());
        ps.setString(3, c.getEmail());
        ps.setString(4, c.getAltPhone());
        ps.setString(5, c.getAddress());
        ps.executeUpdate();

        ps.close();
        con.close();
    }

    public void update(ClientsModel c) throws SQLException, ClassNotFoundException {
        Connection con = dbconnect();
        PreparedStatement ps = con.prepareStatement(
                "UPDATE Clients SET Name = ?, Phone = ?, Email = ?, AltPhone = ?, Address = ? "
                        + "WHERE id = ?");

        ps.setString(1, c.getName());
        ps.setString(2, c.getPhone());
        ps.setString(3, c.getEmail());
        ps.setString(4, c.getAltPhone());
        ps.setString(5, c.getAddress());
        ps.setInt(6, c.getId());
        ps.executeUpdate();

        ps.close();
        con.close();
    }

    public void delete(int id) throws SQLException, ClassNotFoundException {
        Connection con = dbconnect();
        PreparedStatement ps = con.prepareStatement("DELETE FROM Clients WHERE id = ?");
        ps.setInt(1, id);
        ps.executeUpdate();
        ps.close();
        con.close();
    }

    public ArrayList<ClientsModel> view() throws ClassNotFoundException, SQLException {
        ArrayList<ClientsModel> a = new ArrayList<>();
        Connection con = dbconnect();
        PreparedStatement ps = con.prepareStatement("SELECT * FROM Clients ORDER BY id");
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            ClientsModel c = new ClientsModel();
            c.setId(rs.getInt("id"));
            c.setName(rs.getString("name"));
            c.setPhone(rs.getString("phone"));
            c.setEmail(rs.getString("email"));
            c.setAltPhone(rs.getString("altPhone"));
            c.setAddress(rs.getString("address"));
            a.add(c);
        }

        rs.close();
        ps.close();
        con.close();
        return a;
    }
}
