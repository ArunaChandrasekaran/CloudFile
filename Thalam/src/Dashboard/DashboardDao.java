package Dashboard;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author aruna
 */
public class DashboardDao {

    public Connection dbconnect() throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        return DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/Thalam", "postgres", "Arunask@12");
    }

    public int getTotalProjects() throws ClassNotFoundException, SQLException {
        int count = 0;
        Connection con = dbconnect();
        PreparedStatement ps = con.prepareStatement("SELECT COUNT(*) FROM Projects");
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            count = rs.getInt(1);
        }

        rs.close();
        ps.close();
        con.close();
        return count;
    }

    public int getTotalClients() throws ClassNotFoundException, SQLException {
        int count = 0;
        Connection con = dbconnect();
        PreparedStatement ps = con.prepareStatement("SELECT COUNT(*) FROM Clients");
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            count = rs.getInt(1);
        }

        rs.close();
        ps.close();
        con.close();
        return count;
    }

    public int getTotalEmployees() throws ClassNotFoundException, SQLException {
        int count = 0;
        Connection con = dbconnect();
        PreparedStatement ps = con.prepareStatement("SELECT COUNT(*) FROM Employees");
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            count = rs.getInt(1);
        }
        System.out.println(count);
        rs.close();
        ps.close();
        con.close();
        return count;
    }

    public double getPendingExpenseAmount() throws ClassNotFoundException, SQLException {
        double amount = 0;
        Connection con = dbconnect();
        PreparedStatement ps = con.prepareStatement(
                "SELECT COALESCE(SUM(amount), 0) FROM Expenses WHERE is_paid = ?");
        ps.setBoolean(1, false);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            amount = rs.getDouble(1);
        }

        rs.close();
        ps.close();
        con.close();
        return amount;
    }

    public double getPendingInvoiceAmount() throws ClassNotFoundException, SQLException {
        double amount = 0;
        Connection con = dbconnect();
        PreparedStatement ps = con.prepareStatement(
                "SELECT COALESCE(SUM(invoice_amount), 0) FROM Invoices WHERE is_paid = ?");
        ps.setBoolean(1, false);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            amount = rs.getDouble(1);
        }

        rs.close();
        ps.close();
        con.close();
        return amount;
    }

    public double getTotalInvoiceAmount() throws ClassNotFoundException, SQLException {
        double amount = 0;
        Connection con = dbconnect();
        PreparedStatement ps = con.prepareStatement(
                "SELECT COALESCE(SUM(invoice_amount), 0) FROM Invoices");
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            amount = rs.getDouble(1);
        }

        rs.close();
        ps.close();
        con.close();
        return amount;
    }

    public double getTotalExpenseAmount() throws ClassNotFoundException, SQLException {
        double amount = 0;
        Connection con = dbconnect();
        PreparedStatement ps = con.prepareStatement(
                "SELECT COALESCE(SUM(amount), 0) FROM Expenses");
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            amount = rs.getDouble(1);
        }

        rs.close();
        ps.close();
        con.close();
        return amount;
    }
}
