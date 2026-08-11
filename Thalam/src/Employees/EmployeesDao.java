package Employees;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class EmployeesDao {

    public Connection dbconnect() throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        return DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/Thalam", "postgres", "Arunask@12");
    }

    public void insert(EmployeesModel model) throws SQLException, ClassNotFoundException {
        insertReturningId(model);
    }

    public int insertReturningId(EmployeesModel model) throws SQLException, ClassNotFoundException {
        Connection con = dbconnect();
        PreparedStatement ps = con.prepareStatement(
                "INSERT INTO Employees(name, phone, email, address, role_id) VALUES (?,?,?,?,?)",
                Statement.RETURN_GENERATED_KEYS);

        ps.setString(1, model.getName());
        ps.setString(2, model.getPhone());
        ps.setString(3, model.getEmail());
        ps.setString(4, model.getAddress());
        ps.setInt(5, model.getRoleId());

        ps.executeUpdate();
        ResultSet keys = ps.getGeneratedKeys();
        if (!keys.next()) {
            keys.close();
            ps.close();
            con.close();
            throw new SQLException("Failed to get generated employee id");
        }
        int id = keys.getInt(1);
        model.setId(id);
        keys.close();
        ps.close();
        con.close();
        return id;
    }

    public void update(EmployeesModel model) throws SQLException, ClassNotFoundException {
        Connection con = dbconnect();
        PreparedStatement ps = con.prepareStatement(
                "UPDATE Employees SET name = ?, phone = ?, email = ?, address = ?, role_id = ? "
                        + "WHERE id = ?");

        ps.setString(1, model.getName());
        ps.setString(2, model.getPhone());
        ps.setString(3, model.getEmail());
        ps.setString(4, model.getAddress());
        ps.setInt(5, model.getRoleId());
        ps.setInt(6, model.getId());
        ps.executeUpdate();

        ps.close();
        con.close();
    }

    public void delete(int id) throws SQLException, ClassNotFoundException {
        Connection con = dbconnect();
        PreparedStatement ps = con.prepareStatement("DELETE FROM Employees WHERE id = ?");
        ps.setInt(1, id);
        ps.executeUpdate();
        ps.close();
        con.close();
    }

    public ArrayList<EmployeesModel> view() throws ClassNotFoundException, SQLException {
        ArrayList<EmployeesModel> list = new ArrayList<>();
        Connection con = dbconnect();
        PreparedStatement ps = con.prepareStatement(
                "SELECT e.id, e.name, e.phone, e.email, e.address, "
                        + "e.role_id, r.name AS role_name "
                        + "FROM Employees e "
                        + "INNER JOIN Roles r ON e.role_id = r.id "
                        + "ORDER BY e.id");
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            EmployeesModel model = new EmployeesModel();
            model.setId(rs.getInt("id"));
            model.setName(rs.getString("name"));
            model.setPhone(rs.getString("phone"));
            model.setEmail(rs.getString("email"));
            model.setAddress(rs.getString("address"));
            model.setRoleId(rs.getInt("role_id"));
            model.setRole(rs.getString("role_name"));
            list.add(model);
        }

        rs.close();
        ps.close();
        con.close();
        return list;
    }

    public ArrayList<EmployeesModel> viewRecentByRole(int roleId, int limit)
            throws ClassNotFoundException, SQLException {
        ArrayList<EmployeesModel> list = new ArrayList<>();
        Connection con = dbconnect();
        PreparedStatement ps = con.prepareStatement(
                "SELECT e.id, e.name, e.phone "
                        + "FROM Employees e "
                        + "WHERE e.role_id = ? "
                        + "ORDER BY e.id DESC LIMIT ?");
        ps.setInt(1, roleId);
        ps.setInt(2, limit);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            EmployeesModel model = new EmployeesModel();
            model.setId(rs.getInt("id"));
            model.setName(rs.getString("name"));
            model.setPhone(rs.getString("phone"));
            list.add(model);
        }
        rs.close();
        ps.close();
        con.close();
        return list;
    }
}
