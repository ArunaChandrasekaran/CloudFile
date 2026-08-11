package Projects;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

/**
 *
 * @author aruna
 */
public class ProjectDao {

    public Connection dbconnect() throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        return DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/Thalam", "postgres", "Arunask@12");
    }

    public void insert(ProjectsModel model) throws SQLException, ClassNotFoundException {
        Connection con = dbconnect();
        con.setAutoCommit(false);

        try {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO Projects(name, client_id, startdate, enddate, address, "
                            + "contractamount, notes, status) VALUES (?,?,?,?,?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, model.getProjectName());
            ps.setInt(2, model.getClientId());
            ps.setDate(3, Date.valueOf(model.getStarDate()));

            if (model.getExpectedEndDate() != null) {
                ps.setDate(4, Date.valueOf(model.getExpectedEndDate()));
            } else {
                ps.setNull(4, Types.DATE);
            }

            ps.setString(5, model.getAddress());
            ps.setDouble(6, model.getContractAmount());
            ps.setString(7, model.getNotes());
            model.applyResolvedStatus();
            ps.setString(8, model.getStatus());

            ps.executeUpdate();

            ResultSet keys = ps.getGeneratedKeys();
            if (!keys.next()) {
                throw new SQLException("Failed to get generated project id");
            }
            int projectId = keys.getInt(1);
            keys.close();
            ps.close();

            PreparedStatement pe = con.prepareStatement(
                    "INSERT INTO ProjectsAssociatedEmployees(project_id, employee_id) VALUES (?, ?)");

            for (Integer employeeId : model.getEmployeeIds()) {
                pe.setInt(1, projectId);
                pe.setInt(2, employeeId);
                pe.addBatch();
            }
            pe.executeBatch();
            pe.close();

            con.commit();
        } catch (SQLException ex) {
            con.rollback();
            throw ex;
        } finally {
            con.close();
        }
    }

    public void update(ProjectsModel model) throws SQLException, ClassNotFoundException {
        Connection con = dbconnect();
        con.setAutoCommit(false);

        try {
            PreparedStatement ps = con.prepareStatement(
                    "UPDATE Projects SET name = ?, client_id = ?, startdate = ?, enddate = ?, "
                            + "address = ?, contractamount = ?, notes = ?, status = ? WHERE id = ?");

            ps.setString(1, model.getProjectName());
            ps.setInt(2, model.getClientId());
            ps.setDate(3, Date.valueOf(model.getStarDate()));

            if (model.getExpectedEndDate() != null) {
                ps.setDate(4, Date.valueOf(model.getExpectedEndDate()));
            } else {
                ps.setNull(4, Types.DATE);
            }

            ps.setString(5, model.getAddress());
            ps.setDouble(6, model.getContractAmount());
            ps.setString(7, model.getNotes());
            model.applyResolvedStatus();
            ps.setString(8, model.getStatus());
            ps.setInt(9, model.getId());
            ps.executeUpdate();
            ps.close();

            PreparedStatement del = con.prepareStatement(
                    "DELETE FROM ProjectsAssociatedEmployees WHERE project_id = ?");
            del.setInt(1, model.getId());
            del.executeUpdate();
            del.close();

            PreparedStatement pe = con.prepareStatement(
                    "INSERT INTO ProjectsAssociatedEmployees(project_id, employee_id) VALUES (?, ?)");

            for (Integer employeeId : model.getEmployeeIds()) {
                pe.setInt(1, model.getId());
                pe.setInt(2, employeeId);
                pe.addBatch();
            }
            pe.executeBatch();
            pe.close();

            con.commit();
        } catch (SQLException ex) {
            con.rollback();
            throw ex;
        } finally {
            con.close();
        }
    }

    public void delete(int id) throws SQLException, ClassNotFoundException {
        Connection con = dbconnect();
        con.setAutoCommit(false);

        try {
            PreparedStatement delAssoc = con.prepareStatement(
                    "DELETE FROM ProjectsAssociatedEmployees WHERE project_id = ?");
            delAssoc.setInt(1, id);
            delAssoc.executeUpdate();
            delAssoc.close();

            PreparedStatement del = con.prepareStatement("DELETE FROM Projects WHERE id = ?");
            del.setInt(1, id);
            del.executeUpdate();
            del.close();

            con.commit();
        } catch (SQLException ex) {
            con.rollback();
            throw ex;
        } finally {
            con.close();
        }
    }

    public java.util.ArrayList<ProjectsModel> view() throws ClassNotFoundException, SQLException {
        java.util.ArrayList<ProjectsModel> list = new java.util.ArrayList<>();
        Connection con = dbconnect();
        refreshStatuses(con);
        PreparedStatement ps = con.prepareStatement(
                "SELECT p.id, p.name, p.client_id, p.startdate, p.enddate, "
                        + "p.address, p.contractamount, p.notes, p.status, "
                        + "c.name AS client_name "
                        + "FROM Projects p "
                        + "INNER JOIN Clients c ON p.client_id = c.id "
                        + "ORDER BY p.id");
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            ProjectsModel model = new ProjectsModel();
            model.setId(rs.getInt("id"));
            model.setProjectName(rs.getString("name"));
            model.setClientId(rs.getInt("client_id"));
            model.setClient(rs.getString("client_name"));

            Date start = rs.getDate("startdate");
            if (start != null) {
                model.setStarDate(start.toLocalDate());
            }

            Date end = rs.getDate("enddate");
            if (end != null) {
                model.setExpectedEndDate(end.toLocalDate());
            }

            model.setAddress(rs.getString("address"));
            model.setContractAmount(rs.getDouble("contractamount"));
            model.setNotes(rs.getString("notes"));
            model.setStatus(rs.getString("status"));
            model.applyResolvedStatus();
            list.add(model);
        }

        rs.close();
        ps.close();
        con.close();
        return list;
    }

    /** Keep non-completed project statuses current from dates. */
    private void refreshStatuses(Connection con) throws SQLException {
        try (PreparedStatement notStarted = con.prepareStatement(
                "UPDATE Projects SET status = 'Not yet started' "
                        + "WHERE status IS DISTINCT FROM 'Completed' "
                        + "AND startdate IS NOT NULL AND startdate > CURRENT_DATE "
                        + "AND status IS DISTINCT FROM 'Not yet started'")) {
            notStarted.executeUpdate();
        }
        try (PreparedStatement late = con.prepareStatement(
                "UPDATE Projects SET status = 'Late' "
                        + "WHERE status IS DISTINCT FROM 'Completed' "
                        + "AND enddate IS NOT NULL AND enddate < CURRENT_DATE "
                        + "AND (startdate IS NULL OR startdate <= CURRENT_DATE) "
                        + "AND status IS DISTINCT FROM 'Late'")) {
            late.executeUpdate();
        }
        try (PreparedStatement ongoing = con.prepareStatement(
                "UPDATE Projects SET status = 'Ongoing' "
                        + "WHERE status IS DISTINCT FROM 'Completed' "
                        + "AND (startdate IS NULL OR startdate <= CURRENT_DATE) "
                        + "AND (enddate IS NULL OR enddate >= CURRENT_DATE) "
                        + "AND status IS DISTINCT FROM 'Ongoing'")) {
            ongoing.executeUpdate();
        }
    }

    public void markCompleted(int projectId) throws ClassNotFoundException, SQLException {
        Connection con = dbconnect();
        PreparedStatement ps = con.prepareStatement(
                "UPDATE Projects SET status = ? WHERE id = ?");
        ps.setString(1, ProjectsModel.STATUS_COMPLETED);
        ps.setInt(2, projectId);
        ps.executeUpdate();
        ps.close();
        con.close();
    }

    public java.util.ArrayList<ProjectsModel> viewRecentByClient(int clientId, int limit)
            throws ClassNotFoundException, SQLException {
        java.util.ArrayList<ProjectsModel> list = new java.util.ArrayList<>();
        Connection con = dbconnect();
        refreshStatuses(con);
        PreparedStatement ps = con.prepareStatement(
                "SELECT p.id, p.name, p.status "
                        + "FROM Projects p "
                        + "WHERE p.client_id = ? "
                        + "ORDER BY p.id DESC LIMIT ?");
        ps.setInt(1, clientId);
        ps.setInt(2, limit);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            ProjectsModel model = new ProjectsModel();
            model.setId(rs.getInt("id"));
            model.setProjectName(rs.getString("name"));
            model.setStatus(rs.getString("status"));
            list.add(model);
        }
        rs.close();
        ps.close();
        con.close();
        return list;
    }

    public java.util.ArrayList<ProjectsModel> viewRecentByEmployee(int employeeId, int limit)
            throws ClassNotFoundException, SQLException {
        java.util.ArrayList<ProjectsModel> list = new java.util.ArrayList<>();
        Connection con = dbconnect();
        refreshStatuses(con);
        PreparedStatement ps = con.prepareStatement(
                "SELECT p.id, p.name, p.status "
                        + "FROM Projects p "
                        + "INNER JOIN ProjectsAssociatedEmployees pae ON pae.project_id = p.id "
                        + "WHERE pae.employee_id = ? "
                        + "ORDER BY p.id DESC LIMIT ?");
        ps.setInt(1, employeeId);
        ps.setInt(2, limit);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            ProjectsModel model = new ProjectsModel();
            model.setId(rs.getInt("id"));
            model.setProjectName(rs.getString("name"));
            model.setStatus(rs.getString("status"));
            list.add(model);
        }
        rs.close();
        ps.close();
        con.close();
        return list;
    }

    public java.util.ArrayList<Employees.EmployeesModel> viewAssociatedEmployees(int projectId)
            throws ClassNotFoundException, SQLException {
        java.util.ArrayList<Employees.EmployeesModel> list = new java.util.ArrayList<>();
        Connection con = dbconnect();
        PreparedStatement ps = con.prepareStatement(
                "SELECT e.id, e.name, e.phone, e.email "
                        + "FROM ProjectsAssociatedEmployees pae "
                        + "INNER JOIN Employees e ON e.id = pae.employee_id "
                        + "WHERE pae.project_id = ? "
                        + "ORDER BY e.name");
        ps.setInt(1, projectId);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Employees.EmployeesModel model = new Employees.EmployeesModel();
            model.setId(rs.getInt("id"));
            model.setName(rs.getString("name"));
            model.setPhone(rs.getString("phone"));
            model.setEmail(rs.getString("email"));
            list.add(model);
        }

        rs.close();
        ps.close();
        con.close();
        return list;
    }
}
