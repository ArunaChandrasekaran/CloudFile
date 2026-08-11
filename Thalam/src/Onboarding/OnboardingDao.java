package Onboarding;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 *
 * @author aruna
 */
public class OnboardingDao {

    public Connection dbconnect() throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        return DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/Thalam", "postgres", "Arunask@12");
    }

    public boolean hasOnboarding() throws ClassNotFoundException, SQLException {
        Connection con = dbconnect();
        PreparedStatement ps = con.prepareStatement(
                "SELECT 1 FROM OnboardingDetails LIMIT 1");
        ResultSet rs = ps.executeQuery();
        boolean exists = rs.next();
        rs.close();
        ps.close();
        con.close();
        return exists;
    }

    public void insert(OnboardingModel model) throws ClassNotFoundException, SQLException {
        Connection con = dbconnect();
        PreparedStatement ps = con.prepareStatement(
                "INSERT INTO OnboardingDetails("
                        + "company_name, company_address, username, password, "
                        + "forgot_pwd_phrase, employee_id) "
                        + "VALUES (?,?,?,?,?,?)");

        ps.setString(1, model.getCompanyName());
        ps.setString(2, model.getCompanyAddress());
        ps.setString(3, model.getUsername());
        ps.setString(4, model.getPassword());
        ps.setString(5, model.getForgotPwdPhrase());
        if (model.getEmployeeId() != null && model.getEmployeeId() > 0) {
            ps.setInt(6, model.getEmployeeId());
        } else {
            ps.setNull(6, Types.INTEGER);
        }

        ps.executeUpdate();
        ps.close();
        con.close();
    }

    public OnboardingModel findByUsername(String username)
            throws ClassNotFoundException, SQLException {
        Connection con = dbconnect();
        PreparedStatement ps = con.prepareStatement(
                "SELECT id, company_name, company_address, username, password, "
                        + "forgot_pwd_phrase, employee_id "
                        + "FROM OnboardingDetails WHERE username = ?");
        ps.setString(1, username);
        ResultSet rs = ps.executeQuery();

        OnboardingModel model = null;
        if (rs.next()) {
            model = mapRow(rs);
        }

        rs.close();
        ps.close();
        con.close();
        return model;
    }

    public boolean validateLogin(String username, String password)
            throws ClassNotFoundException, SQLException {
        Connection con = dbconnect();
        PreparedStatement ps = con.prepareStatement(
                "SELECT 1 FROM OnboardingDetails WHERE username = ? AND password = ?");
        ps.setString(1, username);
        ps.setString(2, password);
        ResultSet rs = ps.executeQuery();
        boolean ok = rs.next();
        rs.close();
        ps.close();
        con.close();
        return ok;
    }

    public String findPasswordByPhrase(String username, String phrase)
            throws ClassNotFoundException, SQLException {
        Connection con = dbconnect();
        PreparedStatement ps = con.prepareStatement(
                "SELECT password FROM OnboardingDetails "
                        + "WHERE username = ? AND forgot_pwd_phrase = ?");
        ps.setString(1, username);
        ps.setString(2, phrase);
        ResultSet rs = ps.executeQuery();
        String password = null;
        if (rs.next()) {
            password = rs.getString("password");
        }
        rs.close();
        ps.close();
        con.close();
        return password;
    }

    private static OnboardingModel mapRow(ResultSet rs) throws SQLException {
        OnboardingModel model = new OnboardingModel();
        model.setId(rs.getInt("id"));
        model.setCompanyName(rs.getString("company_name"));
        model.setCompanyAddress(rs.getString("company_address"));
        model.setUsername(rs.getString("username"));
        model.setPassword(rs.getString("password"));
        model.setForgotPwdPhrase(rs.getString("forgot_pwd_phrase"));
        int employeeId = rs.getInt("employee_id");
        if (!rs.wasNull()) {
            model.setEmployeeId(employeeId);
        }
        return model;
    }
}
